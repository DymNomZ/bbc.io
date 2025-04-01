package datas;

public class InputData extends SerialData {
    public boolean up_pressed = false;
    public boolean down_pressed = false;
    public boolean left_pressed = false;
    public boolean right_pressed = false;
    public boolean lShift_pressed = false;

    public boolean one_pressed = false;
    public boolean two_pressed = false;
    public boolean three_pressed = false;
    public boolean four_pressed = false;
    public boolean five_pressed = false;

    public boolean left_is_pressed = false;
    public boolean right_is_pressed = false;
    // angle of mouse

    @Override
    public byte[] serialize() {
        return new byte[0];
    }
}
