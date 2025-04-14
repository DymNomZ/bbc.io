package classes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprites {

    public static class Buttons {

        static final Image PLAY_BUTTON_SPRITE;

        public static final ImageView PLAY_BUTTON;

        static {

            PLAY_BUTTON_SPRITE = new Image("file:src/main/java/assets/buttons/temp_btn.jpg");
            PLAY_BUTTON = new ImageView(PLAY_BUTTON_SPRITE);
        }

    }
}
