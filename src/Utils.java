package src;

import classes.entities.CameraEntity;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import javax.swing.*;

public abstract class Utils {

    public static JFrame main_window = new JFrame();

    public static int SCREEN_WIDTH = 1366, SCREEN_HEIGHT = 768;

    public static CameraEntity camera = new CameraEntity(SCREEN_WIDTH, SCREEN_HEIGHT);

    static Dimension size = Utils.main_window.getSize();

    public static WindowStateListener window_state_listener = (WindowEvent e) -> {
        if(e.getNewState() == JFrame.MAXIMIZED_BOTH){
            SCREEN_WIDTH = Utils.size.width;
            SCREEN_HEIGHT = Utils.size.height;
            System.out.println(size.width + " " + size.height);
        }
        else{
            SCREEN_WIDTH = 1366;
            SCREEN_HEIGHT = 768;
            System.out.println("llllllllll");
        }
        camera.width = SCREEN_WIDTH;
        camera.height = SCREEN_HEIGHT;
    };

    public static void showError(String message) {
        System.err.println(message);
    }
}
