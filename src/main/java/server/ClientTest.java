package server;

import configs.SocketConfig;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) throws InterruptedException {
        byte[] b = new byte[50];

        while (true) {
            try (Socket server = new Socket(SocketConfig.HOSTNAME, SocketConfig.PORT)) {
                OutputStream in = server.getOutputStream();
                InputStream out = server.getInputStream();
                int size = 0;

                long time = System.currentTimeMillis();

                in.write(b);
                server.shutdownOutput();

                do {
                    size += out.readAllBytes().length;
                    if (!server.isConnected()) {
                        throw new RuntimeException("Incomplete");
                    }
                } while (size != b.length);

                time = System.currentTimeMillis() - time;

                Logging.write(ClientTest.class, "Ping: " + time + "ms");
            } catch (IOException e) {
                Logging.error(ClientTest.class, "Echo IOException");
                throw new RuntimeException(e);
            }

            Thread.sleep(30);
        }
    }
}
