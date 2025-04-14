package classes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprites {

    public static class Titles {

        static final Image TITLE_SPRITE;

        public static final ImageView TITLE;

        static {

            TITLE_SPRITE = new Image("file:src/main/java/assets/titles/title.png");
            TITLE = new ImageView(TITLE_SPRITE);

        }

    }

    public static class Buttons {

        static final Image PLAY_BUTTON_SPRITE;
        static final Image ABOUT_DEVS_BUTTON_SPRITE;

        public static final ImageView PLAY_BUTTON;
        public static final ImageView ABOUT_DEVS;

        static {

            PLAY_BUTTON_SPRITE = new Image("file:src/main/java/assets/buttons/play_button.png");
            PLAY_BUTTON = new ImageView(PLAY_BUTTON_SPRITE);
            PLAY_BUTTON.setFitWidth(200);
            PLAY_BUTTON.setFitHeight(200);
            PLAY_BUTTON.setPreserveRatio(true);


            ABOUT_DEVS_BUTTON_SPRITE = new Image("file:src/main/java/assets/buttons/about_devs_button.png");
            ABOUT_DEVS = new ImageView(ABOUT_DEVS_BUTTON_SPRITE);
            ABOUT_DEVS.setFitWidth(100);
            ABOUT_DEVS.setFitHeight(100);
            ABOUT_DEVS.setPreserveRatio(true);

        }

    }
}
