package com.example.bbc;

import datas.GameData;
import datas.InputData;
import datas.LobbyData;
import datas.UserData;

public class ServerHandler {
    private ServerDataListener<GameData> game_listener = null;
    private ServerDataListener<LobbyData> lobby_listener = null;
    private ServerListener disconnect_listener = null;
    private ServerListener connect_listener = null;

    public ServerHandler() {
        // start connection
    }

    private void UDPOutputThread() {

    }

    private void UDPInputThread() {

    }

    private void TCPThread() {

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
