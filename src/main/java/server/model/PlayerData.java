package server.model;

import datas.AuthData;
import datas.InputData;
import server.ClientHandler;
import server.Lobby;

import java.net.Socket;

import static configs.DimensionConfig.*;
import static utils.Helpers.rgbBytesToColor;

public class PlayerData {
    private ClientHandler handler;
    public int id;
    public String name;
    public int score;
    public boolean playing = false;
    private InputData inputs;

    public byte[] border_color;
    public byte[] body_color;
    public byte[] barrel_color;

    public PlayerData(AuthData auth, int player_id) {
        id = player_id;
        name = auth.name;

        // TODO: get score & color from db
        // Dummy data of user
        border_color = DEFAULT_COLOR_BORDER.clone();
        body_color = DEFAULT_COLOR_BODY.clone();
        barrel_color = DEFAULT_COLOR_BARREL.clone();
        score = 0;
    }

    public void startHandler(Socket client, UDPAddress UDPAddr, Lobby lobby) {
        handler = new ClientHandler(client, UDPAddr, lobby, id);
    }

    public synchronized void setInputs(InputData data) {
        inputs = data;
    }

    public synchronized InputData getInputs() {
        return inputs;
    }

    @Override
    public String toString() {
        return "PlayerData [id=" + id + ", name=" + name + ", score=" + score + ", color " +
                rgbBytesToColor(body_color) + " " + rgbBytesToColor(barrel_color) + " " + rgbBytesToColor(border_color);
    }
}
