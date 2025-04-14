package com.example.bbc;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.Scenes;
import java.io.IOException;

@Deprecated
public class IOGame extends Application {

    static Stage MAIN_STAGE;

    @Override
    public void start(Stage stage) throws IOException {

        MAIN_STAGE = stage;

        MAIN_STAGE.setTitle("bbc.io");
        MAIN_STAGE.setMinWidth(1280);
        MAIN_STAGE.setMinHeight(720);
        MAIN_STAGE.setScene(Scenes.GAME_SCENE);
        MAIN_STAGE.setWidth(1280);
        MAIN_STAGE.setHeight(720);
        // Maximize window by default
        MAIN_STAGE.setMaximized(false);
        // Set fullscreen
        MAIN_STAGE.setFullScreen(false);

        MAIN_STAGE.show();
    }

    public static void main(String[] args) {
        launch();
    }
}