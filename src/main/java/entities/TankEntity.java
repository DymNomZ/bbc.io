package entities;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TankEntity extends Entity {

    Circle main_body;
    Rectangle turret;

    // constructor for base colors
    // - Lance
    public TankEntity() {
        this(Color.BLUE, Color.RED, Color.BLACK);
    }
    
    // adjustments for user-chosen color options to apply
    public TankEntity(Paint bodyColor, Paint turretColor, Paint borderColor) {
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setFill(bodyColor);
        circle.setStrokeWidth(2.0);
        circle.setStroke(borderColor);

        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(10);
        rectangle.setWidth(50);
        rectangle.setFill(turretColor);
        rectangle.setStroke(borderColor);
        rectangle.setStrokeWidth(2.0);

        //rectangle skews to the right initially, this aligns it to the center of the circle
        //Credits Seth - Dymes
        rectangle.setTranslateY(0 - rectangle.getHeight() / 2);

        entity_group.getChildren().addAll(rectangle, circle);
        main_body = circle;
        turret = rectangle;
    }

    public Circle getMain_body() {
        return main_body;
    }

    public Rectangle getTurret() {
        return turret;
    }

    public void setBodyColor(Paint color) {
        main_body.setFill(color);
    }
    
    public void setTurretColor(Paint color) {
        turret.setFill(color);
    }
    
    public void setBorderColor(Paint color) {
        main_body.setStroke(color);
        turret.setStroke(color);
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
