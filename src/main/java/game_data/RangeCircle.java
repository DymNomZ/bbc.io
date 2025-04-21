package game_data;

import server.model.ServerEntity;

public class RangeCircle {
    double x,y,radius;
    public RangeCircle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    public boolean canContain(ServerEntity entity) {
        return this.distance(entity) <= this.radius + entity.radius;
    }
    public double distance(ServerEntity other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
