package datas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EntityData extends SerialData {
    static public final byte SERIAL_ID = 2;
    static public final byte UPGRADE_HEALTH = 1;
    static public final byte UPGRADE_SPEED = 2;
    static public final byte UPGRADE_DAMAGE = 3;

    public int id;
    public boolean is_projectile;
    public double x, y, angle;
    public int stat_upgradable;
    public int maximum_health;
    public int health;
    public double speed;
    public int damage;
    public long time_damaged;

    // Player Data
    public EntityData(
            int id,
            double x,
            double y,
            double angle,
            int stat_upgradable,
            int maximum_health,
            int health,
            double speed,
            int damage,
            long time_damaged
    ) {
        this.id = id;
        this.is_projectile = false;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.stat_upgradable = stat_upgradable;
        this.maximum_health = maximum_health;
        this.health = health;
        this.speed = speed;
        this.damage = damage;
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
            stat_upgradable = decodeInt(stream.readNBytes(4));
            maximum_health = decodeInt(stream.readNBytes(4));
            health = decodeInt(stream.readNBytes(4));
            speed = decodeDouble(stream.readNBytes(8));
            damage = decodeInt(stream.readNBytes(4));
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
                stream.write(convertInt(stat_upgradable));
                stream.write(convertInt(maximum_health));
                stream.write(convertInt(health));
                stream.write(convertDouble(speed));
                stream.write(convertInt(damage));
                stream.write(convertLong(time_damaged));
            }
        } catch (IOException e) {
            return new byte[1];
        }
        return stream.toByteArray();
    }
}
