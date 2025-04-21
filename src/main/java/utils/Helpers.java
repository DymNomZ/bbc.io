package utils;

import javafx.scene.paint.Color;

public class Helpers {

    public static byte doubleToByte(double value) {
        int intVal = (int) Math.floor(value * 255);
        return (byte) intVal;
    }

    public static byte[] colorToRGBBytes(Color color) {
        return new byte[] {
                doubleToByte(color.getRed()),
                doubleToByte(color.getGreen()),
                doubleToByte(color.getBlue())
        };
    }
}
