package com.example.bbc;

import datas.UserData;
import entities.TankEntity;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import utils.Helpers;
import utils.Scenes;
import javafx.event.ActionEvent;

import static com.example.bbc.IOGame.SERVER_API;
import static utils.Scenes.GAME_SCENE;

public class GameLobbyUIController {
    // Color Options
    @FXML
    public Rectangle btnColorRed, btnColorYellow, btnColorBlue, btnColorOrange, btnColorLime, btnColorViolet,
            btnColorPink, btnColorCyan, btnColorEmerald, btnColorBlack;
    // Buttons to switch colors
    @FXML
    public Button btnSelectBody, btnSelectBarrel, btnSelectBorder;
    
    // Ready button
    @FXML
    public Button btnReady;

    // Parts that will change color
    @FXML
    public Rectangle spTankBarrel;
    @FXML
    public Circle spTankBody;
    public VBox root;

    // Store selected colors for transferring to game
    public static Paint bodyColor;
    public static Paint barrelColor;
    public static Paint borderColor;

    private static Rectangle tankBarrel;
    private static Circle tankBody;

    int buttonSelect = 1; // Default to body
    
    @FXML
    public void initialize() {


        Platform.runLater(() -> {
            // Force layout updates
            root.applyCss();
            root.layout();
        });


        btnSelectBody.setOnAction(event -> buttonSelect = 1);
        btnSelectBarrel.setOnAction(event -> buttonSelect = 2);
        btnSelectBorder.setOnAction(event -> buttonSelect = 3);

        // Set default colors
        bodyColor = spTankBody.getFill();
        barrelColor = spTankBarrel.getFill();
        borderColor = spTankBody.getStroke();

        tankBarrel = spTankBarrel;
        tankBody = spTankBody;
    }

    public static void initializeTank() {
        UserData user = SERVER_API.getUser();

        bodyColor = Helpers.rgbBytesToColor(user.body_color);
        barrelColor = Helpers.rgbBytesToColor(user.barrel_color);
        borderColor = Helpers.rgbBytesToColor(user.border_color);

        tankBody.setFill(bodyColor);
        tankBarrel.setFill(barrelColor);
        tankBody.setStroke(borderColor);
        tankBarrel.setStroke(borderColor);
    }

    public void onChangeColorClick(javafx.scene.input.MouseEvent mouseEvent) {
        Rectangle color_picker = (Rectangle) mouseEvent.getSource();
        Paint selectedColor = color_picker.getFill();

        switch (buttonSelect) {
            case 1:
                spTankBody.setFill(selectedColor);
                bodyColor = selectedColor;
                break;
            case 2:
                spTankBarrel.setFill(selectedColor);
                barrelColor = selectedColor;
                break;
            case 3:
                spTankBody.setStroke(selectedColor);
                spTankBarrel.setStroke(selectedColor);
                borderColor = selectedColor;
                break;
            default:
                // Handle unexpected buttonSelect value (optional)
                System.out.println("Invalid buttonSelect value: " + buttonSelect);
                break;
        }
    }
    
    @FXML
    public void onReadyButtonClick(ActionEvent event) {
        // Apply selected colors to the TankEntity before starting the game
        applyColorsToTankEntity();

        UserData current_user = SERVER_API.getUser();

        //converting to bytes
        current_user.body_color = Helpers.colorToRGBBytes((Color)bodyColor);
        current_user.barrel_color = Helpers.colorToRGBBytes((Color)barrelColor);
        current_user.border_color = Helpers.colorToRGBBytes((Color)borderColor);

        //send data to server
        SERVER_API.modifyUser(current_user);
        SERVER_API.play();
        
        // Switch to game scene
        Stage stage = (Stage) btnReady.getScene().getWindow();

        stage.setScene(GAME_SCENE);
        stage.setFullScreen(IOGameSettings.getInstance().is_fullscreen);
        // Force a layout recalculation after changing the scene
        GAME_SCENE.getRoot().requestLayout();  // Request layout on the new root of the scene

        // Optionally, you can also force layout on the stage or parent container
        stage.getScene().getRoot().requestLayout();  // Request layout on the root node of the new scene

        if(!IOGameSettings.getInstance().is_fullscreen) {
            stage.setFullScreen(true);
            stage.setFullScreen(false);
            System.out.println("I RESIZED");
            // Force layout recalculation by "simulating" a resize
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            // Temporarily resize the window
            stage.setWidth(currentWidth + 1);
            stage.setHeight(currentHeight + 1);

            // Restore the original size
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);
        }
        stage.setTitle("Battle Game");
    }
    
    private void applyColorsToTankEntity() {
        try {
            // Access the player's tank directly
            TankEntity playerTank = (TankEntity) GameScene.main_player; 
            
            if (playerTank != null) {
                // Apply selected colors
                playerTank.getMain_body().setFill(bodyColor);
                playerTank.getTurret().setFill(barrelColor);
                playerTank.getMain_body().setStroke(borderColor);
                playerTank.getTurret().setStroke(borderColor);
            } else {
                System.err.println("Player tank not available");
            }
        } catch (Exception e) {
            System.err.println("Error applying colors to tank: " + e.getMessage());
            e.printStackTrace();
        }
    }
}