package src;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Utils.main_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Utils.main_window.setResizable(true);
        Utils.main_window.setTitle("Untitled .io game");
        Utils.main_window.addWindowStateListener(Utils.window_state_listener); // handle changing of SCREEN_WIDTH/HEIGHT in game panel

        GamePanel game = new GamePanel();

        Utils.main_window.add(game);

        Utils.main_window.pack();
        Utils.main_window.setLocationRelativeTo(null);
        Utils.main_window.setVisible(true);
        
    }
}
