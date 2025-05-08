package utils;

import com.example.bbc.GameScene;
import com.example.bbc.GameUIOverlay;
import com.example.bbc.IOGame;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

import static com.example.bbc.GameScene.SCREEN_HEIGHT;
import static com.example.bbc.GameScene.SCREEN_WIDTH;

public class Scenes {

    public static Scene GAME_SCENE;
    public static Scene TITLE_SCENE;
    public static Scene LOBBY_SCENE;
    public static GameUIOverlay UI_OVERLAY;

    public static FXMLLoader titleSceneFXML = new FXMLLoader(IOGame.class.getResource("title-scene.fxml"));
    public static FXMLLoader lobbySceneFXML = new FXMLLoader(IOGame.class.getResource("game-lobby-ui.fxml"));
    public static FXMLLoader devsFXML = new FXMLLoader(Scenes.class.getResource("dev-page.fxml"));

    public static String titleSceneFXMLResource = "title-scene.fxml";
    public static String lobbySceneFXMLResource = "game-lobby-ui.fxml";
    public static String devsSceneFXMLResource = "dev-page.fxml";
    
    static {
        try {
            GameScene gameScene = new GameScene();
            UI_OVERLAY = new GameUIOverlay();
            GAME_SCENE = UI_OVERLAY.applyToScene(gameScene);
            Platform.runLater(() -> gameScene.game_ui_controller = UI_OVERLAY.getController());


            Parent titleRoot = titleSceneFXML.load();
            TITLE_SCENE = new Scene(titleRoot);

            Parent lobbyRoot = lobbySceneFXML.load();
            LOBBY_SCENE = new Scene(lobbyRoot);

        } catch (IOException e) {
            e.printStackTrace();
            GAME_SCENE = new GameScene();
        }
    }
}