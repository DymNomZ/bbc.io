package server;

import datas.AuthData;
import server.model.PlayerData;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;

public class Lobby {
    private List<PlayerData> players_data;
    static private int ID = 1;
    static private final int MAX_PLAYERS = 20;
    private int id;

    public Lobby() {
        // setup of maps
        do {
            id = ID++;
        } while (id == 0);
    }

    private void gameThread() {
        // Game Algo
    }

    public boolean addPlayer(Socket client) throws IOException {
        if (players_data.size() < MAX_PLAYERS) {
            InputStream stream = client.getInputStream();
            AuthData authPacket = new AuthData(stream);

            // get data and extract
            PlayerData player = new PlayerData(authPacket);
            players_data.add(player);
        }

        return false;
    }
}
