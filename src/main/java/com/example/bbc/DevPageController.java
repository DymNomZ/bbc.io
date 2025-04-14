package com.example.bbc;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
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

    public void initialize(){
        Image dymSprite = new Image("file:src/main/java/assets/dymier_left_F0-resized.png");
        ivDymSprite.setImage(dymSprite);
        ivDymSprite.setSmooth(false);
        ivDymSprite.setPreserveRatio(true);
        ivDymSprite.fitHeightProperty().bind(vDym.heightProperty());
        ivDymSprite.fitWidthProperty().bind(vDym.widthProperty());
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
        apDevScreen.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth + "\tDivider: " + newSceneWidth.doubleValue()/1000);
                ivDymSprite.fitHeightProperty().bind(vDym.heightProperty().divide(newSceneWidth.intValue()/1264));
                ivDymSprite.fitWidthProperty().bind(vDym.widthProperty().divide(newSceneWidth.intValue()/681));
                ivSethSprite.fitHeightProperty().bind(vSeth.heightProperty().divide(newSceneWidth.intValue()/1264));
                ivSethSprite.fitWidthProperty().bind(vSeth.widthProperty().divide(newSceneWidth.intValue()/681));
                ivLanceSprite.fitHeightProperty().bind(vLance.heightProperty().divide(newSceneWidth.intValue()/1264));
                ivLanceSprite.fitWidthProperty().bind(vLance.widthProperty().divide(newSceneWidth.intValue()/681));
                ivZillionSprite.fitHeightProperty().bind(vZillion.heightProperty().divide(newSceneWidth.intValue()/1264));
                ivZillionSprite.fitWidthProperty().bind(vZillion.widthProperty().divide(newSceneWidth.intValue()/681));
                ivRaymondSprite.fitHeightProperty().bind(vRaymond.heightProperty().divide(newSceneWidth.intValue()/1264));
                ivRaymondSprite.fitWidthProperty().bind(vRaymond.widthProperty().divide(newSceneWidth.intValue()/681));
            }
        });
        apDevScreen.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
            }
        });
    }
}
