package com.example.bbc;

import entities.TankEntity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class GameLobbyUIController {
    // Color Options
    @FXML
    public Rectangle btnColorRed, btnColorYellow, btnColorBlue, btnColorOrange, btnColorLime, btnColorViolet,
            btnColorPink, btnColorCyan, btnColorEmerald, btnColorBlack;
    // Buttons to switch colors
    @FXML
    public Button btnSelectBody, btnSelectBarrel, btnSelectBorder;

    // Parts that will change color
    @FXML
    public Rectangle spTankBarrel;
    @FXML
    public Circle spTankBody;
    int buttonSelect = 1; // Default to body
    @FXML
    public void initialize() {
        btnSelectBody.setOnAction(event -> buttonSelect = 1);
        btnSelectBarrel.setOnAction(event -> buttonSelect = 2);
        btnSelectBorder.setOnAction(event -> buttonSelect = 3);
    }

    public void onChangeColorClick(javafx.scene.input.MouseEvent mouseEvent) {
        Rectangle color_picker = (Rectangle) mouseEvent.getSource();
        Paint selectedColor = color_picker.getFill();

        switch (buttonSelect) {
            case 1:
                spTankBody.setFill(selectedColor);
                break;
            case 2:
                spTankBarrel.setFill(selectedColor);
                break;
            case 3:
                spTankBody.setStroke(selectedColor);
                spTankBarrel.setStroke(selectedColor);
                break;
            default:
                // Handle unexpected buttonSelect value (optional)
                System.out.println("Invalid buttonSelect value: " + buttonSelect);
                break;
        }
    }
}
