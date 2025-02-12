package classes.Asset.Sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

class NormalSprite extends Sprite {
    private BufferedImage sprite_buffer;
    private final NormalSprite raw_buffer;
    private boolean is_inverted = false;
    private int left = 0, top = 0;

    NormalSprite(BufferedImage buffer) {
        sprite_buffer = buffer;
        raw_buffer = null;
    }

    NormalSprite(Sprite buffer) {
        raw_buffer = (NormalSprite) buffer;
        sprite_buffer = raw_buffer.sprite_buffer;
    }

    @Override
    public Sprite cropSprite(int top, int right, int bottom, int left) {
        int spriteWidth = Math.max(1, raw_buffer.sprite_buffer.getWidth() - right - left);
        int spriteHeight = Math.max(1, raw_buffer.sprite_buffer.getHeight() - bottom - top);

        this.left = left;
        this.top = top;

        sprite_buffer = raw_buffer.sprite_buffer.getSubimage(left, top, spriteWidth, spriteHeight);
        return this;
    }

    @Override
    public Sprite cropSpriteRelative(int top, int right, int bottom, int left) {
        int spriteWidth = Math.max(1, sprite_buffer.getWidth() - right - left);
        int spriteHeight = Math.max(1, sprite_buffer.getHeight() - bottom - top);

        this.left += left;
        this.top += top;

        sprite_buffer = raw_buffer.sprite_buffer.getSubimage(this.left, this.top, spriteWidth, spriteHeight);
        return this;
    }

    @Override
    public Sprite invert() {
        is_inverted = !is_inverted;
        return this;
    }

    @Override
    public BufferedImage getSprite() {
        return sprite_buffer;
    }

    @Override
    public Sprite display(Graphics g, int x, int y, int width, int height) {
        if (is_inverted) {
            g.drawImage(sprite_buffer, x, y, x + width, y + height, sprite_buffer.getWidth(), 0, 0, sprite_buffer.getHeight(), null);
        } else {
            g.drawImage(sprite_buffer, x, y, x + width, y + height, 0, 0, sprite_buffer.getWidth(), sprite_buffer.getHeight(), null);
        }
        return this;
    }
}
