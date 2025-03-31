package com.example.bbc;

import classes.Player;
import entities.Entity;
import entities.ProjectileEntity;
import entities.TankEntity;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameScene extends Scene {

    public static int FPS = 60; // delta loop purposes
    public static final int SCALE = 2;
    public static final int DEF_DIMENSION = 32;
    public static final int TILE_SIZE = DEF_DIMENSION * SCALE;
    public static double SCREEN_WIDTH = 700, SCREEN_HEIGHT = 700;

    public int max_map_row, max_map_col;
    private int second = 0;

    MapConstructor map;
    KeyHandler key_input;
    MouseHandler mouse_handler;
    public Thread main_thread;
    Player d1;

    static Group root = new Group();
    private Entity mainPlayer;
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private double mouse_x, mouse_y;
    public static List<Entity> entityList;

    public GameScene() {
        super(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        javafx.scene.image.Image backgroundImage = new Image("file:src/main/java/assets/background.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.fitHeightProperty().bind(IOGame.MAIN_STAGE.heightProperty());
        backgroundView.fitWidthProperty().bind(IOGame.MAIN_STAGE.widthProperty());

        root.getChildren().add(backgroundView);

        mainPlayer = new TankEntity();
        entityList = new ArrayList<>();

        double center_x = this.getWidth() / 2;
        double center_y = this.getHeight() / 2;

        spawnEntity(mainPlayer);

        this.setOnMouseMoved(event -> {
            mouse_x = event.getSceneX();
            mouse_y = event.getSceneY();
            mainPlayer.lookAt(mouse_x,mouse_y);
        });
        this.setOnMouseClicked(event -> {
            double x = mainPlayer.getX();
            double y = mainPlayer.getY();
            double angle = Math.toDegrees(Math.atan2(mouse_y - y, mouse_x - x));
            Entity entity = new ProjectileEntity(angle,5, mainPlayer.getX(), mainPlayer.getY());
            entity.setPosition(x + 45 * (Math.cos(Math.toRadians(angle))), y + 45 * (Math.sin(Math.toRadians(angle))));
            spawnEntity(entity);

        });

        gameLoop.start();

    }

    public void spawnEntity(Entity entity) {
        entityList.add(entity);
        entity.render(root);
//        entity.getEntityGroup().toBack();
    }

    public void despawnEntity(Entity entity) {
        root.getChildren().remove(entity.getEntityGroup());
    }

    private final AnimationTimer gameLoop = new AnimationTimer() {
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {
            if (lastUpdate == 0) {
                lastUpdate = now;
                return;
            }

            mainPlayer.getEntityGroup().setLayoutX(root.getScene().getWidth() / 2);
            mainPlayer.getEntityGroup().setLayoutY(root.getScene().getHeight() / 2);

            Iterator it = entityList.iterator();
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
                    System.out.println("Updating projectile");
                }
            }

            double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // Convert nanoseconds to seconds
            lastUpdate = now;
        }
    };

}
