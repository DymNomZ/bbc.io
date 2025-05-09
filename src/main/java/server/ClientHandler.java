package server;

import configs.SocketConfig;
import datas.*;
import exceptions.BBCSQLError;
import server.game_structure.QuadTree;
import server.game_structure.RangeCircle;
import server.model.PlayerData;
import server.model.PlayerEntity;
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
    private final LobbyData lobby_context;
    private final UDPAddress UDPAddr;
    private boolean player_dead = true;
    private final int player_id;
    private int death_messages_idx = 0;

    public ClientHandler(Socket socket, UDPAddress UDPAddr, Lobby lobby, int player_id) {
        lobby_context = lobby.initialLobbyData();

        for (Iterator<UserData> it = ((LinkedList<UserData>) lobby_context.users).descendingIterator(); it.hasNext(); ) {
            UserData i = it.next();
            if (i.id == player_id) {
                it.remove();
                lobby_context.users.add(0, i);
                break;
            }
        }

        if (!ServerMain.DEBUG_WINDOW) {
            Logging.write(this, "Entered player #" + lobby_context.users.get(0).id);
        }

        this.player_id = player_id;
        tcp_socket = socket;
        this.lobby = lobby;
        this.UDPAddr = UDPAddr;

        TCP_thread = new Thread(this::TCPThread);
        UDP_thread = new Thread(this::UDPOutputThread);
        TCP_thread.start();
    }

    private void TCPThread() {
        try {
            OutputStream stdin = tcp_socket.getOutputStream();
            InputStream stdout = tcp_socket.getInputStream();

            LinkedList<UserData> players_in_packet = (LinkedList<UserData>) lobby_context.users;

            // Initial lobby data of leaderboard
            stdin.write(lobby_context.serialize());
            stdin.flush();

            UDP_thread.start();
            tcp_socket.setSoTimeout(100);

            while (lobby.running) {
                try {
                    int dataID = stdout.read();
                    switch (dataID) {
                        case EntityData.SERIAL_ID -> {
                            int upgradeID = stdout.read();
                            PlayerData player = lobby.players_data.get(UDPAddr);
                            ArrayList<ServerEntity> entities = lobby.entity_data.get(player);

                            if (entities != null) {
                                PlayerEntity player_entity = (PlayerEntity) entities.get(0);

                                if (upgradeID == EntityData.KILL_SELF) {
                                    player_entity.health = 0;
                                } else if (player_entity.stat_upgradable != 0) {
                                    switch (upgradeID) {
                                        case EntityData.UPGRADE_HEALTH -> {
                                            player_entity.maximum_health += 10;
                                            player_entity.health += 10;
                                        }
                                        case EntityData.UPGRADE_SPEED -> {
                                            player_entity.speed += 0.05;
                                        }
                                        case EntityData.UPGRADE_DAMAGE -> {
                                            player_entity.damage += 5;
                                        }
                                    }

                                    player_entity.stat_upgradable -= 1;
                                }
                            }
                        }
                        case InputData.SERIAL_ID -> {
                            if (player_dead) {
                                lobby.spawn_queue.add(lobby.players_data.get(UDPAddr));
                                player_dead = false;
                            }
                        }
                        case UserData.SERIAL_ID ->  {
                            PlayerData player = lobby.players_data.get(UDPAddr);
                            UserData data = new UserData(stdout);

                            if (!Objects.equals(player.name, data.name)) {
                                try {
                                    SQLDatabase.setName(player.SQLPlayer, data.name);
                                } catch (BBCSQLError e) {
                                    Logging.error(this, "Error saving player name");
                                }
                            }

                            player.SQLPlayer.barrel_color = data.barrel_color;
                            player.SQLPlayer.body_color = data.body_color;
                            player.SQLPlayer.border_color = data.border_color;

                            try {
                                SQLDatabase.setColors(player.SQLPlayer);
                                player.body_color = data.body_color;
                                player.border_color = data.border_color;
                                player.barrel_color = data.barrel_color;
                            } catch (BBCSQLError e) {
                                player.SQLPlayer.barrel_color = player.barrel_color;
                                player.SQLPlayer.body_color = player.body_color;
                                player.SQLPlayer.border_color = player.border_color;
                                Logging.error(this, "Error saving player colors");
                            }
                        }
                    }
                } catch (SocketTimeoutException ignored) {}

                ListIterator<UserData> context_iter = players_in_packet.listIterator(1);


                // NOTE: Data sent here does not take to account those who reconnected
                for (PlayerData player : lobby.players_data.values()) {
                    if (player.id == player_id) {
                        UserData current = players_in_packet.get(0);
                        current.score = player.score;
                        current.highest_score = player.highest_score;
                        // add name if name is changed when player is in lobby (currently we dont)

                        current.type = UserData.USER_PARTIAL;

                        if (!Arrays.equals(current.barrel_color, player.barrel_color)) {
                            current.type = UserData.USER_FULL;
                            current.barrel_color = player.barrel_color.clone();
                        }
                        if (!Arrays.equals(current.body_color, player.body_color)) {
                            current.type = UserData.USER_FULL;
                            current.body_color = player.body_color.clone();
                        }
                        if (!Arrays.equals(current.border_color, player.border_color)) {
                            current.type = UserData.USER_FULL;
                            current.border_color = player.border_color.clone();
                        }
                    } else {
                        UserData cur = context_iter.hasNext() ? context_iter.next() : null;

                        while (cur != null && cur.id < player.id) {
                            context_iter.remove();
                            cur = context_iter.hasNext() ? context_iter.next() : null;
                        }

                        if (cur == null) {
                            context_iter.add(new UserData(player));
                        } else {
                            cur.score = player.score;
                            cur.highest_score = player.highest_score;
                            cur.type = UserData.USER_PARTIAL;

                            if (!Arrays.equals(cur.barrel_color, player.barrel_color)) {
                                cur.type = UserData.USER_FULL;
                                cur.barrel_color = player.barrel_color.clone();
                            }
                            if (!Arrays.equals(cur.body_color, player.body_color)) {
                                cur.type = UserData.USER_FULL;
                                cur.body_color = player.body_color.clone();
                            }
                            if (!Arrays.equals(cur.border_color, player.border_color)) {
                                cur.type = UserData.USER_FULL;
                                cur.border_color = player.border_color.clone();
                            }
                        }
                    }
                }

                while (context_iter.hasNext()) {
                    context_iter.next();
                    context_iter.remove();
                }

                if (lobby.death_messages.size() > death_messages_idx) {
                    lobby_context.deathMessage = lobby.death_messages.get(death_messages_idx++);
                } else {
                    lobby_context.deathMessage = "";
                }

                stdin.write(lobby_context.serialize());
                stdin.flush();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (IOException ignored) {}

        Logging.write(this, "Player #" + player_id + " disconnected");

        try {
            tcp_socket.close();
        } catch (IOException ignored) {}
        UDP_thread.interrupt();
        PlayerData player = lobby.players_data.remove(UDPAddr);
        synchronized (lobby.entity_data) {
            lobby.entity_data.remove(player);
        }

        try {
            SQLDatabase.closePlayer(player.SQLPlayer, player.score, player.highest_score);
        } catch (BBCSQLError ignored) {}
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
                        if (i.player_id == player_id && i instanceof PlayerEntity) {
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
                    if (i.player_id == player_id && i instanceof PlayerEntity) {
                        old_player_entity = i;
                        current_data.entities.add(0, i.getEntityData());
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
