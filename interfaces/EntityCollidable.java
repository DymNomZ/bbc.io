package interfaces;

import classes.entities.PanelEntity;

public interface EntityCollidable{

	default void onEntityCollision(PanelEntity e){}

	default void onEntityTopCollision(PanelEntity e) {}
	default void onEntityLeftCollision(PanelEntity e) {}
	default void onEntityBottomCollision(PanelEntity e) {}
	default void onEntityRightCollision(PanelEntity e) {}
}
