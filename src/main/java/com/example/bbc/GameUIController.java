package com.example.bbc;

import configs.DimensionConfig;
import entities.TankEntity;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

import static utils.Scenes.GAME_SCENE;

public class GameUIController {
    public AnchorPane root;
    public Button TEMPbtnDevPage;
    public Button TEMPbtnLobbyPage;
    public ProgressBar upgradesHealthBar, upgradesSpeedBar, upgradesDamageBar;
    public Pane minimapPane;
    public ListView<String> chatlog_list;

    IntegerProperty xp = new SimpleIntegerProperty(0);      //using IntegerProperty allows labels to react to changes to the integer
    IntegerProperty health = new SimpleIntegerProperty(100);
    IntegerProperty speed = new SimpleIntegerProperty(5);
    IntegerProperty damage = new SimpleIntegerProperty(10);
    BooleanProperty upgrade_buttons_enabled = new SimpleBooleanProperty(false);   //initial state for upgrade buttons

    public Button btnUpgradeBulletDamage, btnUpgradeSpeed, btnUpgradeHealth;
    @FXML private ProgressBar healthBar;
    @FXML private VBox debugPanel;


    boolean speed_is_upgraded = false;
    boolean health_is_upgraded = false;
    boolean damage_is_upgraded = false;

    private Circle player;


    public void addMessage(String message) {
        chatlog_list.getItems().add(message);
    }

    public void initialize() {
        player = new Circle();
        upgrade_buttons_enabled.set(xp.get() >= 10);  //sets condition for when buttons are enabled or disabled

        btnUpgradeHealth.disableProperty().bind(upgrade_buttons_enabled.not());   //binds to changes to upgradeButtonsEnabled variable
        btnUpgradeSpeed.disableProperty().bind(upgrade_buttons_enabled.not());
        btnUpgradeBulletDamage.disableProperty().bind(upgrade_buttons_enabled.not());

        if(!upgrade_buttons_enabled.get()){
            upgradesHealthBar.setProgress(0.84);
            upgradesSpeedBar.setProgress(0.84);
            upgradesDamageBar.setProgress(0.84);
        } else{
            upgradesHealthBar.setProgress(1);
            upgradesSpeedBar.setProgress(1);
            upgradesDamageBar.setProgress(1);
        }
    }


    public void setPlayer(TankEntity player_reference){
        if(player_reference == null){
            return ;
        }
        player.setFill(player_reference.getMain_body().getFill());
        player.setRadius(3);

        double minimap_x = (player_reference.pos_x / DimensionConfig.MAP_WIDTH) * minimapPane.getWidth();
        double minimap_y = (player_reference.pos_y / DimensionConfig.MAP_HEIGHT) * minimapPane.getHeight();

        player.setLayoutX(minimap_x);
        player.setLayoutY(minimap_y);

        Platform.runLater(()->{
            minimapPane.getChildren().clear();
            minimapPane.getChildren().add(player);
        });
    }
    
    public void toggleDebugInfo() {
        debugPanel.setVisible(!debugPanel.isVisible());
    }

    public void onUpgradeHealth(ActionEvent actionEvent) {
        health.set(health.get() + 5);
        xp.set(xp.get() - 10);
        upgrade_buttons_enabled.set(xp.get() >= 10);
        if(!upgrade_buttons_enabled.get()){
            upgradesHealthBar.setProgress(0.84);
            upgradesSpeedBar.setProgress(0.84);
            upgradesDamageBar.setProgress(0.84);
        } else{
            upgradesHealthBar.setProgress(1);
            upgradesSpeedBar.setProgress(1);
            upgradesDamageBar.setProgress(1);
        }
        health_is_upgraded = true;
    }


