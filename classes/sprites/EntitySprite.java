package classes.sprites;

import classes.Asset.Sprite.AnimatedSprite;
import classes.Asset.Sprite.Sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EntitySprite extends Sprite {
    public static final int DEFAULT_IDLE = 0;
    public static final int DEFAULT_MOVING_LEFT = 1;
    public static final int DEFAULT_MOVING_RIGHT = 2;

    protected boolean is_right = false;
    protected final ArrayList<AnimatedSprite> sprites = new ArrayList<>();
    protected int state = DEFAULT_IDLE;
    protected boolean isMoving;

    public EntitySprite(List<AnimatedSprite> sprites) {
        this.sprites.addAll(sprites);
    }

    public EntitySprite toLeft() {
        if (is_right) {
            sprites.get(DEFAULT_IDLE).invert();
            is_right = false;
        }
        return this;
    }

    public EntitySprite toRight() {
        if (!is_right) {
            sprites.get(DEFAULT_IDLE).invert();
            is_right = true;
        }
        return this;
    }

    public EntitySprite setMoving(boolean isMoving) {
        this.isMoving = isMoving;
        return this;
    }

    public AnimatedSprite get(int state) {
        return sprites.get(state);
    }

    @Override
    public EntitySprite cropSprite(int top, int right, int bottom, int left) {
        for (AnimatedSprite s : sprites) {
            s.cropSprite(top, right, bottom, left);
        }
        return this;
    }

    @Override
    public EntitySprite cropSpriteRelative(int top, int right, int bottom, int left) {
        for (AnimatedSprite s : sprites) {
            s.cropSpriteRelative(top, right, bottom, left);
        }
        return this;
    }

    @Override
    public EntitySprite invert() {
        for (AnimatedSprite s : sprites) {
            s.invert();
        }
        return this;
    }

    protected int getState() {
        if (isMoving) {
            if (state == DEFAULT_IDLE || (is_right && state == DEFAULT_MOVING_LEFT) || (!is_right && state == DEFAULT_MOVING_RIGHT)) {
                sprites.get(state).reset();
            }
            state = is_right ? DEFAULT_MOVING_RIGHT : DEFAULT_MOVING_LEFT;
        } else if (state != DEFAULT_IDLE) {
            sprites.get(state).reset();
            state = DEFAULT_IDLE;
        }

        return state;
    }

    @Override
    public BufferedImage getSprite() {
        return sprites.get(getState()).getSprite();
    }

    @Override
    public EntitySprite display(Graphics g, int x, int y, int width, int height) {
        sprites.get(getState()).display(g, x, y, width, height);
        return this;
    }
}
