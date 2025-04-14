package com.example.bbc;

import entities.Entity;
import entities.ProjectileEntity;
import entities.TankEntity;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.List;

public class GameScene extends Scene {

    public static final int SCALE = 2;
    public static final int DEF_DIMENSION = 32;
    public static final int TILE_SIZE = DEF_DIMENSION * SCALE;

    public static ReadOnlyDoubleProperty WIDTH_PROPERTY = IOGame.MAIN_STAGE.widthProperty();
    public static ReadOnlyDoubleProperty HEIGHT_PROPERTY = IOGame.MAIN_STAGE.heightProperty();
    public static double SCREEN_WIDTH = 1280,  SCREEN_HEIGHT = 720;

    public static StackPane root = new StackPane();

    private final Entity main_player;
    private double center_x, center_y;
    public static List<Entity> entity_list;

    public static MouseHandler mouse_handler;
    public static KeyHandler key_handler;
    ImageView background_view1;
    ImageView background_view2;
    ImageView background_view3;
    ImageView background_view4;


    public GameScene() {
        super(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        System.out.println("Screen Dimensions: " + SCREEN_WIDTH + ", " + SCREEN_HEIGHT);


        Image background_image = new Image("file:src/main/java/assets/background.png");
        background_view1 = new ImageView(background_image);
        background_view1.fitWidthProperty().bind(WIDTH_PROPERTY);
        background_view1.fitHeightProperty().bind(HEIGHT_PROPERTY);
        background_view1.setTranslateX(0);
        background_view1.setTranslateY(0);

        background_view2 = new ImageView(background_image);
        background_view2.fitWidthProperty().bind(WIDTH_PROPERTY);
        background_view2.fitHeightProperty().bind(HEIGHT_PROPERTY);
        background_view2.setTranslateX(0 + SCREEN_WIDTH);
        background_view2.setTranslateY(0);

        background_view3 = new ImageView(background_image);
        background_view3.fitWidthProperty().bind(WIDTH_PROPERTY);
        background_view3.fitHeightProperty().bind(HEIGHT_PROPERTY);
        background_view3.setTranslateX(0 + SCREEN_WIDTH);
        background_view3.setTranslateY(0 + SCREEN_HEIGHT);

        background_view4 = new ImageView(background_image);
        background_view4.fitWidthProperty().bind(WIDTH_PROPERTY);
        background_view4.fitHeightProperty().bind(HEIGHT_PROPERTY);
        background_view4.setTranslateX(0);
        background_view4.setTranslateY(0 + SCREEN_HEIGHT);


        root.getChildren().add(background_view1);
        root.getChildren().add(background_view2);
        root.getChildren().add(background_view3);
        root.getChildren().add(background_view4);

        mouse_handler = new MouseHandler();

        key_handler = new KeyHandler();

        main_player = new TankEntity();
        entity_list = new ArrayList<>();

        center_x = SCREEN_WIDTH / 2;
        center_y = SCREEN_HEIGHT / 2;

        main_player.setPosition(0,0);


        spawnEntity(main_player);

        this.setOnMouseMoved(mouse_handler);
        this.setOnMouseReleased(mouse_handler::mouseReleased);
        this.setOnMousePressed(mouse_handler::mousePressed);

        this.setOnKeyPressed(key_handler::keyPressed);
        this.setOnKeyReleased(key_handler::keyReleased);

        System.out.println(root.getChildren());
        gameLoop.start();


    }

    public static void spawnEntity(Entity entity) {
        entity_list.add(entity);
        entity.render(root);
//        entity.getEntityGroup().toBack();
    }

    public void moveBackground(double x, double y) {
        // List of background views for easy iteration
        List<ImageView> backgrounds = Arrays.asList(background_view1, background_view2, background_view3, background_view4);

        // Move each background by x and y
        for (ImageView background : backgrounds) {
            background.setTranslateX(background.getTranslateX() + x);
            background.setTranslateY(background.getTranslateY() + y);
        }

        // Check and reset the background layers if they move off-screen (vertically and horizontally)
        for (ImageView background : backgrounds) {
            // Vertical reset
            if (background.getTranslateY() >= SCREEN_HEIGHT) {
                // If the background has moved off the screen, move it back to the top.
                background.setTranslateY(background.getTranslateY() - SCREEN_HEIGHT * 2);  // Move back to top
            }
            if (background.getTranslateY() <= -SCREEN_HEIGHT) {
                // If the background has moved too far up, move it back to the bottom.
                background.setTranslateY(background.getTranslateY() + SCREEN_HEIGHT * 2);  // Move back to bottom
            }

            // Horizontal reset
            if (background.getTranslateX() >= SCREEN_WIDTH) {
                // If the background has moved off the screen, move it back to the left.
                background.setTranslateX(background.getTranslateX() - SCREEN_WIDTH * 2);  // Move back to left
            }
            if (background.getTranslateX() <= -SCREEN_WIDTH) {
                // If the background has moved too far to the left, move it back to the right.
                background.setTranslateX(background.getTranslateX() + SCREEN_WIDTH * 2);  // Move back to right
            }
        }

        System.out.println("Y Translation: " + background_view4.getTranslateY());
        System.out.println("X Translation: " + background_view4.getTranslateX());
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
    }

    private void update(){

        main_player.lookAt();

        if(mouse_handler.left_is_pressed){
            main_player.shoot();
        }

//        if(key_handler.movementKeysPressed())
//            ((TankEntity)main_player).move();

        //FIXME
        if (key_handler.up_pressed) {
            moveBackground(0,5);
        }
        if (key_handler.down_pressed) {
            moveBackground(0,-5);
        }
        if (key_handler.left_pressed) {
            moveBackground(5,0);
        }
        if (key_handler.right_pressed) {
            moveBackground(-5,0);
        }

//        System.out.println(background_view.getLayoutY());
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
            update();

            //player is rendered constantly in the center of the client's screen

            //This should be removed as this is handled in the server
            Iterator it = entity_list.iterator();
            while (it.hasNext()) {
                Entity e = (Entity) it.next();
                if(e instanceof ProjectileEntity) {
                    ProjectileEntity projectile = (ProjectileEntity) e;
//                    if(projectile.distanceFromOriginalPosition() >= 200){
//                        despawnEntity(projectile);
//                        it.remove();
//                        continue;
//                    }
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
