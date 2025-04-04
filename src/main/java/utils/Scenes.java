package utils;

import com.example.bbc.GameScene;
import com.example.bbc.GameUIOverlay;
import com.example.bbc.IOGame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class Scenes {

    public static Scene GAME_SCENE;
    public static GameUIOverlay UI_OVERLAY;
    
    static {
        try {
            // Create the base game scene
            GameScene gameScene = new GameScene();
            
            // Create and apply UI overlay
            UI_OVERLAY = new GameUIOverlay();
            GAME_SCENE = UI_OVERLAY.applyToScene(gameScene);
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback to base game scene without UI if there's an error
            GAME_SCENE = new GameScene();
        }
    }
}