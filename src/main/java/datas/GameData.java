package datas;

import java.util.List;

public class GameData extends SerialData {
    static private byte SERIAL_ID = SerialData.SERIAL_ID++;
    public List<EntityData> entities;

    @Override
    public byte[] serialize() {
        return new byte[0];
    }
}
