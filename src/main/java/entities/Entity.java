package entities;

import com.example.bbc.GameScene;
import com.example.bbc.MouseHandler;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import utils.Scenes;

public abstract class Entity {

    private MouseHandler mouse_handler;
    protected double x;
    protected double y;
    protected Group entity_group;
    private Rotate rotation;

    public Entity() {
        entity_group = new Group();
        rotation = new Rotate(0); // Initialize rotation
        entity_group.getTransforms().add(rotation); // Apply rotation transform
        mouse_handler = GameScene.mouse_handler;
    }

    public void render(Group root) {
        root.getChildren().add(entity_group);
    }

    public void shoot(){
        double x = getX();
        double y = getY();
        double mouse_x = mouse_handler.event.getSceneX();
        double mouse_y = mouse_handler.event.getSceneY();

        double angle = Math.toDegrees(Math.atan2(mouse_y - y, mouse_x - x));
        Entity entity = new ProjectileEntity(angle,5, getX(), getY());
        entity.setPosition(x + 45 * (Math.cos(Math.toRadians(angle))), y + 45 * (Math.sin(Math.toRadians(angle))));
        GameScene.spawnEntity(entity);
    }

    public void lookAt() {

        double x, y;

        if(mouse_handler.event != null){

            x = mouse_handler.event.getSceneX();
            y = mouse_handler.event.getSceneY();
            // Calculate angle to target (x, y)
            double angle = Math.toDegrees(Math.atan2(y - entity_group.getLayoutY(), x - entity_group.getLayoutX()));

            // Apply rotation
            rotation.setAngle(angle);
        }

    }

    public double getX() {
        return entity_group.getLayoutX();
    }

    public void setPosition(double x, double y) {
        entity_group.setLayoutX(x);
        entity_group.setLayoutY(y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return entity_group.getLayoutY();
    }

    public void setY(double y) {
        this.y = y;
    }

    public Group getEntity_group() {
        return entity_group;
    }

    public void setEntity_group(Group entity_group) {
        this.entity_group = entity_group;
    }
}
