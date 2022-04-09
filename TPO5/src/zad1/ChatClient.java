/**
 *
 *  @author Podleśny Jakub S20540
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChatClient {
    private String host;
    private int port;
    private String id;
    private final StringBuilder stringBuilder;
    private SocketChannel socketChannel;
    private ExecutorService executorService;
    private Lock lock = new ReentrantLock();
    private boolean tmp = true;

    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
        executorService = Executors.newSingleThreadExecutor();
        stringBuilder = new StringBuilder();
    }

    public void login() {
        stringBuilder.append("=== ").append(id).append(" chat view\n");
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
            send("logged in " + id);
            executorService.submit(this::receiveResponsesFromServer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void send(String req) {
        try {
            ByteBuffer byteReqBuffer = StandardCharsets.UTF_8.encode(req + "555");
            socketChannel.write(byteReqBuffer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void receiveResponsesFromServer() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(7000);
        int bytesRead = 0;
        while (tmp) {
            do {
                try {
                    lock.lock();
                    bytesRead = socketChannel.read(byteBuffer);
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    lock.unlock();
                }
            } while (bytesRead == 0 && !tmp);
            byteBuffer.flip();
            String response = StandardCharsets.UTF_8.decode(byteBuffer).toString();
            stringBuilder.append(response);
            if(response.startsWith(id + " logged out")){
                tmp = false;
            }
            byteBuffer.clear();
        }
    }

    public void logout() {
        send("logged out" + "555");
            try {
                lock.lock();
                executorService.shutdown();
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                lock.unlock();
            }
    }

    public String getChatView() {
        return stringBuilder.toString();
    }


}
