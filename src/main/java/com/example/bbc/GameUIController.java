package com.example.bbc;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;

public class GameUIController {
    @FXML private ProgressBar healthBar;
    @FXML private Label scoreLabel;
    @FXML private Label playerNameLabel;
    @FXML private VBox killFeedContainer;
    @FXML private Pane minimapPane;
    @FXML private VBox debugPanel;
    @FXML private Label coordsLabel;
    @FXML private Label fpsLabel;
    @FXML private Label entityCountLabel;
    
    public void initialize() {

    }
    
    public void toggleDebugInfo() {
        debugPanel.setVisible(!debugPanel.isVisible());
    }
}