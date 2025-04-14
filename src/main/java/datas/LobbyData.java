package datas;

import java.util.ArrayList;
import java.util.List;

public class LobbyData extends SerialData {
    static private byte SERIAL_ID = SerialData.SERIAL_ID++;
    public int id;
    public List<UserData> users;
    public boolean lobbyStarted;

    public String deathMessage;
    // public <UserID, String> chat;

    @Override
    public byte[] serialize() {
        return new byte[0];
    }
}
