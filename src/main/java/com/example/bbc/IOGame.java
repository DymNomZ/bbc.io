package com.example.bbc;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.FontLoader;
import utils.Scenes;

import java.io.IOException;

public class IOGame extends Application {

    static Stage MAIN_STAGE;
    static ServerHandler SERVER_API = new ServerHandler();
    public Parent main_root;

    private static MainController mainController;
    public static MainController getMainController() {
        return mainController;
    }

    @Override
    public void start(Stage stage) throws IOException {

        Platform.runLater(()-> {
            try {
                Class.forName("utils.Scenes");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        IOGameSettings.setInstance();
        applySettings();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("iogame-main.fxml"));
        main_root = loader.load();
        mainController = loader.getController();
        System.out.println(mainController);

        stage.setMinHeight(720);
        stage.setMinWidth(1280);


        Scene scene = new Scene(main_root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("IO Game");
        stage.show();
        MAIN_STAGE = stage;
    }

    public static void main(String[] args) {
        FontLoader.loadGameFonts();
        launch();
    }

    public static void applySettings(){
        Platform.runLater(()->{
            IOGameSettings instance = IOGameSettings.getInstance();
            MAIN_STAGE.setFullScreen(instance.is_fullscreen);
        });
    }

}