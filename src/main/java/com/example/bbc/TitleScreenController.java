package com.example.bbc;

import classes.Sprites;
import datas.EntityData;
import datas.GameData;
import datas.LobbyData;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import utils.Logging;

import java.util.List;

import static com.example.bbc.IOGame.MAIN_STAGE;
import static com.example.bbc.IOGame.SERVER_API;
import static utils.Scenes.LOBBY_SCENE;

public class TitleScreenController {
    public ImageView titleImageView;
    public TextField playerNameTF;
    public Button playBtn;
    public Button aboutDevsBtn;

    public void initialize(){

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

    public void onPlay(){

        if(playerNameTF.getText().isEmpty()){
            //TODO: Improve
            playerNameTF.setText("Must enter playername!");
            return;
        }

        SERVER_API = new ServerHandler(playerNameTF.getText());

        SERVER_API.onConnected(new ServerDataListener<LobbyData>() {
            @Override
            public void run(LobbyData data) {

                Logging.write(this, "Connected to Server");
                Platform.runLater(() -> {
                    MAIN_STAGE.setScene(LOBBY_SCENE);
                });
            }

        });

    }
}
