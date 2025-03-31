package com.example.bbc;

import entities.Entity;
import entities.ProjectileEntity;
import entities.TankEntity;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import utils.Scenes;

import java.io.IOException;
import java.util.*;

public class IOGame extends Application {

    static Stage MAIN_STAGE;

    @Override
    public void start(Stage stage) throws IOException {

        MAIN_STAGE = stage;

        MAIN_STAGE.setTitle("bbc.io");
        MAIN_STAGE.setScene(Scenes.GAME_SCENE);
        // Maximize window by default
        MAIN_STAGE.setMaximized(true);

        // Set fullscreen
        MAIN_STAGE.setFullScreen(false);
        MAIN_STAGE.show();

    }

    public static void main(String[] args) {
        launch();
    }
}