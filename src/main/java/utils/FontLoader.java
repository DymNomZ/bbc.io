package utils;

import javafx.scene.text.Font;

public class FontLoader {
    public static void loadGameFonts() {
        Font coolvetica = Font.loadFont(FontLoader.class.getResourceAsStream("/com/example/bbc/fonts/Coolvetica-Regular.otf"), 0);
        if (coolvetica != null) {
            System.out.println("Loaded font: " + coolvetica.getFamily());
        } else {
            System.out.println("Font not found");
        }

        Font lemon_milk = Font.loadFont(FontLoader.class.getResourceAsStream("/com/example/bbc/fonts/LEMONMILK.otf"), 0);
        if (lemon_milk != null) {
            System.out.println("Loaded font: " + lemon_milk.getFamily());
        } else {
            System.out.println("Font not found");
        }
        // Load other fonts...
    }
}
