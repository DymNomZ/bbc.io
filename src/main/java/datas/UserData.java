package datas;

import server.model.PlayerData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UserData extends SerialData {
    static public final byte SERIAL_ID = 6;
    static private final byte USER_FULL = 1, USER_PARTIAL = 2;

    public byte[] id;
    public String name;
    public long score;
    private byte type;
    public byte[] border_color;
    public byte[] body_color;
    public byte[] barrel_color;

    public UserData(InputStream stream) throws IOException {
        id = stream.readNBytes(6);
        score = decodeLong(stream.readNBytes(8));
        int size = decodeInt(stream.readNBytes(4));
        name = new String(stream.readNBytes(size), StandardCharsets.UTF_8);
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
        score = player.score;

        type = USER_FULL;
        body_color = player.body_color.clone();
        border_color = player.border_color.clone();
        barrel_color = player.barrel_color.clone();
    }

    public UserData(byte[] id, String name, long score) {
        this.id = id;
        this.name = name;
        this.score = score;
        type = USER_PARTIAL;
    }

    public UserData(byte[] id, String name, long score, byte[] border_color, byte[] body_color, byte[] barrel_color) {
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
            array.write(id);
            array.write(convertLong(score));
            array.write(convertInt(name.length()));
            array.write(name.getBytes(StandardCharsets.UTF_8));
            array.write(type);

            if (type == USER_FULL) {
                array.write(border_color);
                array.write(body_color);
                array.write(barrel_color);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return array.toByteArray();
    }
}
