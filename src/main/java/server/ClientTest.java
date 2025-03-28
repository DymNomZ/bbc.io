package server;

import configs.SocketConfig;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) {
        byte[] b = new byte[50];

        try (Socket server = new Socket(SocketConfig.HOSTNAME, SocketConfig.PORT)) {
            OutputStream in = server.getOutputStream();
            InputStream out = server.getInputStream();

            long time = System.currentTimeMillis();

            in.write(b);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
