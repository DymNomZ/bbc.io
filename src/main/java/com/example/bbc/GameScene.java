package com.example.bbc;

import entities.Entity;
import entities.ProjectileEntity;
import entities.TankEntity;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import java.util.*;
import java.util.List;

public class GameScene extends Scene {

    public static final int SCALE = 2;
    public static final int DEF_DIMENSION = 32;
    public static final int TILE_SIZE = DEF_DIMENSION * SCALE;
    public static final double SCREEN_WIDTH = 700, SCREEN_HEIGHT = 700;

    public static Group root = new Group();

    private Entity main_player;
    private final Set<KeyCode> active_keys = new HashSet<>();
    private double mouse_x, mouse_y, center_x, center_y;
    public static List<Entity> entity_list;

    public static MouseHandler mouse_handler;
    public static KeyHandler key_handler;

    public GameScene() {
        super(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        javafx.scene.image.Image background_image = new Image("file:src/main/java/assets/background.png");
        ImageView background_view = new ImageView(background_image);
        background_view.fitHeightProperty().bind(IOGame.MAIN_STAGE.heightProperty());
        background_view.fitWidthProperty().bind(IOGame.MAIN_STAGE.widthProperty());

        root.getChildren().add(background_view);

        mouse_handler = new MouseHandler();

        key_handler = new KeyHandler();

        main_player = new TankEntity();
        entity_list = new ArrayList<>();

        center_x = this.getWidth() / 2;
        center_y = this.getHeight() / 2;

        main_player.setPosition(center_x, center_y);

        spawnEntity(main_player);

        this.setOnMouseMoved(mouse_handler::handle);
        this.setOnMouseReleased(mouse_handler::mouseReleased);
        this.setOnMousePressed(mouse_handler::mousePressed);

        this.setOnKeyPressed(key_handler::keyPressed);
        this.setOnKeyReleased(key_handler::keyReleased);

        gameLoop.start();

    }

    public static void spawnEntity(Entity entity) {
        entity_list.add(entity);
        entity.render(root);
//        entity.getEntityGroup().toBack();
    }

    private void despawnEntity(Entity entity) {
        root.getChildren().remove(entity.getEntity_group());
    }

    private void update(){
        main_player.lookAt();
        if(mouse_handler.left_is_pressed){
            main_player.shoot();
        }
        main_player.move();
    }

    private final AnimationTimer gameLoop = new AnimationTimer() {
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {
            if (lastUpdate == 0) {
                lastUpdate = now;
                return;
            }

            //center, will be important for camera implementation
//            main_player.setPosition(center_x, center_y);

            update();
            //System.out.println(main_player.getX() + " " + main_player.getY());

            Iterator it = entity_list.iterator();
            while (it.hasNext()) {
                Entity e = (Entity) it.next();
                if(e instanceof ProjectileEntity) {
                    ProjectileEntity projectile = (ProjectileEntity) e;
                    if(projectile.distanceFromOriginalPosition() >= 2000){
                        despawnEntity(projectile);
                        it.remove();
                        continue;
                    }
                    double angleRadians = Math.toRadians(projectile.getAngle());
                    double deltaX = projectile.getSpeed() * Math.cos(angleRadians);
                    double deltaY = projectile.getSpeed() * Math.sin(angleRadians);
                    projectile.setPosition(projectile.getX() + deltaX, projectile.getY() + deltaY);
//                    System.out.println("Updating projectile");
                }
            }

            double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // Convert nanoseconds to seconds
            lastUpdate = now;
        }
    };

}
