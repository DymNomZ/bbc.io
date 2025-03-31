package entities;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;

public abstract class Entity {
    protected double x;
    protected double y;
    protected Group entityGroup;
    private Rotate rotation;

    public Entity() {
        entityGroup = new Group();
        rotation = new Rotate(0); // Initialize rotation
        entityGroup.getTransforms().add(rotation); // Apply rotation transform
    }

    public void render(Group root) {
        root.getChildren().add(entityGroup);
    }


    public void lookAt(double x, double y) {
        // Calculate angle to target (x, y)
        double angle = Math.toDegrees(Math.atan2(y - entityGroup.getLayoutY(), x - entityGroup.getLayoutX()));

        // Apply rotation
        rotation.setAngle(angle);
    }

    public double getX() {
        return entityGroup.getLayoutX();
    }

    public void setPosition(double x, double y) {
        entityGroup.setLayoutX(x);
        entityGroup.setLayoutY(y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return entityGroup.getLayoutY();
    }

    public void setY(double y) {
        this.y = y;
    }

    public Group getEntityGroup() {
        return entityGroup;
    }

    public void setEntityGroup(Group entityGroup) {
        this.entityGroup = entityGroup;
    }
}
