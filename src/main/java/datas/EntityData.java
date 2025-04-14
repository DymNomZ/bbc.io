package datas;

public class EntityData extends SerialData {
    static private byte SERIAL_ID = SerialData.SERIAL_ID++;
    static private final int ENTITY_PLAYER = 1, ENTITY_PROJECTILE = 2;

    @Override
    public byte[] serialize() {
        return new byte[0];
    }
}
