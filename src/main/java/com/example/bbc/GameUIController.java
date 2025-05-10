package com.example.bbc;

import configs.DimensionConfig;
import entities.TankEntity;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import static com.example.bbc.IOGame.SERVER_API;

public class GameUIController {
    public AnchorPane root;
    public ProgressBar upgradesHealthBar, upgradesSpeedBar, upgradesDamageBar;
    public Pane minimapPane;
    public ListView<String> lvDeathLogs;
    public static ObservableList<String> death_messages = FXCollections.observableArrayList();
    public Label playersLeftLabel;
    public Label scoreLabel;
    public ScrollPane scpDeathLogs;

    public static IntegerProperty xp = new SimpleIntegerProperty(10);      //using IntegerProperty allows labels to react to changes to the integer
    public static IntegerProperty health = new SimpleIntegerProperty(100);
    public static IntegerProperty speed = new SimpleIntegerProperty(5);
    public static IntegerProperty damage = new SimpleIntegerProperty(10);

    public Label lblHealth;
    public Label lblSpeed;
    public Label lblDamage;

    BooleanProperty upgrade_buttons_enabled = new SimpleBooleanProperty(false);   //initial state for upgrade buttons

    public Button btnUpgradeBulletDamage, btnUpgradeSpeed, btnUpgradeHealth;
    @FXML private ProgressBar healthBar;
    @FXML private VBox debugPanel;

    boolean speed_is_upgraded = false;
    boolean health_is_upgraded = false;
    boolean damage_is_upgraded = false;

    boolean is_deathlog_zoomed = false;

    private Circle player;

    public void updatePlayersLeftCounter(int count){
        String text = "Players Left: " + count;
        playersLeftLabel.setText(text);
    }

    public void updateScore(long score){
        String text = "Score: " + score;
        scoreLabel.setText(text);
    }

    public void refreshDeathMessages(){
        Platform.runLater(() -> {
            lvDeathLogs.setItems(death_messages);
        });
    }

    public static void addMessage(String message) {
        Platform.runLater(() -> {
            death_messages.addFirst(message);
        });
    }

    private void statButtonsVisibility() {
        Platform.runLater(() -> {
            btnUpgradeHealth.disableProperty().bind(upgrade_buttons_enabled.not());   //binds to changes to upgradeButtonsEnabled variable
            btnUpgradeSpeed.disableProperty().bind(upgrade_buttons_enabled.not());
            btnUpgradeBulletDamage.disableProperty().bind(upgrade_buttons_enabled.not());
        });
    }

    public void initialize() {
        player = new Circle();
        SERVER_API.onContainsUpgrades(() -> {
            upgrade_buttons_enabled.set(true);
            upgradesHealthBar.setProgress(1);
            upgradesSpeedBar.setProgress(1);
            upgradesDamageBar.setProgress(1);
        });

        SERVER_API.onNoUpgrades(() -> {
            upgrade_buttons_enabled.set(false);
        }); //sets condition for when buttons are enabled or disabled

        death_messages.addFirst("");
        lvDeathLogs.setItems(death_messages);

        statButtonsVisibility();

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
        player.setFill(Paint.valueOf("#000000"));
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

    public void resetValues(){
        lblHealth.setText("100");
        lblSpeed.setText("100");
        lblDamage.setText("500");
    }

    public void onUpgradeHealth(ActionEvent actionEvent) {
        int hp = Integer.parseInt(lblHealth.getText());
        lblHealth.setText("" + (hp + 50));
        upgradesHealthBar.setProgress(0.84);
        upgradesSpeedBar.setProgress(0.84);
        upgradesDamageBar.setProgress(0.84);
        SERVER_API.upgradeHealth();
    }

    public void onUpgradeSpeed(ActionEvent actionEvent) {
        int speed = Integer.parseInt(lblSpeed.getText());
        lblSpeed.setText("" + (speed + 50));
        upgradesHealthBar.setProgress(0.84);
        upgradesSpeedBar.setProgress(0.84);
        upgradesDamageBar.setProgress(0.84);
        SERVER_API.upgradeSpeed();
    }

    public void onUpgradeDamage(ActionEvent actionEvent) {
        int damage = Integer.parseInt(lblDamage.getText());
        lblDamage.setText((damage + 100) + "");
        upgradesHealthBar.setProgress(0.84);
        upgradesSpeedBar.setProgress(0.84);
        upgradesDamageBar.setProgress(0.84);
        SERVER_API.upgradeDamage();
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

    public void zoomDeathLogs(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.T){
            if(is_deathlog_zoomed){
                scpDeathLogs.setPrefWidth(200);
                lvDeathLogs.setPrefWidth(200);
                is_deathlog_zoomed = false;
                lvDeathLogs.setOpacity(0.65);
                scpDeathLogs.setOpacity(0.65);

            } else{
                scpDeathLogs.setPrefWidth(450);
                lvDeathLogs.setPrefWidth(450);
                is_deathlog_zoomed = true;
                lvDeathLogs.setOpacity(1.0);
                scpDeathLogs.setOpacity(1.0);
            }
        }
    }
}