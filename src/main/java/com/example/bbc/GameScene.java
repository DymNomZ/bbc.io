package com.example.bbc;

import entities.Entity;
import entities.ProjectileEntity;
import entities.TankEntity;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;

import java.util.*;
import java.util.List;

public class GameScene extends Scene {

    public static final int SCALE = 2;
    public static final int DEF_DIMENSION = 32;
    public static final int TILE_SIZE = DEF_DIMENSION * SCALE;

    public static ReadOnlyDoubleProperty WIDTH_PROPERTY = IOGame.MAIN_STAGE.widthProperty();
    public static ReadOnlyDoubleProperty HEIGHT_PROPERTY = IOGame.MAIN_STAGE.heightProperty();
    public static double SCREEN_WIDTH = 1280,  SCREEN_HEIGHT = 720;

    public static Group root = new Group();

    private final Entity main_player;
    private double center_x, center_y;
    public static List<Entity> entity_list;

    public static MouseHandler mouse_handler;
    public static KeyHandler key_handler;
    ImageView background_view;

    public GameScene() {
        super(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        Image background_image = new Image("file:src/main/java/assets/background.png");
        background_view = new ImageView(background_image);
        background_view.fitHeightProperty().bind(IOGame.MAIN_STAGE.heightProperty());
        background_view.fitWidthProperty().bind(IOGame.MAIN_STAGE.widthProperty());

        root.getChildren().add(background_view);

        mouse_handler = new MouseHandler();

        key_handler = new KeyHandler();

        main_player = new TankEntity();
        entity_list = new ArrayList<>();

        center_x = SCREEN_WIDTH / 2;
        center_y = SCREEN_HEIGHT / 2;

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

    //To ensure player is always on the screen regardless of screen size changes
    private void recalculate(){
        WIDTH_PROPERTY = IOGame.MAIN_STAGE.widthProperty();
        HEIGHT_PROPERTY = IOGame.MAIN_STAGE.heightProperty();
        SCREEN_WIDTH = WIDTH_PROPERTY.get();
        SCREEN_HEIGHT = HEIGHT_PROPERTY.get();
        center_x = SCREEN_WIDTH / 2;
        center_y = SCREEN_HEIGHT / 2;
    }

    private void update(){
        main_player.lookAt();
        if(mouse_handler.left_is_pressed){
//            main_player.shoot();
        }

//        System.out.println(background_view.getLayoutY());

        main_player.move();

        if(key_handler.up_pressed){
            background_view.setLayoutY(background_view.getLayoutY() + 5);
        }

        System.out.println(background_view.getLayoutY());
    }

    private final AnimationTimer gameLoop = new AnimationTimer() {
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {
            if (lastUpdate == 0) {
                lastUpdate = now;
                return;
            }

            recalculate();
//            System.out.println(SCREEN_WIDTH + " " + SCREEN_HEIGHT);
            update();
//            System.out.println(main_player.pos_x + " " + main_player.pos_y);
//            System.out.println(main_player.getLayoutX() + " " + main_player.getLayoutY());

            //player is rendered constantly in the center of the client's screen
            main_player.setPosition(center_x, center_y);

            Iterator it = entity_list.iterator();
            while (it.hasNext()) {
                Entity e = (Entity) it.next();
                if(e instanceof ProjectileEntity) {
                    ProjectileEntity projectile = (ProjectileEntity) e;
                    if(projectile.distanceFromOriginalPosition() >= 200){
                        despawnEntity(projectile);
                        it.remove();
                        continue;
                    }
                    double angleRadians = Math.toRadians(projectile.getAngle());
                    double deltaX = projectile.getSpeed() * Math.cos(angleRadians);
                    double deltaY = projectile.getSpeed() * Math.sin(angleRadians);
                    projectile.setPosition(projectile.getLayoutX() + deltaX, projectile.getLayoutY() + deltaY);
//                    System.out.println("Updating projectile");
                }
            }

            double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // Convert nanoseconds to seconds
            lastUpdate = now;
        }
    };

}
