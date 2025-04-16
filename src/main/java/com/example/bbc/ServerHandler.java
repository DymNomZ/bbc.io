package com.example.bbc;

import configs.SocketConfig;
import datas.*;
import exceptions.BBCServerNotConnected;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;

public class ServerHandler {
    private ServerDataListener<GameData> game_listener = null;
    private ServerDataListener<LobbyData> lobby_listener = null;
    private ServerListener disconnect_listener = null;
    private ServerDataListener<UserData> connect_listener = null;
    private ServerListener error_listener = null;
    private Thread tcp_thread;
    private String name = "";
    private int lobby_id = 0;
    private boolean isConnected = false;

    public ServerHandler() {
        tcp_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TCPThread();
            }
        });
        tcp_thread.start();
    }

    private void UDPOutputThread() {

    }

    private void UDPInputThread() {

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
                OutputStream stdin = server.getOutputStream();

                stdin.write(SocketConfig.KEY);
                stdin.flush();
                if (stdout.read() != 255) {
                    throw new IOException();
                }

                AuthData authPacket = new AuthData(id, name, lobby_id);

                stdin.write(authPacket.serialize());
                stdin.flush();


            } catch (IOException e) {
                Logging.error(this, "Unable to connect to server. . . Retrying in 5 seconds");
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return;
            }
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

    public void onConnected(ServerDataListener<UserData> listener) {
        connect_listener = listener;
    }
    
    public void sendUserInput(InputData data) {
        // send data (non-blocking)
    }

    public void sendUserData(UserData data) {
        // send user data (name or character modification)
    }

    public void disconnect() {

    }

    public void play() {
        if (!isConnected) {
            throw new BBCServerNotConnected();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        // set name to server if connected
    }
}
