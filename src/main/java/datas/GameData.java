package datas;

import java.util.List;

public class GameData extends SerialData {
    public List<EntityData> entities;

    @Override
    public byte[] serialize() {
        return new byte[0];
    }
}
