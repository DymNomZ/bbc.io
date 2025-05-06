package com.example.bbc;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import utils.FontLoader;
import utils.Scenes;

import java.io.IOException;

public class IOGame extends Application {

    static Stage MAIN_STAGE;
    static ServerHandler SERVER_API = null;

    @Override
    public void start(Stage stage) throws IOException {
        MAIN_STAGE = stage;
        MAIN_STAGE.setTitle("bbc.io");
        MAIN_STAGE.setMinWidth(1280);
        MAIN_STAGE.setMinHeight(720);
        MAIN_STAGE.setScene(Scenes.TITLE_SCENE);
        Platform.runLater(() -> {
            Region root = (Region) IOGame.MAIN_STAGE.getScene().getRoot();
            root.applyCss();
            root.layout();
        });

        MAIN_STAGE.setWidth(1280);
        MAIN_STAGE.setHeight(720);
        // Maximize window by default
        MAIN_STAGE.setMaximized(false);
        // Set fullscreen
        MAIN_STAGE.setFullScreen(false);

        MAIN_STAGE.show();
    }

    public static void main(String[] args) {
        FontLoader.loadGameFonts();
        launch();
    }
    public static void changeScene(String sceneFXML) throws IOException {
        double w = MAIN_STAGE.getWidth();
        double h = MAIN_STAGE.getHeight();

        FXMLLoader loader = new FXMLLoader(IOGame.class.getResource(sceneFXML));
        Parent root = loader.load();
        Scene scene = new Scene(root, w, h);
        MAIN_STAGE.setScene(scene);

        // Force layout fix
        Platform.runLater(() -> {
            System.out.println("Bounds: " + root.getLayoutBounds());
            System.out.println("Width: " + root.getScene().getWidth());
            root.applyCss();
            root.layout();
        });
    }
}