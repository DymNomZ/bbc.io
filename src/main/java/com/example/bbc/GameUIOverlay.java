package com.example.bbc;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;


//Helper class to add a UI overlay to the existing game scene
public class GameUIOverlay {

    private Parent uiRoot;
    private GameUIController controller;
    
    public GameUIOverlay() throws IOException {
        FXMLLoader uiLoader = new FXMLLoader(getClass().getResource("game-ui.fxml"));
        uiRoot = uiLoader.load();
        controller = uiLoader.getController();
        
        // Make UI transparent to mouse events where there are no UI controls
        // This allows clicking through empty parts of the UI to the game beneath
        uiRoot.setPickOnBounds(false);
    }
    
    /**
     * Applies the UI overlay to an existing scene by wrapping the original root
     * in a StackPane with the UI on top
     */
    public Scene applyToScene(Scene originalScene) {
        // Get the original root
        Parent originalRoot = originalScene.getRoot();
        
        // Create a StackPane to hold both the original content and the UI overlay
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(originalRoot, uiRoot);
        
        // Create a new scene with the StackPane as root
        Scene newScene = new Scene(stackPane, originalScene.getWidth(), originalScene.getHeight());
        
        // Copy stylesheets from original scene if any
        newScene.getStylesheets().addAll(originalScene.getStylesheets());
        
        // Preserve existing event handlers from the original scene
        preserveEventHandlers(originalScene, newScene);
        
        // Add key event to toggle debug panel with F3
        newScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F3) {
                controller.toggleDebugInfo();
            }
        });
        
        return newScene;
    }
    
    /**
     * Preserve event handlers from the original scene
     */
    private void preserveEventHandlers(Scene originalScene, Scene newScene) {
        // Preserve key event handlers
        if (originalScene.getOnKeyPressed() != null) {
            newScene.setOnKeyPressed(originalScene.getOnKeyPressed());
        }
        
        if (originalScene.getOnKeyReleased() != null) {
            newScene.setOnKeyReleased(originalScene.getOnKeyReleased());
        }
        
        // Preserve mouse event handlers
        if (originalScene.getOnMouseMoved() != null) {
            newScene.setOnMouseMoved(originalScene.getOnMouseMoved());
        }
        
        if (originalScene.getOnMousePressed() != null) {
            newScene.setOnMousePressed(originalScene.getOnMousePressed());
        }
        
        if (originalScene.getOnMouseReleased() != null) {
            newScene.setOnMouseReleased(originalScene.getOnMouseReleased());
        }
        
        if (originalScene.getOnMouseDragged() != null) {
            newScene.setOnMouseDragged(originalScene.getOnMouseDragged());
        }
        
        if (originalScene.getOnMouseClicked() != null) {
            newScene.setOnMouseClicked(originalScene.getOnMouseClicked());
        }
    }
    
    /**
     * Utility method to directly apply the UI overlay to a stage
     */
    public void applyToStage(Stage stage) throws IOException {
        Scene originalScene = stage.getScene();
        Scene newScene = applyToScene(originalScene);
        stage.setScene(newScene);
    }
    
    /**
     * Get the UI controller for later use
     */
    public GameUIController getController() {
        return controller;
    }
}