package server;

import datas.GameData;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    List<Thread> threads = new ArrayList<>();
    Socket tcp_socket;
    GameData current_data = new GameData();

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
