package com.example.bbc;

import classes.Sprites;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static com.example.bbc.IOGame.getMainController;

public class SettingsController {
    public ComboBox combobox_screen_mode;
    public ToggleButton toggle_player_names;
    public Button btn_apply;
    public Button btn_back;
    private boolean has_applied_changes = true;

    public void initialize() {
        btn_back.setGraphic(Sprites.Buttons.BACK_TITLE_BUTTON);
        btn_back.setFocusTraversable(false);

        IOGameSettings settings = IOGameSettings.getInstance();

        Platform.runLater(() -> {

            combobox_screen_mode.getItems().add("Fullscreen");
            combobox_screen_mode.getItems().add("Windowed");

            combobox_screen_mode.setValue("Windowed");
            if(settings.is_fullscreen) combobox_screen_mode.setValue("Fullscreen");
            else combobox_screen_mode.setValue("Windowed");

        });

        combobox_screen_mode.setOnAction((event)->{
            has_applied_changes = false;
        });
//        btn_back.setOnAction((event)->{
//            getMainController().switchView("title-scene.fxml");
//        });
    }


    @FXML
    private void onApply(){
        IOGameSettings instance = IOGameSettings.getInstance();
        has_applied_changes = true;
        switch((String)combobox_screen_mode.getValue()){
            case "Fullscreen":
                instance.is_fullscreen = true;
                break;
            case "Windowed":
                instance.is_fullscreen = false;
                break;
        }
        instance.show_player_names = toggle_player_names.isSelected();

        IOGame.applySettings();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("settings.ser"));
            oos.writeObject(instance);
            oos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void onTitleClicked(ActionEvent actionEvent) {
        Platform.runLater(()->{
            getMainController().switchView("title-scene.fxml");
        });
    }
}
