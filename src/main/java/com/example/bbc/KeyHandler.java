package com.example.bbc;

import datas.InputData;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static com.example.bbc.IOGame.SERVER_API;

public class KeyHandler implements EventHandler<KeyEvent> {
    public boolean up_pressed = false;
    public boolean down_pressed = false;
    public boolean left_pressed = false;
    public boolean right_pressed = false;
    public boolean lShift_pressed = false;

    public boolean one_pressed = false;
    public boolean two_pressed = false;
    public boolean three_pressed = false;
    public boolean four_pressed = false;
    public boolean five_pressed = false;
    public boolean f_pressed = false;

    public boolean esc_pressed = false;

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) keyPressed(event);
        else if (event.getEventType() == KeyEvent.KEY_RELEASED) keyReleased(event);
    }

    public void keyPressed(KeyEvent e){

        KeyCode code = e.getCode();
        switch (code){
            case W -> {
                up_pressed = true;
            }
            case S -> {
                down_pressed = true;
            }
            case A -> {
                left_pressed = true;
            }
            case D -> {
                right_pressed = true;
            }
            case SHIFT -> {
                lShift_pressed = true;
            }
            case DIGIT1 -> one_pressed = true;
            case DIGIT2 -> two_pressed = true;
            case DIGIT3 -> three_pressed = true;
            case DIGIT4 -> four_pressed = true;
            case DIGIT5 -> five_pressed = true;
            case F -> f_pressed = true;
            case ESCAPE -> esc_pressed = false;
        }
    }

    public void keyReleased(KeyEvent e){

        KeyCode code = e.getCode();
        switch (code){
            case W -> {
                up_pressed = false;
            }
            case S -> {
                down_pressed = false;
            }
            case A -> {
                left_pressed = false;
            }
            case D -> {
                right_pressed = false;
            }
            case SHIFT -> {
                lShift_pressed = false;
            }
            case F -> f_pressed = false;
            case ESCAPE -> esc_pressed = false;
        }
    }

    public void refreshHotbarKeys(){
        one_pressed = false;
        two_pressed = false;
        three_pressed = false;
        four_pressed = false;
        five_pressed = false;
    }
    public boolean movementKeysPressed(){
        return up_pressed || down_pressed || left_pressed || right_pressed;
    }

}
