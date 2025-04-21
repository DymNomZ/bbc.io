package com.example.bbc;

import entities.Entity;
import entities.ProjectileEntity;
import entities.TankEntity;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

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

    //changed player from final to static for color change testing
    public static Entity main_player;
    private double center_x, center_y;
    public static List<Entity> entity_list;

    public static MouseHandler mouse_handler;
    public static KeyHandler key_handler;

    public static List<Line> vertical_bg_lines;
    public static List<Line> horizontal_bg_lines;

    public static boolean can_be_damaged = true;


    public GameScene() {
        super(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        System.out.println("Screen Dimensions: " + SCREEN_WIDTH + ", " + SCREEN_HEIGHT);

        vertical_bg_lines = new ArrayList<>();
        horizontal_bg_lines = new ArrayList<>();

        HEIGHT_PROPERTY.addListener((observable, oldValue, newValue) -> {
            recalculate();
            adjustBackground();
        });
        WIDTH_PROPERTY.addListener((observable, oldValue, newValue) -> {
            recalculate();
            adjustBackground();
        });

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

    private Color brighten(Color color){
        double red = Math.min((color.getRed() + 0.5), 1.0);
        double green = Math.min((color.getGreen() + 0.5), 1.0);
        double blue = Math.min(color.getBlue() + 0.5, 1.0);
        double opacity = color.getOpacity();
        Color brightened = new Color(red, green, blue, opacity);
        return brightened;
    }

    private void damageBrighten(Shape s){

        Color fill = (Color) s.getFill();
        Color stroke = (Color)s.getStroke();

        s.setFill(brighten(fill));
        s.setStroke(brighten(stroke));

        //This is not a thread, more of like a sticky note
        //on the javafx thread
        //
        //Basically saying "do this 200ms in the future ok?" to the main thread
        PauseTransition pause = new PauseTransition(Duration.millis(200));
        pause.setOnFinished(e -> {
            s.setFill(fill);
            s.setStroke(stroke);
            can_be_damaged = true; // Unlock after cooldown
        });
        pause.play();
    }

    public void onPlayerDamaged() {
        can_be_damaged = false;
        Circle c = ((TankEntity) main_player).getMain_body();
        Rectangle r = ((TankEntity) main_player).getTurret();
        damageBrighten(c);
        damageBrighten(r);

    }

    public static void spawnEntity(Entity entity) {
        entity_list.add(entity);
        entity.render(root);
//        entity.getEntityGroup().toBack();
    }

    public void clearBackground(){
        for(Line l : vertical_bg_lines){
            root.getChildren().remove(l);
        }
        for(Line l : horizontal_bg_lines){
            root.getChildren().remove(l);
        }
        vertical_bg_lines.clear();
        horizontal_bg_lines.clear();
    }

    private void adjustBackground(){
        clearBackground();
        for(int i = 0 - (int)SCREEN_WIDTH;i < SCREEN_WIDTH;i += 80){
            Line l = new Line(0,0,0,SCREEN_HEIGHT);
            l.setStrokeWidth(2.0);
            l.setTranslateX(i);
            l.setStroke(Color.valueOf("#AAAAAA"));
            vertical_bg_lines.add(l);
            root.getChildren().add(l);
            l.toBack();
        }
        for(int i = 0 - (int)SCREEN_HEIGHT;i < SCREEN_HEIGHT;i += 80){
            Line l = new Line(0,0,SCREEN_WIDTH,0);
            l.setStrokeWidth(2.0);
            l.setTranslateY(i);
            l.setStroke(Color.valueOf("#AAAAAA"));
            horizontal_bg_lines.add(l);
            root.getChildren().add(l);
            l.toBack();
        }

    }
    private void moveBackground(double x, double y) {
        for(Line l : vertical_bg_lines){
            l.setTranslateX(l.getTranslateX() + x);
            if(l.getTranslateX() > SCREEN_WIDTH){
                l.setTranslateX(-SCREEN_WIDTH);
            } else if(l.getTranslateX() < -SCREEN_WIDTH){
                l.setTranslateX(SCREEN_WIDTH);
            }
        }
        for(Line l : horizontal_bg_lines){
            l.setTranslateY(l.getTranslateY() + y);
            if(l.getTranslateY() > SCREEN_HEIGHT){
                l.setTranslateY(-SCREEN_HEIGHT);
            } else if(l.getTranslateY() < -SCREEN_HEIGHT){
                l.setTranslateY(SCREEN_HEIGHT);
            }
        }
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
            main_player.shoot(root);
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
        if(key_handler.f_pressed){
            if(can_be_damaged)onPlayerDamaged();
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
