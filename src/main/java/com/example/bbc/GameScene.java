package com.example.bbc;

import classes.Dialogues;
import classes.EnemyHPBar;
import configs.StatsConfig;
import datas.*;
import entities.Entity;
import entities.ProjectileEntity;
import entities.TankEntity;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import server.DeathMessageGenerator;

import java.util.*;
import java.util.List;
import static com.example.bbc.IOGame.MAIN_STAGE;
import static com.example.bbc.IOGame.SERVER_API;
import static utils.Helpers.rgbBytesToColor;
import static utils.Scenes.LOBBY_SCENE;

public class GameScene extends Scene {

    public static final int SCALE = 2;
    public static final int DEF_DIMENSION = 32;
    public static final int TILE_SIZE = DEF_DIMENSION * SCALE;

    public static ReadOnlyDoubleProperty WIDTH_PROPERTY = IOGame.MAIN_STAGE.widthProperty();
    public static ReadOnlyDoubleProperty HEIGHT_PROPERTY = IOGame.MAIN_STAGE.heightProperty();
    public static double SCREEN_WIDTH = IOGame.MAIN_STAGE.widthProperty().doubleValue(),  SCREEN_HEIGHT = IOGame.MAIN_STAGE.heightProperty().doubleValue();

    public static StackPane root = new StackPane();
    public static final StackPane server_entities_container = new StackPane();


    //changed player from final to static for color change testing
    public static TankEntity main_player;
    public static List<Entity> entity_list;
    public static List<Entity> received_entities;

    public static MouseHandler mouse_handler;
    public static KeyHandler key_handler;

    public static List<Line> vertical_bg_lines;
    public static List<Line> horizontal_bg_lines;

    public static boolean can_be_damaged = true;
    public static GameUIController game_ui_controller;

    public static int last_score;


    protected static void toLobbyRespawn(){
        Platform.runLater(() -> {
            game_ui_controller.resetValues();
            double width = WIDTH_PROPERTY.get();
            double height = HEIGHT_PROPERTY.get();
            MAIN_STAGE.setScene(LOBBY_SCENE);
            MAIN_STAGE.setWidth(width);
            MAIN_STAGE.setHeight(height);
            MAIN_STAGE.getScene().getRoot().requestLayout();
            MAIN_STAGE.setFullScreen(true);
            MAIN_STAGE.setFullScreen(IOGameSettings.getInstance().is_fullscreen);
            GameLobbyUIController.initializeTank();
        });
    }

