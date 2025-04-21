package interfaces;

public interface Collidable<T>{
    public boolean isCollidingWith(T other);
    public void handleCollision(T other);

}
