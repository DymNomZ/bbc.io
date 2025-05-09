package entities;

import com.example.bbc.GameScene;
import com.example.bbc.KeyHandler;
import com.example.bbc.MouseHandler;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;

public abstract class Entity {

    private final MouseHandler mouse_handler;
    protected KeyHandler key_handler;
    public double pos_x;
    public double pos_y;
    protected Group entity_group;
    private final Rotate rotation;

    public Entity() {
        entity_group = new Group();
        rotation = new Rotate(0); // Initialize rotation
        entity_group.getTransforms().add(rotation); // Apply rotation transform
        mouse_handler = GameScene.mouse_handler;
        key_handler = GameScene.key_handler;
        pos_x = (int)(Math.random() * 10);
        pos_y = (int)(Math.random() * 10);
    }

    public void render(StackPane root) {
        root.getChildren().add(entity_group);
    }

    private long last_shot_time = 0;
    private long shot_cooldown = 500_000_000;



    //FOR ENTITY DATA READING
    public void lookAt(double angle){
        // Apply rotation
        rotation.setAngle(angle);
    }

    public double getAngle() {
        double mouse_x, mouse_y, angle = 0;

        if(mouse_handler.event != null){

            mouse_x = mouse_handler.event.getSceneX();
            mouse_y = mouse_handler.event.getSceneY();
            // Calculate angle to target (x, y)
            angle = Math.toDegrees(Math.atan2(mouse_y - entity_group.getLayoutY(), mouse_x - entity_group.getLayoutX()));
        }

        return angle;
    }

    public void setAngle(double angle) {
        rotation.setAngle(angle);
    }

    public void lookAt() {

        double mouse_x, mouse_y;

        if(mouse_handler.event != null){

            mouse_x = mouse_handler.event.getSceneX();
            mouse_y = mouse_handler.event.getSceneY();
            // Calculate angle to target (x, y)
            double angle = Math.toDegrees(Math.atan2(mouse_y - entity_group.getLayoutY(), mouse_x - entity_group.getLayoutX()));

            // Apply rotation
            rotation.setAngle(angle);
        }

    }

    //will handle rendering of player on the screen
    public void setPosition(double x, double y) {
        entity_group.setTranslateX(x);
        entity_group.setTranslateY(y);
    }

    public double getLayoutX() {
        return entity_group.getTranslateX();
    }

    public double getLayoutY() {
        return entity_group.getTranslateY();
    }

    public Group getEntity_group() {
        return entity_group;
    }

    public void setEntity_group(Group entity_group) {
        this.entity_group = entity_group;
    }
}
