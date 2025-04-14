package datas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class AuthData extends SerialData{
    static private byte SERIAL_ID = SerialData.SERIAL_ID++;
    public byte[] id;
    public String name;
    public int lobby_id = 0;

    public AuthData(byte[] id, String name, int lobby_id) {
        this.id = id;
        this.name = name;
        this.lobby_id = lobby_id;
    }

    public AuthData(InputStream stream) throws IOException {
        int size = decodeInt(stream.readNBytes(4)) - 10;
        id = stream.readNBytes(6);
        lobby_id = decodeInt(stream.readNBytes(4));
        name = new String(stream.readNBytes(size), StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize() {
        try {
            int size = 10 + name.length();

            ByteArrayOutputStream array = new ByteArrayOutputStream();
            array.write(convertInt(size));
            array.write(id);
            array.write(convertInt(lobby_id));
            array.write(name.getBytes(StandardCharsets.UTF_8));

            return array.toByteArray();
        } catch (IOException e) {
            return new byte[1];
        }
    }
}
