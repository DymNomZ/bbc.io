package GameData;

import interfaces.Collidable;

public abstract class GameObject implements Collidable<GameObject> {
    double x,y;
    boolean collided;
    abstract boolean isInsideRangeCircle(RangeCircle circle);
}
