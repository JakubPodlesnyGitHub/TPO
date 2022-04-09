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
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {
    private String host;
    private int port;
    private SocketChannel socketChannel;
    private Selector selector;
    private String id;
    private StringBuilder stringBuilder;

    public Client(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
        stringBuilder = new StringBuilder();
    }

    public void connect() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.socket().connect(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public String send(String req) {
        try {
            ByteBuffer byteSendBuffer = ByteBuffer.wrap(req.getBytes(StandardCharsets.UTF_8));
            ByteBuffer byteReceiveBuffer = ByteBuffer.allocate(7000);
            StringBuffer stringBuffer = new StringBuffer();
            boolean ifResponseArrived = false;
            byteReceiveBuffer.clear();
            stringBuffer.setLength(0);
            socketChannel.write(byteSendBuffer);
            while (!ifResponseArrived) {
                int responseSize = socketChannel.read(byteReceiveBuffer);
                if (responseSize > 0) {
                    byteReceiveBuffer.flip();
                    CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteReceiveBuffer);
                    while (charBuffer.hasRemaining()) {
                        char c = charBuffer.get();
                        stringBuffer.append(c);
                    }
                    ifResponseArrived = true;
                }
            }

            if (stringBuffer.toString().startsWith("Od") && stringBuilder.toString().contains("=== " + id + " log start ===" + "\n" + "logged in"))
                stringBuilder.append("Request: ").append(req).append("\n").append("Result:").append("\n");

            stringBuilder.append(stringBuffer.toString()).append("\n");
            if (req.equals("bye and log transfer") || req.equals("bye")) {
                socketChannel.close();
                return stringBuilder.toString();
            } else
                return stringBuffer.toString();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return "";
    }

    public String getId() {
        return id;
    }
}
