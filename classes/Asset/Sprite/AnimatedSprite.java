package classes.Asset.Sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AnimatedSprite extends Sprite {
    private final List<Sprite> sprites;
    private long interval;
    private long start_nano = 0;

    public AnimatedSprite(long nanoInterval, ArrayList<Sprite> sprites) {
        interval = nanoInterval;

        for (Sprite s : sprites) {
            if (!(s instanceof NormalSprite)) {
                throw new UnsupportedOperationException("Only Sprites created by Sprite.load(filename) are allowed...");
            }
        }

        this.sprites = sprites;
    }

    public AnimatedSprite(long nanoInterval, String[] filePaths) {
        sprites = new ArrayList<>();

        for (String s : filePaths) {
            sprites.add(Sprite.load(s));
        }

        interval = nanoInterval;
    }

    public AnimatedSprite setInterval(long interval) {
        this.interval = Math.max(interval, 0);
        return this;
    }

    public Sprite get(int index) {
        return sprites.get(index);
    }

    public AnimatedSprite reset() {
        start_nano = 0;
        return this;
    }

    @Override
    public AnimatedSprite cropSprite(int top, int right, int bottom, int left) {
        for (Sprite s : sprites) {
            s.cropSprite(top, right, bottom, left);
        }
        return this;
    }

    @Override
    public AnimatedSprite cropSpriteRelative(int top, int right, int bottom, int left) {
        for (Sprite s : sprites) {
            s.cropSpriteRelative(top, right, bottom, left);
        }
        return this;
    }

    @Override
    public AnimatedSprite invert() {
        for (Sprite s : sprites) {
            s.invert();
        }
        return this;
    }

    private int nextIndex() {
        if (start_nano == 0) start_nano = System.nanoTime();

        long index = Math.max(System.nanoTime() - start_nano, 0);
        index /= interval;

        return (int)(index % sprites.size());
    }

    @Override
    public BufferedImage getSprite() {
        return sprites.get(nextIndex()).getSprite();
    }

    @Override
    public AnimatedSprite display(Graphics g, int x, int y, int width, int height) {
        sprites.get(nextIndex()).display(g, x, y, width, height);
        return this;
    }
}
