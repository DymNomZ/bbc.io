//package com.example.bbc;
//
//import javafx.event.EventHandler;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
//
//import java.awt.*;
//
//public class MouseHandler implements EventHandler<MouseEvent> {
//    int mouse_x;
//    int mouse_y;
//    Point mouse_location_on_screen;
//
//    boolean left_is_pressed  = false;
//    boolean right_is_pressed = false;
//
//    @Override
//    public void handle(MouseEvent event) {
//        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) mousePressed(event);
//        else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) mouseReleased(event);
//    }
//
//    public void mouseReleased(MouseEvent e){
//        left_is_pressed = false;
//        right_is_pressed = false;
//    }
//
//    public void mousePressed(MouseEvent e){
//
//        MouseButton mouseButton = e.getButton();
//
//        switch (mouseButton) {
//            case PRIMARY -> left_is_pressed = true;
//            case SECONDARY -> right_is_pressed = true;
//        }
//        //handle button dynamic GUI
//        handleButtons(e);
//
//    }
//}
