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

        rectangle.setTranslateY(0 - rectangle.getHeight() / 2);

        entityGroup.getChildren().addAll(rectangle, circle);
    }
}
