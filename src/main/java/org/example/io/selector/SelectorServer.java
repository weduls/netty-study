package org.example.io.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class SelectorServer {

    public static void main(String args[]) throws IOException, InterruptedException {
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open();
             Selector selector = Selector.open();
        ) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));
            // non blocking으로 변경하는 방법
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // blocking 하면서 요청을 대기 (이부분이 Nio blocking과 다른 코드)
                selector.select();
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    if (key.isAcceptable()) {
                        // null이 될수 없음
                        SocketChannel clientSocket = ((ServerSocketChannel)key.channel()).accept();
                        clientSocket.configureBlocking(false);
                        clientSocket.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel clientSocket = ((SocketChannel)key.channel());
                        ByteBuffer requestByteBuffer = ByteBuffer.allocateDirect(1024);
                        clientSocket.read(requestByteBuffer);
                        requestByteBuffer.flip();
                        String requestBody = StandardCharsets.UTF_8.decode(requestByteBuffer).toString();
                        System.out.printf("request: %s", requestBody);

                        ByteBuffer responseByteBuffer = ByteBuffer.wrap("This is nio server".getBytes());
                        clientSocket.write(responseByteBuffer);
                        clientSocket.close();
                    }
                }
            }
        }
    }

}
