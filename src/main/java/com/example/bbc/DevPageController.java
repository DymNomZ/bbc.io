package com.example.bbc;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DevPageController {
    public ImageView ivDymSprite, ivSethSprite, ivLanceSprite, ivZillionSprite, ivRaymondSprite;
    public AnchorPane apDevScreen;
    public VBox vDym, vSeth, vLance, vZillion, vRaymond;

    public void initialize(){
        new SpriteMaker(ivDymSprite, vDym, "dymier", apDevScreen);
        new SpriteMaker(ivSethSprite, vSeth, "seth", apDevScreen);
        new SpriteMaker(ivLanceSprite, vLance, "lance", apDevScreen);
        new SpriteMaker(ivZillionSprite, vZillion, "zillion", apDevScreen);
        new SpriteMaker(ivRaymondSprite, vRaymond, "raymond", apDevScreen);
    }
}