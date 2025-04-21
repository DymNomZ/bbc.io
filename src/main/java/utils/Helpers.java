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

    public static Color rgbBytesToColor(byte[] rgbBytes) {
        double red = (rgbBytes[0] & 0xFF) / 255.0;
        double green = (rgbBytes[1] & 0xFF) / 255.0;
        double blue = (rgbBytes[2] & 0xFF) / 255.0;
        return Color.color(red, green, blue);
    }
}
