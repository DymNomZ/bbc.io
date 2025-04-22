package datas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EntityData extends SerialData {
    static private final byte SERIAL_ID = 2;
    public int id;
    public boolean is_projectile;
    public double x, y, angle;
    public int health;
    public long time_damaged;

    // Player Data
    public EntityData(
            int id,
            double x,
            double y,
            double angle,
            int health,
            long time_damaged
    ) {
        this.id = id;
        this.is_projectile = false;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.health = health;
        this.time_damaged = time_damaged;
    }

    // Projectile Data
    public EntityData(int id, double x, double y, double angle) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.is_projectile = true;
    }

    public EntityData(InputStream stream) throws IOException {
        id = decodeInt(stream.readNBytes(4));
        is_projectile = stream.read() == 1;
        x = decodeDouble(stream.readNBytes(8));
        y = decodeDouble(stream.readNBytes(8));
        angle = decodeDouble(stream.readNBytes(8));
        if (!is_projectile) {
            health = decodeInt(stream.readNBytes(4));
            time_damaged = decodeLong(stream.readNBytes(8));
        }
    }

    @Override
    public byte[] serialize() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(43);
        try {
            stream.write(convertInt(id));
            stream.write(is_projectile ? 1 : 0);
            stream.write(convertDouble(x));
            stream.write(convertDouble(y));
            stream.write(convertDouble(angle));
            if (!is_projectile) {
                stream.write(convertInt(health));
                stream.write(convertLong(time_damaged));
            }
        } catch (IOException e) {
            return new byte[1];
        }
        return stream.toByteArray();
    }
}
