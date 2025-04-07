package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    List<Thread> threads = new ArrayList<>();
    Socket tcp_socket;

    public ClientHandler(Socket socket) {
        tcp_socket = socket;
    }

    private void TCPThread() {

    }

    private void UDPInputThread() {

    }

    private void UDPOutputThread() {

    }
}
