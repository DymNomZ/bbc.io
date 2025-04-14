package utils;

import com.example.bbc.GameScene;
import com.example.bbc.GameUIOverlay;
import com.example.bbc.IOGame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class Scenes {

    public static Scene GAME_SCENE;
    public static Scene TITLE_SCENE;
    public static GameUIOverlay UI_OVERLAY;

    static FXMLLoader titleSceneFXML = new FXMLLoader(IOGame.class.getResource("title-scene.fxml"));

    static FXMLLoader devsFXML = new FXMLLoader(Scenes.class.getResource("dev-page.fxml"));
    public static Scene DEVS_SCENE;
    
    static {
        try {
//            DEVS_SCENE = new Scene(devsfxml.load(), 1280, 720);      //causes error when running IOGame
            // Create the base game scene
            GameScene gameScene = new GameScene();
            
            // Create and apply UI overlay
            UI_OVERLAY = new GameUIOverlay();
            GAME_SCENE = UI_OVERLAY.applyToScene(gameScene);

            TITLE_SCENE = new Scene(titleSceneFXML.load(), 600, 500);

        } catch (IOException e) {
            e.printStackTrace();
            // Fallback to base game scene without UI if there's an error
            GAME_SCENE = new GameScene();
        }
    }
}