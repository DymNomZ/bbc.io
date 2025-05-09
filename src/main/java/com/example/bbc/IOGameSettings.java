package com.example.bbc;

import java.io.Serializable;

public class IOGameSettings implements Serializable {

    public static ScreenMode screenMode = ScreenMode.WINDOW_DEFAULT;
    public static double width = 1280;
    public static double height = 720;
    public static boolean show_player_names = true;

    enum ScreenMode implements Serializable {
        FULLSCREEN,
        WINDOW_FULLSCREEN,
        WINDOW_DEFAULT
    }

}
