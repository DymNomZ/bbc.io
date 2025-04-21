package GameData;


public class QuadRectangle {
    double x,y,w,h;
    public QuadRectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    public boolean canContain(GameObject object) {
        return x + w >= object.x
                && y + h >= object.y
                && object.x >= x
                && object.y >= y;
    }


    public boolean inRange(RangeCircle circle) {
        double closestX = Math.max(circle.x, Math.min(x, x + w));
        double closestY = Math.max(circle.y, Math.min(y, y + h));
        double dx = circle.x - closestX;
        double dy = circle.y - closestY;
        return (dx * dx + dy * dy) < (circle.radius * circle.radius);
    }

    @Override
    public String toString() {
        return "QuadRectangle{" +
                "x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                '}';
    }
}
