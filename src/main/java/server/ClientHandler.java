package server;

import datas.GameData;
import datas.LobbyData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    private List<Thread> threads = new ArrayList<>();
    private final Socket tcp_socket;
    private GameData current_data = new GameData();
    private final Lobby lobby;
    private LobbyData lobby_context;

    public ClientHandler(Socket socket, Lobby lobby) {

        lobby_context = lobby.initialLobbyData();

        tcp_socket = socket;
        this.lobby = lobby;
    }

    private void TCPThread() {
        try {
            OutputStream stdin = tcp_socket.getOutputStream();
            InputStream stdout = tcp_socket.getInputStream();


        } catch (IOException e) {
            // handle disconnection
        }
    }

    private void UDPOutputThread() {

    }
}
