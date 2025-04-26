package com.example.bbc;

import classes.PictureMaker;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DevPageController {
    public ImageView ivDymSprite, ivSethSprite, ivLanceSprite, ivZillionSprite, ivRaymondSprite, ivDevTitle;
    public AnchorPane apDevScreen;
    public VBox vDym, vSeth, vLance, vZillion, vRaymond, vDevTitle;

    public void initialize(){
        new PictureMaker(ivDymSprite, vDym, "dymier", apDevScreen, true, 1264);
        new PictureMaker(ivSethSprite, vSeth, "seth", apDevScreen, true, 1264);
        new PictureMaker(ivLanceSprite, vLance, "lance", apDevScreen, true, 1264);
        new PictureMaker(ivZillionSprite, vZillion, "zillion", apDevScreen, true, 1264);
        new PictureMaker(ivRaymondSprite, vRaymond, "raymond", apDevScreen, true, 1264);
        new PictureMaker(ivDevTitle, vDevTitle, "titles/dev_title", apDevScreen, false, 1392);
    }
}