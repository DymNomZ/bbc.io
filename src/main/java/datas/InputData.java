package datas;

import java.util.Arrays;

public class InputData extends SerialData {
    static public final byte SERIAL_ID = 4;

    public boolean up_pressed = false;
    public boolean down_pressed = false;
    public boolean left_pressed = false;
    public boolean right_pressed = false;
    public boolean lClick_pressed = false;
    public boolean lShift_pressed = false;
    public double angle = 0.00;

    public InputData() {}

    public InputData(byte[] data) {
        byte inputs = data[0];

        up_pressed = (inputs & 0x01) == 1;
        down_pressed = (inputs & 0x02) == 2;
        left_pressed = (inputs & 0x04) == 4;
        right_pressed = (inputs & 0x08) == 8;
        lShift_pressed = (inputs & 0x10) == 16;
        lClick_pressed = (inputs & 0x20) == 32;

        angle = decodeDouble(Arrays.copyOfRange(data, 1, 9));
    }

    @Override
    public byte[] serialize() {
        byte[] data = new byte[9];

        data[0] = 0;
        data[0] |= (byte) (up_pressed ? 0x01 : 0);
        data[0] |= (byte) (down_pressed ? 0x02 : 0);
        data[0] |= (byte) (left_pressed ? 0x04 : 0);
        data[0] |= (byte) (right_pressed ? 0x08 : 0);
        data[0] |= (byte) (lShift_pressed ? 0x10 : 0);
        data[0] |= (byte) (lClick_pressed ? 0x20 : 0);

        byte[] angle = convertDouble(this.angle);
        for (int i = 0; i < 8; i++) {
            data[i + 1] = angle[i];
        }

        return data;
    }
}
