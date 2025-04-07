package com.example.bbc;

import configs.SocketConfig;
import datas.GameData;
import datas.InputData;
import datas.LobbyData;
import datas.UserData;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class ServerHandler {
    private ServerDataListener<GameData> game_listener = null;
    private ServerDataListener<LobbyData> lobby_listener = null;
    private ServerListener disconnect_listener = null;
    private ServerListener connect_listener = null;
    private Thread tcp_thread;

    public ServerHandler() {
        //UUID user_id = UUID.randomUUID();

        tcp_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TCPThread();
            }
        });
    }

    private void UDPOutputThread() {

    }

    private void UDPInputThread() {

    }

    private void TCPThread() {
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

                // handle client here
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

    public void onConnect(ServerListener listener) {
        connect_listener = listener;
    }
    
    public void sendUserInput(InputData data) {
        // send data (non-blocking)
    }

    public void sendUserData(UserData data) {
        // send user data (name or character modification)
    }

    public void ready() {

    }

    public void unready() {

    }
}
