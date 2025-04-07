package server;

import configs.SocketConfig;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.UUID;

public class ServerMain {
    public static void main(String[] args) throws InterruptedException {
        ServerSocket listener;

        while (true) {
            try  {
                listener = new ServerSocket(SocketConfig.PORT);
                break;
            } catch (IOException e) {
                Logging.error(ServerMain.class, "Unable to initialize ServerSocket");
            }

            Thread.sleep(5000);
        }

        while (true) {
            try {
                Socket client = listener.accept();
                client.setSoTimeout(5000);
                InputStream stdout = client.getInputStream();
                OutputStream stdin = client.getOutputStream();

                try {
                    byte[] pass = stdout.readNBytes(26);

                    if (Arrays.equals(pass, SocketConfig.KEY)) {
                        stdin.write(255);
                        stdin.flush();
                        client.setSoTimeout(30000);
                        // handle client connection
                    } else {
                        client.close();
                        Logging.write(ServerMain.class, "Client Incorrect key passed");
                    }
                } catch (SocketTimeoutException e) {
                    client.close();
                    Logging.write(ServerMain.class, "Client Timeout reached");
                } catch (IOException e) {
                    client.close();
                    Logging.write(ServerMain.class, "IOException at ");
                }
            } catch (IOException e) {
                Logging.error(ServerMain.class, "IOException at listener.accept");
            }
        }
    }
}
