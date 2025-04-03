package entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TankEntity extends Entity {


    public TankEntity() {
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setFill(Color.BLUE);
        circle.setStrokeWidth(2.0);
        circle.setStroke(Color.BLACK);

        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(10);
        rectangle.setWidth(50);
        rectangle.setFill(Color.RED);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2.0);

        //rectangle skews to the right initially, this aligns it to the center of the circle
        //Credits Seth - Dymes
        rectangle.setTranslateY(0 - rectangle.getHeight() / 2);

        entity_group.getChildren().addAll(rectangle, circle);
    }
    public void move(){
        int speed = 1;

        if(key_handler.up_pressed) pos_y -= speed;
        else if(key_handler.down_pressed) pos_y += speed;

        else if(key_handler.left_pressed) pos_x -= speed;
        else if(key_handler.right_pressed) pos_x += speed;

        System.out.println("Sending new player data: " + pos_x + "," + pos_y);

        //TODO: Send player position data to server

    }
}
