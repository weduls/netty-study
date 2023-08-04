package org.example.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class JavaNIOBlockingServer {

    public static void main(String args[]) throws IOException {
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));

            while (true) {
                SocketChannel clientSocket = serverSocket.accept();
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
