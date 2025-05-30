package entities;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TankEntity extends Entity {

    Circle main_body;
    Rectangle turret;

    public String player_name;

    public double health;
    public double max_health;

    // constructor for base colors
    // - Lance
    public TankEntity() {
        this(Color.BLUE, Color.RED, Color.BLACK,100,100,"");
    }
    
    // adjustments for user-chosen color options to apply
    public TankEntity(Paint bodyColor, Paint barrelColor, Paint borderColor, double health, double max_health, String name) {
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setFill(bodyColor);
        circle.setStrokeWidth(2.0);
        circle.setStroke(borderColor);

        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(10);
        rectangle.setWidth(50);
        rectangle.setFill(barrelColor);
        rectangle.setStroke(borderColor);
        rectangle.setStrokeWidth(2.0);

        this.player_name = name;

        this.health = health;
        this.max_health = max_health;

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
    
    public void setBarrelColor(Paint color) {
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
