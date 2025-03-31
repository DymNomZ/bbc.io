package com.example.bbc;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameUIController {
    public Button TEMP_manual_add_xp;  //for visuals only, to be deleted
    public Label TEMP_lbl_health, TEMP_lbl_speed, TEMP_lbl_damage, TEMP_lbl_xp;  //for visuals only, to be deleted
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
}