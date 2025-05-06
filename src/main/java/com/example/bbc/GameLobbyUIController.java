package com.example.bbc;

import datas.UserData;
import entities.TankEntity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import utils.Helpers;
import utils.Scenes;
import javafx.event.ActionEvent;

import static com.example.bbc.IOGame.SERVER_API;

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
    
    // Store selected colors for transferring to game
    private Paint bodyColor;
    private Paint barrelColor;
    private Paint borderColor;
    
    int buttonSelect = 1; // Default to body
    
    @FXML
    public void initialize() {




        btnSelectBody.setOnAction(event -> buttonSelect = 1);
        btnSelectBarrel.setOnAction(event -> buttonSelect = 2);
        btnSelectBorder.setOnAction(event -> buttonSelect = 3);
        
        // Set default colors
        bodyColor = spTankBody.getFill();
        barrelColor = spTankBarrel.getFill();
        borderColor = spTankBody.getStroke();
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
        stage.setScene(Scenes.GAME_SCENE);
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