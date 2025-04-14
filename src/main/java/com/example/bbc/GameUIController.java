package com.example.bbc;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Scenes;

import java.io.IOException;

public class GameUIController {
    public Button TEMP_manual_add_xp;  //for visuals only, to be deleted
    public Label TEMP_lbl_health, TEMP_lbl_speed, TEMP_lbl_damage, TEMP_lbl_xp;  //for visuals only, to be deleted
    public AnchorPane root;
    public Button TEMPbtnDevPage;
    public Button TEMPbtnLobbyPage;
    IntegerProperty xp = new SimpleIntegerProperty(0);      //using IntegerProperty allows labels to react to changes to the integer
    IntegerProperty health = new SimpleIntegerProperty(100);
    IntegerProperty speed = new SimpleIntegerProperty(5);
    IntegerProperty damage = new SimpleIntegerProperty(10);
    BooleanProperty upgrade_buttons_enabled = new SimpleBooleanProperty(false);   //initial state for upgrade buttons

    public Button btnUpgradeBulletDamage, btnUpgradeSpeed, btnUpgradeHealth;
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
        TEMP_lbl_health.textProperty().bind(health.asString());   //binds to all changes to health variable
        TEMP_lbl_speed.textProperty().bind(speed.asString());
        TEMP_lbl_damage.textProperty().bind(damage.asString());
        TEMP_lbl_xp.textProperty().bind(xp.asString());

        upgrade_buttons_enabled.set(xp.get() >= 10);  //sets condition for when buttons are enabled or disabled

        btnUpgradeHealth.disableProperty().bind(upgrade_buttons_enabled.not());   //binds to changes to upgradeButtonsEnabled variable
        btnUpgradeSpeed.disableProperty().bind(upgrade_buttons_enabled.not());
        btnUpgradeBulletDamage.disableProperty().bind(upgrade_buttons_enabled.not());
    }

    //FIXME: Issue #2 demonstration. Feel free to remove on the next commit.
    public void test(MouseEvent e){
        System.out.println("aaaaa");
    }

    public void test2(MouseEvent e){
        System.out.println("bbb");
    }
    
    public void toggleDebugInfo() {
        debugPanel.setVisible(!debugPanel.isVisible());
    }

    public void onUpgradeHealth(ActionEvent actionEvent) {
        health.set(health.get() + 5);
        xp.set(xp.get() - 10);
        upgrade_buttons_enabled.set(xp.get() >= 10);
    }

    public void onUpgradeSpeed(ActionEvent actionEvent) {
        speed.set(speed.get() + 3);
        xp.set(xp.get() - 10);
        upgrade_buttons_enabled.set(xp.get() >= 10);
    }

    public void onUpgradeDamage(ActionEvent actionEvent) {
        damage.set(damage.get() + 5);
        xp.set(xp.get() - 10);
        upgrade_buttons_enabled.set(xp.get() >= 10);
    }

    public void TEMP_manualAddXP(ActionEvent actionEvent) {
        xp.set(xp.get() + 5);
        upgrade_buttons_enabled.set(xp.get() >= 10);
    }

    public void handleKeyPressed(KeyEvent e) {

        KeyCode code = e.getCode();

        System.out.println(code);

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

    public void TEMP_devPage(ActionEvent actionEvent) throws IOException {
//        IOGame.MAIN_STAGE.setScene(Scenes.DEVS_SCENE);
        FXMLLoader fxmlLoader = new FXMLLoader(IOGame.class.getResource("dev-page.fxml"));
        Stage stage = (Stage) TEMPbtnDevPage.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 1280, 720));
        stage.setTitle("Dev Screen");
        stage.setScene(stage.getScene());
        stage.show();
    }

    public void TEMP_lobbyPage(ActionEvent actionEvent) throws IOException {
//        IOGame.MAIN_STAGE.setScene(Scenes.DEVS_SCENE);
        FXMLLoader fxmlLoader = new FXMLLoader(IOGame.class.getResource("game-lobby-ui.fxml"));
        Stage stage = (Stage) TEMPbtnLobbyPage.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load(), 1280, 720));
        stage.setTitle("Lobby Screen");
        stage.setScene(stage.getScene());
        stage.show();
    }
}