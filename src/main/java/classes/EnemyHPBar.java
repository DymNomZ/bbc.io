package classes;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EnemyHPBar {
    private Rectangle border;
    private Rectangle bar;

    public Group group;

    public EnemyHPBar(double progress,double max) {
        border = new Rectangle();
        border.setWidth(104);
        border.setHeight(14);
        border.setFill(Color.TRANSPARENT); // Transparent fill inside
        border.setStroke(Color.DARKGRAY);  // Border color
        border.setStrokeWidth(2);

        bar = new Rectangle();
        bar.setWidth((progress / max) * 100);
        bar.setHeight(10);
        bar.setFill(Color.RED);
        bar.setX(2);
        bar.setY(2);

        group = new Group();
        group.getChildren().add(border);
        group.getChildren().add(bar);
    }

    public void setPosition(double x, double y){
        group.setTranslateX(x);
        group.setTranslateY(y);
    }

}
