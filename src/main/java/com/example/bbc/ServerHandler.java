package com.example.bbc;

import configs.SocketConfig;
import datas.*;
import exceptions.BBCServerNotConnected;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Arrays;

public class ServerHandler {
    private ServerDataListener<GameData> game_listener = null;
    private ServerDataListener<LobbyData> lobby_listener = null;
    private ServerListener disconnect_listener = null;
    private ServerListener death_listener = null;
    private ServerDataListener<LobbyData> connect_listener = null;
    private ServerListener error_listener = null;
    private Thread tcp_thread;
    private DatagramSocket UDP_socket;
    private DatagramPacket input_packet = null;
    private OutputStream server_stdin = null;
    private String name = "";
    private int lobby_id = 0;
    private boolean is_connected = false;
    private UserData current_user = null;

    public ServerHandler(String name) {

        this.name = name;

        try {
            UDP_socket = new DatagramSocket(SocketConfig.PORT);
        } catch (SocketException e) {
            Logging.error(this, "Unable to bind UDP socket. Is a program with UDP socket using port " + SocketConfig.PORT + "?");
            throw new RuntimeException(e);
        }
        tcp_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TCPThread();
            }
        });
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
                    invokeListener(death_listener);
                } else {
                    invokeDataListener(game_listener, data);
                }
            }
        } catch (IOException e) {
            // This is scary. But for now
            UDP_socket.close();
        }
    }

    private void UDPInputThread() {
        while (is_connected) {
            try {
                UDP_socket.send(input_packet);
            } catch (IOException ignored) {}
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
                if (stdout.read() != 255) {
                    throw new IOException();
                }

                AuthData authPacket = new AuthData(id, name, lobby_id);

                server_stdin.write(authPacket.serialize());
                server_stdin.flush();

                stdout.read(); // ignore SerialData Type
                LobbyData lobbyData = new LobbyData(stdout);

                current_user = lobbyData.users.getFirst();

                is_connected = true;
                lobby_id = lobbyData.id;
                invokeDataListener(connect_listener, lobbyData);

                input_packet = new DatagramPacket(new byte[9], 9, server.getInetAddress(), SocketConfig.PORT);

                Thread udp_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UDPOutputThread();
                    }
                });
                udp_thread.setDaemon(true);
                udp_thread.start();

                udp_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UDPInputThread();
                    }
                });
                udp_thread.setDaemon(true);
                udp_thread.start();

                while (server.isConnected()) {
                    try {
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
    public void onGameUpdate(ServerDataListener<GameData> listener) {
        game_listener = listener;
    }

    // Data of leaderboard, Death messages, and more to add on lobby (TCP: treat data here to be stable)
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
    
    public void sendUserInput(InputData data) {
        input_packet.setData(data.serialize());
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
