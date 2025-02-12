package interfaces;

// Callbacks called by verifyEntityCollision
// These callbacks are specifically for solid tile collisions
public interface CollisionHandler {
    default void onCollision() {}

    default void onTopCollision() {}
    default void onLeftCollision() {}
    default void onBottomCollision() {}
    default void onRightCollision() {}
}