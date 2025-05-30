package datas;

import java.nio.ByteBuffer;

public abstract class SerialData {
    abstract public byte[] serialize();

    static public byte[] convertInt(int val) {
        return ByteBuffer.allocate(4).putInt(val).array();
    }

    static public int decodeInt(byte[] val) {
        return ByteBuffer.wrap(val).getInt();
    }

    static protected byte[] convertLong(long val) {
        return ByteBuffer.allocate(8).putLong(val).array();
    }

    static protected long decodeLong(byte[] val) {
        return ByteBuffer.wrap(val).getLong();
    }

    static protected byte[] convertDouble(double val) {
        return ByteBuffer.allocate(8).putDouble(val).array();
    }

    static protected double decodeDouble(byte[] val) {
        return ByteBuffer.wrap(val).getDouble();
    }
}
