package src;

import classes.entities.*;
import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

    public static int FPS = 60; // delta loop purposes
    public static final int SCALE = 2;
    public static final int DEF_DIMENSION = 32;
    public static final int TILE_SIZE = DEF_DIMENSION * SCALE;
    public static int SCREEN_WIDTH = Utils.SCREEN_WIDTH, SCREEN_HEIGHT = Utils.SCREEN_HEIGHT;

    public int max_map_row, max_map_col;
    private int second = 0;

    MapConstructor map;
    KeyHandler key_input;
    MouseHandler mouse_handler;
    public Thread main_thread;
    Player d1;

    Dimension dimensions = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);

    public GamePanel(){

        key_input = new KeyHandler();
        mouse_handler = new MouseHandler();
        map = new MapConstructor("assets/maps/square_test.zip");

        max_map_row = map.getMap_height();
        max_map_col = map.getMap_length();
        //System.out.println(max_map_col + " " + max_map_row);

        d1 = new Player("Amogus", 69, 10 * TILE_SIZE, 13 * TILE_SIZE, 64, 0, key_input);

        this.setPreferredSize(dimensions);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(key_input);
        this.addMouseListener(mouse_handler);
        this.setLayout(null);

    }

    public void start_main_thread(){
        main_thread = new Thread(this);
        main_thread.start();
    }

    @Override
    public void run() {
        long nanoInterval = 16666667; // 60 fps
        long lastEntityCheck = 0;
        long last_system_time = System.nanoTime();
        long current_system_time;
        long delta = 0;

        while (main_thread != null) {
            
            last_system_time = System.nanoTime() - (System.nanoTime() - last_system_time);
            repaint();
            d1.move();

            dimensions.width = Utils.SCREEN_WIDTH;
            dimensions.height = Utils.SCREEN_HEIGHT;

            SCREEN_WIDTH = Utils.SCREEN_WIDTH;
            SCREEN_HEIGHT = Utils.SCREEN_HEIGHT;

            Utils.camera.width = Utils.SCREEN_WIDTH;
            Utils.camera.height = Utils.SCREEN_HEIGHT;

            mouse_handler.mouse_location_on_screen = MouseInfo.getPointerInfo().getLocation();

            SwingUtilities.convertPointFromScreen(mouse_handler.mouse_location_on_screen,this);
            updateMousePosition(mouse_handler.mouse_location_on_screen.x,mouse_handler.mouse_location_on_screen.y);

            if (lastEntityCheck < last_system_time) {

                lastEntityCheck = System.nanoTime() + 1000000000;
                
                second++;
                // System.out.println("POS TILE: " + d1.getTileXPosition() + " " + d1.getTileYPosition() + " " + PlayerData.offsets[PlayerData.check_pointer]);
                //System.out.println("lol");

            }

            current_system_time = System.nanoTime();
            delta = Math.max(nanoInterval - current_system_time + last_system_time, 0);
            last_system_time = current_system_time;

            try {
                Thread.sleep(delta / 1000000, (int)(delta % 1000000));
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void updateMousePosition(int x, int y) {
        mouse_handler.mouse_x = x - (d1.x - Utils.camera.x);
        mouse_handler.mouse_y = y - (d1.y - Utils.camera.y);
    }

    void handleInteractions(Player d1, Graphics g) {

    }

    @Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        map.view(d1, Utils.camera);
        map.displayTiles(g, Utils.camera);
        map.verifyEntityPosition(d1);
        d1.display(g, Utils.camera);

        handleInteractions(d1, g);

        //debug check coords for spawning purposes
        // System.out.println("POS: " + d1.x + " " + d1.y);
        //System.out.println();
        //System.out.println("POS TILE: " + d1.getTileXPosition() + " " + d1.getTileYPosition());
    }

}
