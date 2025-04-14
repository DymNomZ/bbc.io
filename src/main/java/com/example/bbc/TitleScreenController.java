package com.example.bbc;

import classes.Sprites;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class TitleScreenController {
    public ImageView titleImageView;
    public TextField playerNameTF;
    public Button playBtn;

    public void initialize(){

        playBtn.setGraphic(Sprites.Buttons.PLAY_BUTTON);
    }
}
