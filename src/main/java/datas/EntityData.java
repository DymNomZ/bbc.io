package datas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EntityData extends SerialData {
    static private final byte SERIAL_ID = 2;
    public byte[] id;
    public boolean is_projectile;
    public double x, y, angle;

    // ID may be the player id or the owner id of the projectile
    public EntityData(byte[] id, double x, double y, double angle, boolean is_projectile) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.is_projectile = is_projectile;
    }

    public EntityData(InputStream stream) throws IOException {
        id = stream.readNBytes(6);
        is_projectile = stream.read() == 1;
        x = decodeDouble(stream.readNBytes(8));
        y = decodeDouble(stream.readNBytes(8));
        angle = decodeDouble(stream.readNBytes(8));
    }

    @Override
    public byte[] serialize() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(31);
        try {
            stream.write(id);
            stream.write(is_projectile ? 1 : 0);
            stream.write(convertDouble(x));
            stream.write(convertDouble(y));
            stream.write(convertDouble(angle));
        } catch (IOException e) {
            return new byte[1];
        }
        return stream.toByteArray();
    }
}
