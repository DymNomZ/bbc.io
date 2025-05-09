package com.example.bbc;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;

public class SettingsController {
    public ComboBox combobox_resolution;
    public ComboBox combobox_screen_mode;
    public ToggleButton toggle_player_names;
    public Button btn_apply;
    private boolean has_applied_changes = true;

    public void initialize() {
        Platform.runLater(() -> {
            combobox_resolution.getItems().add("1280 x 720");
            combobox_resolution.getItems().add("1366 x 768");
            combobox_resolution.getItems().add("1920 x 1080");

            combobox_screen_mode.getItems().add("Fullscreen");
            combobox_screen_mode.getItems().add("Windowed Fullscreen");
            combobox_screen_mode.getItems().add("Windowed");

            combobox_resolution.setValue("1280 x 720");
            combobox_screen_mode.setValue("Windowed");
        });

        combobox_screen_mode.setOnAction((event)->{
            onScreenModeChanged();
        });
        combobox_resolution.setOnAction((event)->{
            onResolutionChanged();
        });
    }

    public void onResolutionChanged() {
        switch((String)combobox_resolution.getValue()){
            case "1280 x 720":
                IOGameSettings.width = 1280;
                IOGameSettings.height = 720;
                break;
            case "1366 x 768":
                IOGameSettings.width = 1366;
                IOGameSettings.height = 768;
                break;
            case "1920 x 1080-Fullscreen":
                IOGameSettings.width = 1920;
                IOGameSettings.height = 1080;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + combobox_screen_mode.getValue());
        }
        has_applied_changes = false;
    }
    public void onScreenModeChanged() {
        switch((String)combobox_screen_mode.getValue()){
            case "Fullscreen":
                IOGameSettings.screenMode = IOGameSettings.ScreenMode.FULLSCREEN;
                combobox_resolution.setDisable(true);
                break;
            case "Windowed":
                IOGameSettings.screenMode = IOGameSettings.ScreenMode.WINDOW_DEFAULT;
                break;
            case "Windowed-Fullscreen":
                IOGameSettings.screenMode = IOGameSettings.ScreenMode.WINDOW_FULLSCREEN;
                combobox_resolution.setDisable(true);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + combobox_screen_mode.getValue());
        }
        has_applied_changes = false;
    }

    @FXML
    private void onApply(){
        has_applied_changes = true;

    }
}
