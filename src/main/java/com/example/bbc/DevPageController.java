package com.example.bbc;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.*;

import static com.example.bbc.GameScene.*;

public class DevPageController {
    public ImageView ivDymSprite, ivSethSprite, ivLanceSprite, ivZillionSprite, ivRaymondSprite;
    public AnchorPane apDevScreen;
    public VBox vDym, vSeth, vLance, vZillion, vRaymond;
    public Pane paneDym;

    public void initialize(){
        Image dymSprite = new Image("file:src/main/java/assets/dymier_left_F0-resized.png");
//        vDym.setMinHeight(Double.MIN_VALUE);
//        vDym.setMinWidth(Double.MIN_VALUE);
        vDym.setMaxHeight(Double.MAX_VALUE);
        vDym.setMaxWidth(Double.MAX_VALUE);
        vDym.setAlignment(Pos.CENTER);
        ivDymSprite.setImage(dymSprite);
        ivDymSprite.setSmooth(false);
        ivDymSprite.setPreserveRatio(true);
//        paneDym.setMaxWidth(5000.0);
//        paneDym.setMaxHeight(5000.0);
        ivDymSprite.fitHeightProperty().bind(vDym.heightProperty());
        ivDymSprite.fitWidthProperty().bind(vDym.widthProperty());
        //add pane and store IV inside
        Image sethSprite = new Image("file:src/main/java/assets/seth_left_F0-resized.png");
        vSeth.setMinHeight(Double.MIN_VALUE);
        vSeth.setMinWidth(Double.MIN_VALUE);
        vSeth.setMaxHeight(Double.MAX_VALUE);
        vSeth.setMaxWidth(Double.MAX_VALUE);
        ivSethSprite.setImage(sethSprite);
        ivSethSprite.setSmooth(false);
        ivSethSprite.setPreserveRatio(true);
        ivSethSprite.fitHeightProperty().bind(vSeth.heightProperty());
        ivSethSprite.fitWidthProperty().bind(vSeth.widthProperty());
        Image lanceSprite = new Image("file:src/main/java/assets/lance_left_F0-resized.png");
        vLance.setMinHeight(Double.MIN_VALUE);
        vLance.setMinWidth(Double.MIN_VALUE);
        vLance.setMaxHeight(Double.MAX_VALUE);
        vLance.setMaxWidth(Double.MAX_VALUE);
        ivLanceSprite.setImage(lanceSprite);
        ivLanceSprite.setSmooth(false);
        ivLanceSprite.setPreserveRatio(true);
        ivLanceSprite.fitHeightProperty().bind(vLance.heightProperty());
        ivLanceSprite.fitWidthProperty().bind(vLance.widthProperty());
        Image zillionSprite = new Image("file:src/main/java/assets/zillion_left_F0-resized.png");
        vZillion.setMinHeight(Double.MIN_VALUE);
        vZillion.setMinWidth(Double.MIN_VALUE);
        vZillion.setMaxHeight(Double.MAX_VALUE);
        vZillion.setMaxWidth(Double.MAX_VALUE);
        ivZillionSprite.setImage(zillionSprite);
        ivZillionSprite.setSmooth(false);
        ivZillionSprite.setPreserveRatio(true);
        ivZillionSprite.fitHeightProperty().bind(vZillion.heightProperty());
        ivZillionSprite.fitWidthProperty().bind(vZillion.widthProperty());
        Image raymondSprite = new Image("file:src/main/java/assets/raymond_left_F0-resized.png");
        vRaymond.setMinHeight(Double.MIN_VALUE);
        vRaymond.setMinWidth(Double.MIN_VALUE);
        vRaymond.setMaxHeight(Double.MAX_VALUE);
        vRaymond.setMaxWidth(Double.MAX_VALUE);
        ivRaymondSprite.setImage(raymondSprite);
        ivRaymondSprite.setSmooth(false);
        ivRaymondSprite.setPreserveRatio(true);
        ivRaymondSprite.fitHeightProperty().bind(vRaymond.heightProperty());
        ivRaymondSprite.fitWidthProperty().bind(vRaymond.widthProperty());
    }
}
