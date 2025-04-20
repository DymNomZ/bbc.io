package datas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LobbyData extends SerialData {
    static private final byte SERIAL_ID = 5;
    public int id;
    public List<UserData> users = new ArrayList<>();

    public String deathMessage  = "";
    // public <UserID, String> chat;

    public LobbyData() {}

    public LobbyData(InputStream stream) throws IOException {
        id = decodeInt(stream.readNBytes(4));
        int size = decodeInt(stream.readNBytes(4));
        while (size-- != 0) {
            users.add(new UserData(stream));
        }
        size = decodeInt(stream.readNBytes(4));
        deathMessage = new String(stream.readNBytes(size), StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize() {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        array.write(SERIAL_ID);
        try {
            array.write(convertInt(id));
            array.write(convertInt(users.size()));
            for (UserData i : users) {
                array.write(i.serialize());
            }
            array.write(convertInt(deathMessage.length()));
            array.write(deathMessage.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            return new byte[1];
        }

        return array.toByteArray();
    }
}
