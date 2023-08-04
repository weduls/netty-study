package org.example.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class JavaIOClient {

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", 8080));

            OutputStream out = socket.getOutputStream();
            String requestBody = "This is client";
            out.write(requestBody.getBytes());
            out.flush();

            InputStream in = socket.getInputStream();
            byte[] responseBytes =  new byte[1024];
            in.read(responseBytes);
            System.out.printf("result : %s", new String(responseBytes).trim());
        }
    }

}
