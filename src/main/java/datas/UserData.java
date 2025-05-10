package datas;

import server.model.PlayerData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UserData extends SerialData {
    static public final byte SERIAL_ID = 6;
    static public final byte USER_FULL = 1, USER_PARTIAL = 2;

    public int id;
    public String name;
    public int highest_score;
    public int score;
    public byte type;
    public byte[] border_color;
    public byte[] body_color;
    public byte[] barrel_color;

    public UserData(InputStream stream) throws IOException {
        id = decodeInt(stream.readNBytes(4));
        highest_score = decodeInt(stream.readNBytes(4));
        score = decodeInt(stream.readNBytes(4));
        int size = decodeInt(stream.readNBytes(4));
        name = new String(stream.readNBytes(size), StandardCharsets.UTF_16);
        type = (byte) stream.read();

        if (type == USER_FULL) {
            border_color = stream.readNBytes(3);
            body_color = stream.readNBytes(3);
            barrel_color = stream.readNBytes(3);
        }
    }

    public UserData(PlayerData player) {
        id = player.id;
        name = player.name;
        highest_score = player.highest_score;
        score = player.score;

        type = USER_FULL;
        body_color = player.body_color.clone();
        border_color = player.border_color.clone();
        barrel_color = player.barrel_color.clone();
    }

    @Deprecated
    public UserData(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
        type = USER_PARTIAL;
    }

    @Deprecated
    public UserData(int id, String name, int score, byte[] border_color, byte[] body_color, byte[] barrel_color) {
        this.id = id;
        this.name = name;
        this.score = score;
        type = USER_FULL;
        this.border_color = border_color;
        this.body_color = body_color;
        this.barrel_color = barrel_color;
    }

    @Override
    public byte[] serialize() {
        ByteArrayOutputStream array = new ByteArrayOutputStream();

        try {
            array.write(convertInt(id));
            array.write(convertInt(highest_score));
            array.write(convertInt(score));

            byte[] nameBytes = name.getBytes(StandardCharsets.UTF_16);

            array.write(convertInt(nameBytes.length));
            array.write(nameBytes);
            array.write(type);

            if (type == USER_FULL) {
                array.write(border_color, 0, 3);
                array.write(body_color, 0, 3);
                array.write(barrel_color, 0, 3);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return array.toByteArray();
    }
}
