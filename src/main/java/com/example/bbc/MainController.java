package com.example.bbc;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    public StackPane main_container;
    public void initialize() {
        switchView("title-scene.fxml");
    }
    public void switchView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            main_container.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
