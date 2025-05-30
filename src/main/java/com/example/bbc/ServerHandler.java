package com.example.bbc;

import configs.SocketConfig;
import datas.*;
import exceptions.BBCServerNotConnected;
import server.Lobby;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ServerHandler {
    private static final int SERVER_ACK = 255;

    private ServerDataListener<GameData> game_listener = null;
    private ServerDataListener<LobbyData> lobby_listener = null;
    private ServerListener disconnect_listener = null;
    private ServerListener death_listener = null;
    private ServerDataListener<LobbyData> connect_listener = null;
    private ServerListener error_listener = null;
    private ServerListener has_upgrade_listener = null;
    private ServerListener no_upgrade_listener = null;

    private Thread tcp_thread;
    private DatagramSocket UDP_socket;
    private DatagramPacket input_packet = null;
    private OutputStream server_stdin = null;
    private String name;
    private int lobby_id = 0;
    private boolean is_connected = false;
    private UserData current_user = null;

    public List<UserData> users_in_lobby = new LinkedList<>();

    private boolean player_dead = true;
    private boolean no_stat_upgrades = true;

    public ServerHandler() {
        this.name = "Garfield";
    }

    public void connect(String name) {
        this.name = name;

        try {
            UDP_socket = new DatagramSocket();
        } catch (SocketException e) {
            Logging.error(this, "Unable to bind UDP socket. Is a program with UDP socket using port " + SocketConfig.PORT + "?");
            throw new RuntimeException(e);
        }
        tcp_thread = new Thread(this::TCPThread);
        tcp_thread.setDaemon(true);
        tcp_thread.start();
    }

    private void UDPOutputThread() {
        byte[] buf = new byte[1550];
        DatagramPacket packet = new DatagramPacket(buf, 1550);

        try {
            UDP_socket.setSoTimeout(0);

            while (is_connected) {
                UDP_socket.receive(packet);
                GameData data = new GameData(packet.getData());
                if (data.entities.isEmpty()) {
                    if (!player_dead) {
                        invokeListener(death_listener);
                        player_dead = true;
                        no_stat_upgrades = true;
                    }
                } else {
                    player_dead = false;
                    invokeDataListener(game_listener, data);

                    int upgrades = data.entities.get(0).stat_upgradable;

                    if (no_stat_upgrades && upgrades != 0) {
                        no_stat_upgrades = false;
                        invokeListener(has_upgrade_listener);
                    } else if (!no_stat_upgrades && upgrades == 0) {
                        no_stat_upgrades = true;
                        invokeListener(no_upgrade_listener);
                    }
                }
            }
        } catch (IOException e) {
            // This is scary. But for now
            Logging.error(this, "UDP Output Thread arrested by ICC.");
            UDP_socket.close();
        }
    }

    private void UDPInputThread() {
        byte[] encoded_id = SerialData.convertInt(lobby_id);
        DatagramPacket packet;

        try {
            packet = new DatagramPacket(encoded_id, 4, InetAddress.getByName(SocketConfig.HOSTNAME), SocketConfig.PORT);
        } catch (UnknownHostException e) {
            Logging.error(this, "Unknown host name: " + SocketConfig.HOSTNAME);
            return;
        }

        while (input_packet == null) {
            try {
                UDP_socket.send(packet);
            } catch (IOException ignored) {}
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return;
            }
        }

        while (is_connected) {
            try {
                UDP_socket.send(input_packet);
            } catch (IOException ignored) {}
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private void TCPThread() {
        byte[] id = null;

        try {
            for (NetworkInterface i : NetworkInterface.networkInterfaces().toList()) {
                id = i.getHardwareAddress();
                if (id != null) {
                    break;
                }
            }

            if (id == null) {
                Logging.error(this, "No Network interface from this device have a mac address / is connected to the internet");
                if (error_listener != null) {
                    error_listener.run();
                }
                return;
            }
        } catch (SocketException e) {
            throw new RuntimeException("Cant get Device Mac Address as ID");
        }

        while (true) {
            try (Socket server = new Socket(SocketConfig.HOSTNAME, SocketConfig.PORT)) {
                server.setSoTimeout(30000);
                InputStream stdout = server.getInputStream();
                server_stdin = server.getOutputStream();

                server_stdin.write(SocketConfig.KEY);
                server_stdin.flush();
                if (stdout.read() != SERVER_ACK) {
                    throw new IOException();
                }

                AuthData authPacket = new AuthData(id, name, lobby_id);

                server_stdin.write(authPacket.serialize());
                server_stdin.flush();

                lobby_id = SerialData.decodeInt(stdout.readNBytes(4));

                Thread udp_thread = new Thread(this::UDPInputThread);
                udp_thread.setDaemon(true);
                udp_thread.start();

                server.setSoTimeout(5000);
                try {
                    if (stdout.read() != SERVER_ACK) {
                        throw new IOException();
                    }
                } catch (IOException e) {
                    udp_thread.interrupt();
                    throw new IOException();
                }

                server.setSoTimeout(30000);

                stdout.read(); // ignore SerialData Type
                LobbyData lobbyData = new LobbyData(stdout);

                current_user = lobbyData.users.get(0);

                is_connected = true;
                input_packet = new DatagramPacket(new byte[9], 9, server.getInetAddress(), SocketConfig.PORT);
                lobby_id = lobbyData.id;
                invokeDataListener(connect_listener, lobbyData);

                udp_thread = new Thread(this::UDPOutputThread);
                udp_thread.setDaemon(true);
                udp_thread.start();

                while (server.isConnected()) {
                    try {
                        stdout.read(); // ignore SerialData Type
                        lobbyData = new LobbyData(stdout);
                        invokeDataListener(lobby_listener, lobbyData);
                    } catch (SocketTimeoutException ignored) {}
                }
            } catch (IOException e) {
                Logging.error(this, "Unable to connect to server. . . Retrying in 5 seconds");
            }

            if (is_connected) {
                invokeListener(disconnect_listener);
            }
            is_connected = false;
            input_packet = null;

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private <T extends SerialData> void invokeDataListener(ServerDataListener<T> listener, T data) {
        if (listener != null) {
            listener.run(data);
        }
    }

    private void invokeListener(ServerListener listener) {
        if (listener != null) {
            listener.run();
        }
    }

    public void onHandlerError(ServerListener listener) {
        error_listener = listener;
    }

    // Data of entities to apply to game
    // The first entity in List will always be you
    // NOTE: This will no longer update when user is dead (use onPlayerDeath listener)
    public void onGameUpdate(ServerDataListener<GameData> listener) {
        game_listener = listener;
    }

    // Data of leaderboard, Death messages, and more to add on lobby
    // Check UserData type variable (USER_FULL | USER_PARTIAL) if this User is new or this User modified his/her character customization
    // The first UserData will always be the current user
    public void onLobbyUpdate(ServerDataListener<LobbyData> listener) {
        lobby_listener = listener;
    }

    public void onDisconnected(ServerListener listener) {
        disconnect_listener = listener;
    }

    // The initial lobby data will be sent (scores, name and tank colors of all users including the current user)
    public void onConnected(ServerDataListener<LobbyData> listener) {
        connect_listener = listener;
    }

    public void onPlayerDeath(ServerListener listener) {
        death_listener = listener;
    }

    public void onContainsUpgrades(ServerListener listener) {
        has_upgrade_listener = listener;
    }

    public void onNoUpgrades(ServerListener listener) {
        no_upgrade_listener = listener;
    }
    
    public void sendUserInput(InputData data) {
        if (input_packet != null) {
            input_packet.setData(data.serialize());
        }
    }

    public void disconnect() {
        // TODO: disconnect server
    }

    public void play() {
        if (!is_connected) {
            throw new BBCServerNotConnected();
        }
        try {
            server_stdin.write(InputData.SERIAL_ID);
            server_stdin.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void upgradeHealth() {
        if (!is_connected) {
            throw new BBCServerNotConnected();
        }
        try {
            byte[] data = {EntityData.SERIAL_ID, EntityData.UPGRADE_HEALTH};

            server_stdin.write(data);
            server_stdin.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void upgradeSpeed() {
        if (!is_connected) {
            throw new BBCServerNotConnected();
        }
        try {
            byte[] data = {EntityData.SERIAL_ID, EntityData.UPGRADE_SPEED};

            server_stdin.write(data);
            server_stdin.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void upgradeDamage() {
        if (!is_connected) {
            throw new BBCServerNotConnected();
        }
        try {
            byte[] data = {EntityData.SERIAL_ID, EntityData.UPGRADE_DAMAGE};

            server_stdin.write(data);
            server_stdin.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void suicide() {
        if (!is_connected) {
            throw new BBCServerNotConnected();
        }
        try {
            byte[] data = {EntityData.SERIAL_ID, EntityData.KILL_SELF};

            server_stdin.write(data);
            server_stdin.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserData getUser() {
        if (!is_connected) {
            throw new BBCServerNotConnected();
        }
        return current_user;
    }

    public void modifyUser(UserData data) {
        if (!is_connected) {
            throw new BBCServerNotConnected();
        }
        current_user = data;
        try {
            server_stdin.write(UserData.SERIAL_ID);
            server_stdin.write(data.serialize());
            server_stdin.flush();
        } catch (IOException ignored) {}
    }
}
