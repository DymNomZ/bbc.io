package server.model;

import datas.AuthData;
import datas.InputData;
import server.ClientHandler;
import server.Lobby;

import java.net.Socket;

import static configs.DimensionConfig.*;

public class PlayerData {
    private ClientHandler handler;
    public byte[] id;
    public String name;
    public int score;
    public boolean playing = false;
    private InputData inputs;

    public byte[] border_color;
    public byte[] body_color;
    public byte[] barrel_color;

    public PlayerData(AuthData auth, Socket client, Lobby lobby) {
        id = auth.id;
        name = auth.name;

        // get score by db & color
        border_color = DEFAULT_BORDER.clone();
        body_color = DEFAULT_BODY.clone();
        barrel_color = DEFAULT_BARREL.clone();
        score = 0;

        handler = new ClientHandler(client, lobby);
    }

    public synchronized void setInputs(InputData data) {
        inputs = data;
    }

    public synchronized InputData getInputs() {
        return inputs;
    }
}
