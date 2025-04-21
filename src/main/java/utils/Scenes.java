package utils;

import com.example.bbc.GameScene;
import com.example.bbc.GameUIOverlay;
import com.example.bbc.IOGame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

import static com.example.bbc.GameScene.SCREEN_HEIGHT;
import static com.example.bbc.GameScene.SCREEN_WIDTH;

public class Scenes {

    public static Scene GAME_SCENE;
    public static Scene TITLE_SCENE;
    public static Scene LOBBY_SCENE;
    public static GameUIOverlay UI_OVERLAY;

    static FXMLLoader titleSceneFXML = new FXMLLoader(IOGame.class.getResource("title-scene.fxml"));
    static FXMLLoader lobbySceneFXML = new FXMLLoader(IOGame.class.getResource("game-lobby-ui.fxml"));
    static FXMLLoader devsFXML = new FXMLLoader(Scenes.class.getResource("dev-page.fxml"));
    
    static {
        try {
            GameScene gameScene = new GameScene();
            UI_OVERLAY = new GameUIOverlay();
            GAME_SCENE = UI_OVERLAY.applyToScene(gameScene);

            TITLE_SCENE = new Scene(titleSceneFXML.load(), SCREEN_WIDTH, SCREEN_HEIGHT);
            LOBBY_SCENE = new Scene(lobbySceneFXML.load(), SCREEN_WIDTH, SCREEN_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
            GAME_SCENE = new GameScene();
        }
    }
}