    public void onUpgradeSpeed(ActionEvent actionEvent) {
        speed.set(speed.get() + 3);
        xp.set(xp.get() - 10);
        upgrade_buttons_enabled.set(xp.get() >= 10);
        if(!upgrade_buttons_enabled.get()){
            upgradesHealthBar.setProgress(0.84);
            upgradesSpeedBar.setProgress(0.84);
            upgradesDamageBar.setProgress(0.84);
        } else{
            upgradesHealthBar.setProgress(1);
            upgradesSpeedBar.setProgress(1);
            upgradesDamageBar.setProgress(1);
        }
        speed_is_upgraded = true;
    }

    public void onUpgradeDamage(ActionEvent actionEvent) {
        damage.set(damage.get() + 5);
        xp.set(xp.get() - 10);
        upgrade_buttons_enabled.set(xp.get() >= 10);
        if(!upgrade_buttons_enabled.get()){
            upgradesHealthBar.setProgress(0.84);
            upgradesSpeedBar.setProgress(0.84);
            upgradesDamageBar.setProgress(0.84);
        } else{
            upgradesHealthBar.setProgress(1);
            upgradesSpeedBar.setProgress(1);
            upgradesDamageBar.setProgress(1);
        }
        damage_is_upgraded = true;
    }

    public void TEMP_manualAddXP(ActionEvent actionEvent) {
        xp.set(xp.get() + 5);
        upgrade_buttons_enabled.set(xp.get() >= 10);
        if(!upgrade_buttons_enabled.get()){
            upgradesHealthBar.setProgress(0.84);
            upgradesSpeedBar.setProgress(0.84);
            upgradesDamageBar.setProgress(0.84);
        } else{
            upgradesHealthBar.setProgress(1);
            upgradesSpeedBar.setProgress(1);
            upgradesDamageBar.setProgress(1);
        }
    }

    public void handleKeyPressed(KeyEvent e) {

        KeyCode code = e.getCode();

        switch (code){
            case R -> resetProgressBar();
            case F -> decreaseProgressBar();
            case X -> emptyProgressBar();
        }
    }

    public void decreaseProgressBar() {
        if (healthBar != null) {
            // Decrease by 10%, ensure it doesn't go below 0
            healthBar.setProgress(Math.max(0, healthBar.getProgress() - 0.1));
            //TODO: Damage logic
        }
    }

    public void setProgressBar(double progress, double max){
        if (healthBar != null) {
            // Decrease by 10%, ensure it doesn't go below 0
            healthBar.setProgress(Math.max(0, max / progress));
            //TODO: Damage logic
        }
    }

    public void emptyProgressBar() {
        if (healthBar != null) {
            healthBar.setProgress(0);
            //TODO: Death logic
        }
    }

    public void resetProgressBar() {
        if (healthBar != null) {
            healthBar.setProgress(1.0); // Reset to full
            //TODO: Respawn logic
        }
    }

//    public void TEMP_devPage(ActionEvent actionEvent) throws IOException {
////        IOGame.MAIN_STAGE.setScene(Scenes.DEVS_SCENE);
//        FXMLLoader fxmlLoader = new FXMLLoader(IOGame.class.getResource("dev-page.fxml"));
//        Stage stage = (Stage) TEMPbtnDevPage.getScene().getWindow();
//        stage.setScene(new Scene(fxmlLoader.load(), 1280, 720));
//        stage.setTitle("Dev Screen");
//        stage.setScene(stage.getScene());
//        stage.show();
//    }
//
//    public void TEMP_lobbyPage(ActionEvent actionEvent) throws IOException {
////        IOGame.MAIN_STAGE.setScene(Scenes.DEVS_SCENE);
//        FXMLLoader fxmlLoader = new FXMLLoader(IOGame.class.getResource("game-lobby-ui.fxml"));
//        Stage stage = (Stage) TEMPbtnLobbyPage.getScene().getWindow();
//        stage.setScene(new Scene(fxmlLoader.load(), 1280, 720));
//        stage.setTitle("Lobby Screen");
//        stage.setScene(stage.getScene());
//        stage.show();
//    }
}