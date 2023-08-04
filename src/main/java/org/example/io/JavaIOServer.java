package org.example.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class JavaIOServer {

    public static void main(String args[]) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                byte[] requestBytes = new byte[1024];
                InputStream in = clientSocket.getInputStream();
                in.read(requestBytes);
                System.out.printf("request: %s", new String(requestBytes).trim());

                OutputStream out = clientSocket.getOutputStream();
                String response = "This is Server";
                out.write(response.getBytes());
                out.flush();
            }
        }
    }

}
