package com.example.bbc;

import classes.PictureMaker;
import classes.Sprites;
import datas.EntityData;
import datas.GameData;
import datas.LobbyData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utils.Logging;

import java.io.IOException;
import java.util.List;

import static com.example.bbc.IOGame.MAIN_STAGE;
import static com.example.bbc.IOGame.SERVER_API;
import static utils.Scenes.*;

public class TitleScreenController {
    public ImageView titleImageView;
    public TextField playerNameTF;
    public Button playBtn;
    public Button aboutDevsBtn;
    public Label lblEmptyWarning;
    public AnchorPane apTitleScreen;
    public VBox vTitle;

    public void initialize(){
//        new PictureMaker(titleImageView, vTitle, "titles/title", apTitleScreen, false, 1400);

        String style = "-fx-background-color: transparent;" +
                "-fx-padding: 0;" +
                "-fx-background-radius: 0;";



        playBtn.setGraphic(Sprites.Buttons.PLAY_BUTTON);
        playBtn.setFocusTraversable(false);
        playBtn.setStyle(style);

        aboutDevsBtn.setGraphic(Sprites.Buttons.ABOUT_DEVS);
        aboutDevsBtn.setFocusTraversable(false);
        aboutDevsBtn.setStyle(style);

        titleImageView.setImage(Sprites.Titles.TITLE.getImage());
        titleImageView.setFitWidth(500);
        titleImageView.setFitHeight(300);
        titleImageView.setPreserveRatio(true);
    }

    public void onPlay(ActionEvent event){

        if(playerNameTF.getText().isEmpty()){
            //TODO: Improve
//            playerNameTF.setText("Must enter playername!");
            lblEmptyWarning.setText("Must enter playername!");
            return;
        }

        SERVER_API = new ServerHandler(playerNameTF.getText());

        SERVER_API.onConnected(new ServerDataListener<LobbyData>() {
            @Override
            public void run(LobbyData data) {

                Logging.write(this, "Connected to Server");

                //get users in lobby
                SERVER_API.users_in_lobby.addAll(data.users);

                Platform.runLater(() -> {
                    try {
                        IOGame.changeScene(event, lobbySceneFXMLResource);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Region root = (Region) IOGame.MAIN_STAGE.getScene().getRoot();
                    root.applyCss();
                    root.layout();
                    MAIN_STAGE.centerOnScreen();

                    GameScene.initializeOnGameUpdate();
                });
            }
        });
    }
}
