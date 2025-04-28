package classes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class PictureMaker {
    ArrayList<Image> frames;
    ImageView ivSpriteHolder;
    VBox vContainer;
    public PictureMaker(ImageView ivSpriteHolder, VBox vContainer, String name, AnchorPane apDevScreen, boolean isSprite, double divider){
        this.ivSpriteHolder = ivSpriteHolder;
        this.vContainer = vContainer;
        if(isSprite){
            this.frames = new ArrayList<>();
            this.frames.add(new Image("file:src/main/java/assets/" + name + "_left_F0-resized.png"));
            this.frames.add(new Image("file:src/main/java/assets/" + name + "_left_F1-resized.png"));
            this.frames.add(new Image("file:src/main/java/assets/" + name + "_left_F0-resized.png"));
            this.frames.add(new Image("file:src/main/java/assets/" + name + "_left_F-1-resized.png"));
            ivSpriteHolder.setImage(frames.get(0));
            AtomicInteger currentFrameIndex = new AtomicInteger();
            Timeline timeline;
            Duration frameDuration = Duration.millis(400);
            KeyFrame keyFrame = new KeyFrame(frameDuration, event -> {
                ivSpriteHolder.setImage(frames.get(currentFrameIndex.get()));
                currentFrameIndex.set((currentFrameIndex.get() + 1) % 4);
            });
            timeline = new Timeline(keyFrame);
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } else{
            Image static_img = new Image("file:src/main/java/assets/" + name + ".png");
            ivSpriteHolder.setImage(static_img);
        }
        ivSpriteHolder.setSmooth(false);
        ivSpriteHolder.setPreserveRatio(true);
        ivSpriteHolder.fitHeightProperty().bind(vContainer.heightProperty());
        ivSpriteHolder.fitWidthProperty().bind(vContainer.widthProperty());
        apDevScreen.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                System.out.println(name + " Width: " + newSceneWidth);
//                System.out.println(newSceneWidth.doubleValue()/divider);
                ivSpriteHolder.fitHeightProperty().bind(vContainer.heightProperty().divide(newSceneWidth.doubleValue()/divider));
                ivSpriteHolder.fitWidthProperty().bind(vContainer.widthProperty().divide(newSceneWidth.doubleValue()/divider));
            }
        });
    }
}