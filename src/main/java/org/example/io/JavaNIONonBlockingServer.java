package org.example.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class JavaNIONonBlockingServer {

    public static void main(String args[]) throws IOException, InterruptedException {
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));
            // non blocking으로 변경하는 방법
            serverSocket.configureBlocking(false);

            while (true) {
                SocketChannel clientSocket = serverSocket.accept();
                // bloking이 아니기 때문에 socket이 열려있는건 현재 없음 client 요청이 들어와야 그때부터 socket open
                if (clientSocket == null) {
                    Thread.sleep(100);
                    continue;
                }

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
