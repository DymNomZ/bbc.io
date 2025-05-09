package com.example.bbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class IOGameSettings implements Serializable {

    private static IOGameSettings instance;

    public boolean is_fullscreen = false;
    public boolean show_player_names = true;


    public static IOGameSettings getInstance() {
        if (instance == null) {
            instance = new IOGameSettings();
        }
        return instance;
    }

    public static void setInstance() {
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("settings.ser"));
            instance = (IOGameSettings) (ois.readObject());
            ois.close();
        } catch (IOException | ClassNotFoundException e){
            instance = new IOGameSettings();
            System.out.println(e.getClass());
        }
    }

    private IOGameSettings() {}


}
