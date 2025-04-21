package GameData;

public class Vector {
    double x,y;
    Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    Vector add(Vector v) {
        return new Vector(this.x + v.x, this.y + v.y);
    }

    Vector subtract(Vector v) {
        return new Vector(this.x - v.x, this.y - v.y);
    }

    Vector multiply(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar);
    }

    double dot(Vector v) {
        return this.x * v.x + this.y * v.y;
    }

    double magnitudeSquared() {
        return this.x * this.x + this.y * this.y;
    }
    public double magnitude() {
        double sum = x * x + y * y;
        return Math.sqrt(sum);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public void setMag(double newMag) {
        double currentMag = magnitude();
        if (currentMag == 0) {
            // Can't scale a zero vector
            return;
        }
        x = (x / currentMag) * newMag;
        y = (y / currentMag) * newMag;
    }


    public Vector divide(double d) {
        return new Vector(this.x / d, this.y / d);
    }
    public Vector copy(){
        return new Vector(this.x, this.y);
    }
    public Vector normalize() {
        double mag = magnitude();
        if (mag == 0) {
            return new Vector(0, 0); // Or throw an error, depending on your use case
        }
        return new Vector(x / mag, y / mag);
    }
}
