package classes.Asset.Sprite;

import classes.Asset.Asset;

import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static src.Utils.showError;

public abstract class Sprite extends Asset {
    //bytes of void image
    private static final byte[] void_image = {
            -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 32,
            0, 0, 0, 32, 8, 6, 0, 0, 0, 115, 122, 122, -12, 0, 0, 0, 1, 115, 82, 71, 66,
            0, -82, -50, 28, -23, 0, 0, 0, 65, 73, 68, 65, 84, 88, 71, -19, -41, 49, 17,
            0, 48, 8, 4, -63, -96, 26, 13, -88, 78, -122, 2, 13, -92, 88, 12, -4, -51, 118,
            68, 102, -34, -77, 120, -47, 1, 85, 21, 27, 13, -67, 45, -128, 0, 1, 2, 4, 8, 16,
            32, 64, -128, 0, 1, 2, 4, 8, -4, 33, -80, -15, 25, -49, -26, 3, -65, 3, -60, -95,
            -83, 118, -11, -39, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126
    };

    public static Sprite load(String fileName) {
        Sprite ret = (Sprite) Asset.load(fileName);

        if (ret != null) return new NormalSprite(ret);

        try{
            if (Objects.equals(fileName, "default")) {
                ret = new NormalSprite(ImageIO.read(new ByteArrayInputStream(void_image)));
            } else {
                InputStream fp = Sprite.class.getResourceAsStream("../../../assets/" + fileName);
                if (fp == null) throw new IOException();
                ret = new NormalSprite(ImageIO.read(fp));
            }
            Asset.add(fileName, ret);
        }catch(IOException e){
            showError("[SPRITE LOADER] Asset not found at assets/" + fileName);
            ret = load("default");
        }

        return new NormalSprite(ret);
    }

    // Crop sprite base on the non-cropped image. Cropping inwards
    public abstract Sprite cropSprite(int top, int right, int bottom, int left);
    // Crop sprite base on the cropped image. Cropping inwards
    public abstract Sprite cropSpriteRelative(int top, int right, int bottom, int left);

    // Do not use this to draw image to Graphics, use display instead.
    // This will return the cropped BufferImage (it will not return the inverted if the Sprite is set inverted)
    public abstract BufferedImage getSprite();
    public abstract Sprite invert();
    public abstract Sprite display(Graphics g, int x, int y, int width, int height);
}
