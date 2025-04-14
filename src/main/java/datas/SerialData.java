package datas;

import java.nio.ByteBuffer;

public abstract class SerialData {
    static protected byte SERIAL_ID = 1;

    abstract public byte[] serialize();

    static protected byte[] convertInt(int val) {
        return ByteBuffer.allocate(4).putInt(val).array();
    }

    static protected int decodeInt(byte[] val) {
        return ByteBuffer.wrap(val).getInt();
    }

    static protected byte[] convertLong(long val) {
        return ByteBuffer.allocate(8).putLong(val).array();
    }

    static protected long decodeLong(byte[] val) {
        return ByteBuffer.wrap(val).getLong();
    }
}
