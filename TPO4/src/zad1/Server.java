/**
 * @author Podleśny Jakub S20540
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private String host;
    private int port;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private boolean serverIsRunning = true;
    private ExecutorService executorService;
    private List<String> logList;
    private Map<Integer, String> portLoginMap;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
        executorService = Executors.newSingleThreadExecutor();
        logList = new ArrayList<>();
        portLoginMap = new HashMap<>();
    }

    public void startServer() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(host, port));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.exit(1);
        }
        executorService.submit(this::prepareForRequest);
    }

    private void prepareForRequest() {
        while (serverIsRunning) {
            try {
                selector.select();
                Set keys = selector.selectedKeys();
                Iterator iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = (SelectionKey) iterator.next();
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
                        assert reqFromClient != null;
                        sendRightResponse(reqFromClient, socketChannel);
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private String receiveRequest(SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(7000);
        StringBuffer reqStringBuffer = new StringBuffer();
        if (!socketChannel.isOpen()) return null;
        byteBuffer.clear();
        int requestSize = socketChannel.read(byteBuffer);
        if (requestSize > 0) {
            byteBuffer.flip();
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
            while (charBuffer.hasRemaining()) {
                char c = charBuffer.get();
                if (c == '\n' || c == '\r') break;
                reqStringBuffer.append(c);
            }
            return reqStringBuffer.toString();
        }
        return null;
    }

    private void sendRightResponse(String req, SocketChannel channel) throws IOException {
        String dateNOW = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        int clientPort = Integer.parseInt(channel.getRemoteAddress().toString().split(":")[1]);
        if (req.startsWith("bye and log transfer")) {
            writeResponse("logged out" + "\n" + "=== " + portLoginMap.get(clientPort) + " log end ===", channel);
            logList.add(portLoginMap.get(clientPort) + " logged out at " + dateNOW);
            channel.close();
        } else if (req.startsWith("bye")) {
            writeResponse("\n" + "logged out" + "\n" + "=== " + portLoginMap.get(clientPort) + " log end ===", channel);
            logList.add(portLoginMap.get(clientPort) + " logged out at " + dateNOW);
            channel.close();
        } else if (req.startsWith("login ")) {
            putIntoMapLoginPort(req.split("\\s")[1], channel);
            writeResponse("=== " + portLoginMap.get(clientPort) + " log start ===" + "\n" + "logged in", channel);
            logList.add(portLoginMap.get(clientPort) + " logged in at " + dateNOW);
        } else {
            writeResponse(Time.passed(req.split("\\s")[0], req.split("\\s")[1]), channel);
            logList.add(portLoginMap.get(clientPort) + " request at " + dateNOW + ": \"" + req + "\"");
        }
    }

    private void putIntoMapLoginPort(String login, SocketChannel socketChannel) {
        try {
            if (!portLoginMap.containsKey(login))
                portLoginMap.put(Integer.parseInt(socketChannel.getRemoteAddress().toString().split(":")[1]), login);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void writeResponse(String response, SocketChannel socketChannel) throws IOException {
        if (socketChannel.isOpen()) {
            socketChannel.write(StandardCharsets.UTF_8.encode(CharBuffer.wrap(new StringBuffer().append(response))));
        }
    }

    public void stopServer() {
        serverIsRunning = false;
        try {
            serverSocketChannel.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        executorService.shutdownNow();
    }

    public String getServerLog() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String log : logList) {
            stringBuilder.append(log).append("\n");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
