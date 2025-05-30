package server.model;

import datas.AuthData;
import datas.InputData;
import exceptions.BBCSQLError;
import server.ClientHandler;
import server.Lobby;
import server.SQLDatabase;
import utils.Logging;

import java.net.Socket;

import static configs.DimensionConfig.*;
import static utils.Helpers.rgbBytesToColor;

public class PlayerData {
    private ClientHandler handler;
    public SQLDatabase.SQLPlayer SQLPlayer;
    public int id;
    public String name;
    public int highest_score;
    public int score;
    private InputData inputs = new InputData();
    public long last_shoot = 0;

    public byte[] border_color;
    public byte[] body_color;
    public byte[] barrel_color;

    public PlayerEntity player_entity = null;
    private int last_upgradable_score = 0;

    public PlayerData(AuthData auth, int player_id, int lobby_id) throws BBCSQLError {
        id = player_id;
        name = auth.name;

        SQLPlayer = SQLDatabase.getPlayerData(auth, lobby_id);

        border_color = SQLPlayer.border_color;
        body_color = SQLPlayer.body_color;
        barrel_color = SQLPlayer.barrel_color;
        highest_score = SQLPlayer.highest_score;
        score = 0;
    }

    public int addScore(int score) {
        this.score += score;

        if (highest_score < this.score) {
            highest_score = this.score;
        }

        while (this.score >= last_upgradable_score + 50) {
            last_upgradable_score += 50;
            return 1;
        }
        return 0;
    }

    public void resetScore() {
        score = 0;
        last_upgradable_score = 0;
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
