package com.example.bbc;

import classes.Sprites;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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

    public void test(){
        System.out.println("lol");
    }
}