    public GameScene() {
        super(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        System.out.println("Screen Dimensions: " + SCREEN_WIDTH + ", " + SCREEN_HEIGHT);

        root.setAlignment(Pos.CENTER);

        vertical_bg_lines = new ArrayList<>();
        horizontal_bg_lines = new ArrayList<>();

        //game_ui_controller = UI_OVERLAY.getController();

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
        entity_list = new LinkedList<>();
        received_entities = new ArrayList<>();


        main_player.setPosition(0,0);

        root.getChildren().add(server_entities_container);

        /*
        * we do not add the player to the entity list as we will be clearing the list on every GameData received
        * - Dymes
        * */
        main_player.render(root);

        this.setOnMouseMoved(mouse_handler);
        this.setOnMouseReleased(mouse_handler::mouseReleased);
        this.setOnMousePressed(mouse_handler::mousePressed);

        this.setOnKeyPressed(key_handler::keyPressed);
        this.setOnKeyReleased(key_handler::keyReleased);

        System.out.println("Game scene has "+root.getChildren());

        gameLoop.start();

    }

    public static void initializeOnGameUpdate(){
        SERVER_API.onLobbyUpdate(new ServerDataListener<LobbyData>() {
            @Override
            public void run(LobbyData data) {
                if (!data.deathMessage.isEmpty()) {
                    GameUIController.addMessage(data.deathMessage);
                }

                synchronized (SERVER_API.users_in_lobby) {
                    ListIterator<UserData> iter = SERVER_API.users_in_lobby.listIterator();

                    for (UserData i : data.users) {
                        if (iter.hasNext()) {
                            UserData cur = iter.next();

                            while (cur != null && cur.id != i.id) {
                                iter.remove();
                                cur = iter.hasNext() ? iter.next() : null;
                            }

                            if (cur == null) {
                                iter.add(i);
                            } else {
                                cur.score = i.score;
                                if (i.type == UserData.USER_FULL) {
                                    cur.barrel_color = i.barrel_color;
                                    cur.border_color = i.border_color;
                                    cur.body_color = i.body_color;
                                }
                            }
                        } else {
                            iter.add(i);
                        }
                    }
                }
            }
        });
        SERVER_API.onPlayerDeath(GameScene::toLobbyRespawn);
        SERVER_API.onGameUpdate(new ServerDataListener<GameData>() {
            @Override
            public void run(GameData data) {

                //Logging.write(this,"Rendering " + String.valueOf(data.entities.size()) + " entities");

                synchronized (received_entities) {

                    received_entities.clear();

                    List<EntityData> entities = data.entities;

                    //get player count
                    int ctr = 0;
                    for(EntityData ed : entities){
                        if(!ed.is_projectile) ctr++;
                    }


                    final int player_count = ctr;

                    if(last_score + 100 < SERVER_API.getUser().score){
                        last_score = (SERVER_API.getUser().score / 100) * 100;
                    }

                    Platform.runLater(() -> {
                        game_ui_controller.updateScore(SERVER_API.getUser().score);
                        game_ui_controller.updatePlayersLeftCounter(player_count);
                        game_ui_controller.refreshDeathMessages();
                    });

                    double x = entities.get(0).x;
                    double y = entities.get(0).y;

                    try {
                        game_ui_controller.setProgressBar(StatsConfig.PLAYER_HEALTH, entities.getFirst().health);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    main_player.pos_x = x;
                    main_player.pos_y = y;
                    main_player.setAngle(entities.get(0).angle);

                    int i = 0;
                    for(EntityData ed : entities){
                        //skip player
                        if(!ed.is_projectile){
                            if(i == 0){
                                synchronized (SERVER_API.users_in_lobby) {
                                    for(UserData ud : SERVER_API.users_in_lobby){
                                        if(ud.id == ed.id) {
                                            Paint body_color = rgbBytesToColor(ud.body_color);
                                            Paint barrel_color = rgbBytesToColor(ud.barrel_color);
                                            Paint border_color = rgbBytesToColor(ud.border_color);
                                            String name = ud.name;
                                            TankEntity tank = new TankEntity(body_color, barrel_color, border_color, ed.health, StatsConfig.PLAYER_HEALTH, name);
                                            tank.setPosition(ed.x, ed.y);
                                            tank.pos_x = ed.x;
                                            tank.pos_y = ed.y;
                                            tank.setAngle(ed.angle);

                                            game_ui_controller.setPlayer(tank);
                                        }
                                    }
                                }
                                i++;
                                continue;
                            }
                            //find the user with the given id

                            synchronized (SERVER_API.users_in_lobby) {
                                for(UserData ud : SERVER_API.users_in_lobby){

                                    if(ud.id == ed.id) {
                                        Paint body_color = rgbBytesToColor(ud.body_color);
                                        Paint barrel_color = rgbBytesToColor(ud.barrel_color);
                                        Paint border_color = rgbBytesToColor(ud.border_color);
                                        String name = ud.name;
                                        TankEntity tank = new TankEntity(body_color, barrel_color, border_color, ed.health, StatsConfig.PLAYER_HEALTH, name);
                                        tank.setPosition(ed.x - x, ed.y - y);
                                        tank.pos_x = ed.x - x;
                                        tank.pos_y = ed.y - y;
                                        tank.setAngle(ed.angle);
                                        received_entities.add(tank);
                                    }
                                }
                            }
                        }
                        else{
                            ProjectileEntity p = new ProjectileEntity(ed.angle, ed.x - x, ed.y - y);
                            p.setPosition(ed.x - x,ed.y - y);
                            received_entities.add(p);
                        }
                        i++;
                        //Logging.write(this, String.valueOf(ed.id));
                    }
                }


            }
        });
    }

    public static void renderEntities(){
        synchronized (entity_list) {
            System.out.println("Rendering " + server_entities_container.getChildren().size() + " entities");
            //clear previous entity_list
            server_entities_container.getChildren().clear();
            entity_list = received_entities;
        }
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
        for(int i = 0 - (int)SCREEN_WIDTH - 100;i < SCREEN_WIDTH + 100;i += 80){
            Line l = new Line(0,0,0,SCREEN_HEIGHT);
            l.setStrokeWidth(2.0);
            l.setTranslateX(i);
            l.setStroke(Color.valueOf("#AAAAAA"));
            vertical_bg_lines.add(l);
            root.getChildren().add(l);
            l.toBack();
        }
        for(int i = 0 - (int)SCREEN_HEIGHT - 100;i < SCREEN_HEIGHT + 100;i += 80){
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

    private double last_x = 0, last_y = 0;

    private void update(){
        moveBackground((main_player.pos_x - last_x) * -1, 0);
        moveBackground(0, (main_player.pos_y - last_y) * -1);

        last_x = main_player.pos_x;
        last_y = main_player.pos_y;
    }

    private final AnimationTimer gameLoop = new AnimationTimer() {
        private long lastUpdate = 0;
        private final InputData packet = new InputData();
        private boolean esc_sent = false;

        @Override
        public void handle(long now) {
            packet.right_pressed = key_handler.right_pressed;
            packet.left_pressed = key_handler.left_pressed;
            packet.up_pressed = key_handler.up_pressed;
            packet.down_pressed = key_handler.down_pressed;
            packet.lShift_pressed = key_handler.lShift_pressed;

            packet.lClick_pressed = mouse_handler.left_is_pressed;
            packet.angle = main_player.getAngle();
            SERVER_API.sendUserInput(packet);

            if (!esc_sent && key_handler.esc_pressed) {
                esc_sent = true;
                SERVER_API.suicide();
            } else if (esc_sent && !key_handler.esc_pressed) {
                esc_sent = false;
            }

            if (lastUpdate == 0) {
                lastUpdate = now;
                return;
            }

            root.getChildren().removeIf((Node e) -> {
                ListIterator<Entity> iter = entity_list.listIterator();
                while (iter.hasNext()) {
                    Entity cur = iter.next();
                    if (cur.getEntity_group() == e) {
                        iter.remove();
                        return true;
                    }
                }
                return false;
            });
            entity_list.clear();
            server_entities_container.getChildren().clear();

            synchronized (received_entities) {
                update();
                for (Entity e : received_entities) {
                    entity_list.add(e);
                    e.render(root);
                    if(e instanceof TankEntity) {
                        EnemyHPBar hpBar = new EnemyHPBar(((TankEntity)e).health,((TankEntity)e).max_health);
                        hpBar.setPosition(e.pos_x - 5, e.pos_y - 30);

                        Text player_name = new Text(((TankEntity)e).player_name);
                        player_name.setTranslateY(e.pos_y + 30);
                        player_name.setTranslateX(e.pos_x - 10);


                        server_entities_container.getChildren().add((hpBar.group));
                        server_entities_container.getChildren().add(player_name);

                    }
                }
            }

            double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // Convert nanoseconds to seconds
            lastUpdate = now;

        }
    };

}
