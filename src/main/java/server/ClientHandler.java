package server;

import configs.SocketConfig;
import datas.*;
import server.game_structure.QuadTree;
import server.game_structure.RangeCircle;
import server.model.PlayerData;
import server.model.ServerEntity;
import server.model.UDPAddress;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

import static utils.Helpers.rgbBytesToColor;

public class ClientHandler {
    private final Thread TCP_thread;
    private final Thread UDP_thread;
    private final Socket tcp_socket;
    private GameData current_data = new GameData();
    private final Lobby lobby;
    private LobbyData lobby_context;
    private final UDPAddress UDPAddr;
    private boolean player_dead = true;
    private final int player_id;

    public ClientHandler(Socket socket, UDPAddress UDPAddr, Lobby lobby, int player_id) {
        lobby_context = lobby.initialLobbyData();

        for (Iterator<UserData> it = ((LinkedList<UserData>) lobby_context.users).descendingIterator(); it.hasNext(); ) {
            UserData i = it.next();
            if (i.id == player_id) {
                it.remove();
                lobby_context.users.addFirst(i);
                break;
            }
        }

        if (!ServerMain.DEBUG_WINDOW) {
            Logging.write(this, "Entered player #" + lobby_context.users.getFirst().id);
        }

        this.player_id = player_id;
        tcp_socket = socket;
        this.lobby = lobby;
        this.UDPAddr = UDPAddr;

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
                            lobby.spawn_queue.add(lobby.players_data.get(UDPAddr));
                            player_dead = false;
                        }
                        case UserData.SERIAL_ID ->  {
                            PlayerData player = lobby.players_data.get(UDPAddr);
                            UserData data = new UserData(stdout);

                            // TODO: Propagate to db
                            player.barrel_color = data.barrel_color;
                            player.body_color = data.body_color;
                            player.border_color = data.border_color;
                            player.name = data.name;
                        }
                    }
                } catch (SocketTimeoutException ignored) {}
                // TODO: send lobby data
                stdin.write(lobby_context.serialize());
                stdin.flush();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new IOException();
                }
            }
        } catch (IOException ignored) {}

        Logging.write(this, "Player #" + player_id + " disconnected");

        try {
            tcp_socket.close();
        } catch (IOException ignored) {}
        UDP_thread.interrupt();
        PlayerData player = lobby.players_data.remove(UDPAddr);
        lobby.entity_data.remove(player);

    }

    private void UDPOutputThread() {
        DatagramPacket packet = new DatagramPacket(current_data.serialize(), 1, UDPAddr.ip, UDPAddr.port);

        // save the Last player entity
        ServerEntity old_player_entity = null;
        boolean player_found;

        while (lobby.running) {
            current_data.entities.clear();

            if (!player_dead) {
                QuadTree tree = lobby.qtree;

                if (old_player_entity == null) {
                    for (ServerEntity i : tree.root_entities) {
                        if (i.player_id == player_id) {
                            old_player_entity = i;
                            break;
                        }
                    }

                    if (old_player_entity == null) {
                        continue;
                    }
                }

                List<ServerEntity> in_range = tree.query(new RangeCircle(old_player_entity.x, old_player_entity.y, old_player_entity.radius + 1500));
                //Logging.write(this,in_range.size()+" " + old_player_entity);
                player_found = false;
                for (ServerEntity i : in_range) {
                    if (i.player_id == player_id) {
                        old_player_entity = i;
                        current_data.entities.addFirst(i.getEntityData());
                        player_found = true;
                        continue;
                    }
                    current_data.entities.add(i.getEntityData());
                }

                if (!player_found) {
                    player_dead = true;
                    old_player_entity = null;
                    current_data.entities.clear();
                }
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
