package server;

import configs.SocketConfig;
import datas.*;
import server.model.PlayerData;
import server.model.ServerEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    private final Thread TCP_thread;
    private final Thread UDP_thread;
    private final Socket tcp_socket;
    private GameData current_data = new GameData();
    private final Lobby lobby;
    private LobbyData lobby_context;

    public ClientHandler(Socket socket, Lobby lobby) {
        lobby_context = lobby.initialLobbyData();
        tcp_socket = socket;
        this.lobby = lobby;

        TCP_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TCPThread();
            }
        });
        UDP_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UDPOutputThread();
            }
        });
        TCP_thread.start();
    }

    private void TCPThread() {
        try {
            OutputStream stdin = tcp_socket.getOutputStream();
            InputStream stdout = tcp_socket.getInputStream();

            // Initial lobby data of leaderboard
            stdin.write(lobby_context.serialize());
            stdin.flush();

            UDP_thread.start();
            tcp_socket.setSoTimeout(100);

            while (lobby.running) {
                try {
                    int dataID = stdout.read();
                    switch (dataID) {
                        case InputData.SERIAL_ID -> {
                            lobby.spawn_queue.add(lobby.players_data.get(tcp_socket.getInetAddress()));
                        }
                        case UserData.SERIAL_ID ->  {
                            PlayerData player = lobby.players_data.get(tcp_socket.getInetAddress());
                            UserData data = new UserData(stdout);

                            // TODO: Propagate to db
                            player.barrel_color = data.barrel_color;
                            player.body_color = data.body_color;
                            player.border_color = data.border_color;
                            player.name = data.name;
                        }
                    }
                } catch (SocketTimeoutException ignored) {}
                // TODO: Improve sending lobby data
            }
        } catch (IOException ignored) {}

        try {
            tcp_socket.close();
        } catch (IOException ignored) {}
        UDP_thread.interrupt();
        PlayerData player = lobby.players_data.remove(tcp_socket.getInetAddress());
        lobby.entity_data.remove(player);

    }

    private void UDPOutputThread() {
        // TODO: Send data only that the player can see (data sent must not exceed 50 entities)
        // TODO: Sent empty packet to signify death

        DatagramPacket packet = new DatagramPacket(current_data.serialize(), 1, tcp_socket.getInetAddress(), SocketConfig.PORT);

        while (lobby.running) {
            current_data.entities.clear();

            // Todo: Change this scary shet (prone to exception by modified element while iterating)
            try {
                for (ServerEntity i : lobby.entity_data.values()) {
                    current_data.entities.add(i.getEntityData());
                }
            } catch (Exception e) {
                continue;
            }

            packet.setData(current_data.serialize());

            try {
                lobby.input_socket.send(packet);
            } catch (IOException ignored) {}

            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
