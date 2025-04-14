package datas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UserData extends SerialData {
    static private byte SERIAL_ID = SerialData.SERIAL_ID++;
    static private final byte USER_FULL = 1, USER_PARTIAL = 2;

    public String name;
    public long score;
    private byte type;
    public byte[] border_color;
    public byte[] body_color;
    public byte[] barrel_color;

    public UserData(InputStream stream) throws IOException {
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

    @Override
    public byte[] serialize() {
        ByteArrayOutputStream array = new ByteArrayOutputStream();

        try {
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
