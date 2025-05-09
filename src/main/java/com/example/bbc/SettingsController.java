package com.example.bbc;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;

public class SettingsController {
    public ComboBox combobox_resolution;
    public ComboBox combobox_screen_mode;
    public ToggleButton toggle_player_names;

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
    }
}
