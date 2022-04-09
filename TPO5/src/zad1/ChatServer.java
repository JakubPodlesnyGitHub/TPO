/**
 *
 *  @author Podleśny Jakub S20540
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;

public class ChatServer {
    private List<String> serverLogList;
    private Map<Integer, String> portLoginMap;
    private Map<SocketChannel, String> clients;
    //---------------------------------------------
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ExecutorService executorService;
    private boolean serverIsRunning = true;
    private String host;
    private int port;
    //----------------------------------------
    private Lock lock = new ReentrantLock();

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
        serverLogList = new ArrayList<>();
        clients = new HashMap<>();
        portLoginMap = new HashMap<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void startServer() {
        System.out.println("Server started\n");
        executorService.submit(this::prepareForRequest);
    }

    private void prepareForRequest() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(host, port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, serverSocketChannel.validOps(), null);

            while (serverIsRunning) {
                selector.select();
                if (selector.isOpen()) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();

                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            continue;
                        }

                        if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            String reqFromClient = receiveRequest(socketChannel);
                            String[] parts = reqFromClient.split("555");
                            for (String req : parts) {
                                String responseToClient = sendRightResponse(req, socketChannel);
                                for (Map.Entry<SocketChannel, String> entry : clients.entrySet()) {
                                    ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(responseToClient);
                                    entry.getKey().write(byteBuffer);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String receiveRequest(SocketChannel socketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(7000);
        StringBuffer reqStringBuffer = new StringBuffer();
        Charset charset = StandardCharsets.UTF_8;
        int numberOfReadingBytes = 0;
        do {
            try {
                lock.lock();
                numberOfReadingBytes = socketChannel.read(byteBuffer);
                byteBuffer.flip();
                reqStringBuffer.append(charset.decode(byteBuffer));
                byteBuffer.clear();
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                lock.unlock();
            }
        } while (numberOfReadingBytes != 0);

        return reqStringBuffer.toString();
    }

    private String sendRightResponse(String req, SocketChannel socketChannel) throws IOException {
        StringBuffer responseFromServer = new StringBuffer();
        String dateNOW = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        int clientPort = Integer.parseInt(socketChannel.getRemoteAddress().toString().split(":")[1]);

        if (req.startsWith("logged in")) {
            String[] loginPart = req.split("\\s");
            portLoginMap.put(clientPort, loginPart[2]);
            clients.put(socketChannel, portLoginMap.get(clientPort));
            serverLogList.add(dateNOW + " " + portLoginMap.get(clientPort) + " logged in" + "\n");
            responseFromServer.append(portLoginMap.get(clientPort) + " logged in" + "\n");
        } else if (req.startsWith("logged out")) {
            serverLogList.add(dateNOW + " " + portLoginMap.get(clientPort) + " logged out" + "\n");
            responseFromServer.append(portLoginMap.get(clientPort) + " logged out" + "\n");

            ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(responseFromServer.toString());
            socketChannel.write(byteBuffer);
            clients.remove(socketChannel);
        } else {
            serverLogList.add(dateNOW + " " + portLoginMap.get(clientPort) + ": " + req + "\n");
            responseFromServer.append(portLoginMap.get(clientPort) + ": " + req + "\n");
        }

        return responseFromServer.toString();
    }

    public void stopServer() {
        try {
            lock.lock();
            serverIsRunning = false;
            executorService.shutdownNow();
            selector.close();
            serverSocketChannel.close();
            System.out.println("Server stopped");
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String getServerLog() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String log : serverLogList) {
            stringBuilder.append(log);
        }
        return stringBuilder.toString();
    }

}