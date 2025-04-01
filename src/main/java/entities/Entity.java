package entities;

import com.example.bbc.GameScene;
import com.example.bbc.KeyHandler;
import com.example.bbc.MouseHandler;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;

public abstract class Entity {

    private MouseHandler mouse_handler;
    private KeyHandler key_handler;
    public double pos_x;
    public double pos_y;
    protected Group entity_group;
    private Rotate rotation;

    public Entity() {
        entity_group = new Group();
        rotation = new Rotate(0); // Initialize rotation
        entity_group.getTransforms().add(rotation); // Apply rotation transform
        mouse_handler = GameScene.mouse_handler;
        key_handler = GameScene.key_handler;
        pos_x = 0;
        pos_y = 0;
    }

    public void render(Group root) {
        root.getChildren().add(entity_group);
    }

    public void move(){

        int speed = 5;

        if(key_handler.up_pressed) pos_y -= speed;
        else if(key_handler.down_pressed) pos_y += speed;

        else if(key_handler.left_pressed) pos_x -= speed;
        else if(key_handler.right_pressed) pos_x += speed;

        //TODO: Send player position data to server
    }

    private long last_shot_time = 0;
    private long shot_cooldown = 500_000_000;

    public void shoot(){

        long currentTime = System.nanoTime();
        if (currentTime - last_shot_time < shot_cooldown) {
            return;
        }

        double x = getLayoutX();
        double y = getLayoutY();
        double mouse_x = mouse_handler.event.getSceneX();
        double mouse_y = mouse_handler.event.getSceneY();

        double angle = Math.toDegrees(Math.atan2(mouse_y - y, mouse_x - x));
        Entity entity = new ProjectileEntity(angle, 5, getLayoutX(), getLayoutY());
        entity.setPosition(x + 45 * (Math.cos(Math.toRadians(angle))), y + 45 * (Math.sin(Math.toRadians(angle))));
        GameScene.spawnEntity(entity);

        //TODO: Send projectile position data to server

        last_shot_time = currentTime;
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
        entity_group.setLayoutX(x);
        entity_group.setLayoutY(y);
    }

    public double getLayoutX() {
        return entity_group.getLayoutX();
    }

    public double getLayoutY() {
        return entity_group.getLayoutY();
    }


    public Group getEntity_group() {
        return entity_group;
    }

    public void setEntity_group(Group entity_group) {
        this.entity_group = entity_group;
    }
}
