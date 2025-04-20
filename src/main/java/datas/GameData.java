package datas;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameData extends SerialData {
    static private final byte SERIAL_ID = 3;
    public List<EntityData> entities;

    public GameData() {
        entities = new ArrayList<>(0);
    }

    public GameData(byte[] data) throws IOException {
        entities = new ArrayList<>();
        ByteArrayInputStream stream =  new ByteArrayInputStream(data);

        int size = stream.read();
        while (size-- != 0) {
            entities.add(new EntityData(stream));
        }
    }

    @Override
    public byte[] serialize() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        stream.write(entities.size());

        try {
            for (EntityData i : entities) {
                stream.write(i.serialize());
            }
        } catch (IOException e) {
            byte[] buf = new byte[1];
            buf[0] = 0;
            return buf;
        }


        return stream.toByteArray();
    }
}
