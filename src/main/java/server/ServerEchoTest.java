package server;

import configs.SocketConfig;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerEchoTest {
    public static void main(String[] args) {
        try (ServerSocket s = new ServerSocket(SocketConfig.PORT)) {

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
