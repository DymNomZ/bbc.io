package server;

import configs.SocketConfig;
import utils.Logging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerEchoTest {
    public static void main(String[] args) {
        try (ServerSocket s = new ServerSocket(SocketConfig.PORT)) {
            while (true) {
                try (Socket client = s.accept()) {
                    s.setSoTimeout(5000);

                    client.getOutputStream().write(client.getInputStream().readAllBytes());
                    client.getOutputStream().close();
                } catch (IOException e) {
                    Logging.error(ServerEchoTest.class, "Client request dropped");
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
