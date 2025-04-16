package server;

import datas.GameData;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    List<Thread> threads = new ArrayList<>();
    Socket tcp_socket;
    GameData current_data = new GameData();
    private Lobby lobby;

    public ClientHandler(Socket socket, Lobby lobby) {
        // add initial lobby data
        tcp_socket = socket;
        this.lobby = lobby;
    }

    private void TCPThread() {

    }

    private void UDPOutputThread() {

    }
}
