package classes.entities;

import classes.Asset.Sprite.Sprite;
import classes.sprites.EntitySprite;
import interfaces.EntityCollidable;

import java.awt.*;

import src.GamePanel;

/*  Super class for all entities
 *  This class differs from MapEntity by its JPanel specific methods and data
 *                                                                    - Lil Z
 */
public abstract class PanelEntity {
    /* Explanation for each variable:
     * - x and y are the position of the top-leftmost pixel of the panel
     *
     * - x_pos_center and y_pos_center as of now, no uses but can be used to hold the center
     *   point of any PanelEntity
     *
     *  - width is the number of pixels horizontally, height is the number of pixels vertically
     *  - deltaX and deltaY are used to change the x and y variables respectively
     *
     *  - buffer is used to store the sprite (might change in the future)
     *  - Set H
     */
    public int x, y, width, height;
    public int deltaX, deltaY;
    public long key;
    int speed;
    Sprite buffer;
    public boolean is_colliding = false;

    protected void setX(int x){
        this.x = x;
    }

    protected void setY(int y){
        this.y = y;
    }

    protected void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }

    protected void setWidth(int width){
        this.width = width;
    }

    protected void setHeight(int height){
        this.height = height;
    }

    protected void setDeltaX(int deltaX){
        this.deltaX = deltaX;
    }

    protected void setDeltaY(int deltaY){
        this.deltaY = deltaY;
    }

    protected void setKey(long key){
        this.key = key;
    }

    protected void setSpeed(int speed){
        this.speed = speed;
    }

    protected void setBuffer(Sprite buffer){
        this.buffer = buffer;
    }

    //DEFAULT CONSTRUCTOR
    public PanelEntity() {
        x = 0;
        y = 0;
        width = GamePanel.TILE_SIZE;
        height = GamePanel.TILE_SIZE;
        deltaX = 0;
        deltaY = 0;
        buffer = Sprite.load("default");
    }


    public void move(int offsetX, int offsetY) {
        deltaX = offsetX;
        deltaY = offsetY;
    }
    public int getTileXPosition(){
        return x / (GamePanel.TILE_SIZE);
    }
    public int getTileYPosition(){
        return y / (GamePanel.TILE_SIZE);
    }

    // move like a sigma male, disregarding all boundaries of this world
    public void moveAbsolute(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveToXandY(int x, int y, int speed) {
        double x_diff = x - this.x;
        double y_diff = y - this.y;

        double distance = Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));

        // Threshold to decide when to snap to the position - Set H
        double snapThreshold = 5.0;

        if (distance < snapThreshold) {
            // Snap directly to the target if within threshold - Set H
            this.x = x;
            this.y = y;
        } else {
            double stepX = (x_diff) / distance;
            double stepY = (y_diff) / distance;

            this.deltaX = (int) Math.round(stepX * speed);
            this.deltaY = (int) Math.round(stepY * speed);

            this.x += deltaX;
            this.y += deltaY;

            // Reset delta values if needed for other calculations
            deltaX = 0;
            deltaY = 0;
        }
    }

    public void display(Graphics g, CameraEntity cam) {
        buffer.display(g, x - cam.x, y - cam.y, width, height);
    }

    //for character selection screen
    public void display(Graphics g) {
        buffer.display(g, x, y, width, height);
    }

    //temp touching function, ikik this guy will be deprecated soon XD - Dymes
    //used in temporary attacking
    public boolean checkIfTouching(MapEntity d1){
        
        int dx = d1.x - GamePanel.TILE_SIZE;
        int dy = d1.y - GamePanel.TILE_SIZE;
        int dx2 = d1.x + (GamePanel.TILE_SIZE * 2);
        int dy2 = d1.y + (GamePanel.TILE_SIZE * 2);

        int x1 = x - GamePanel.TILE_SIZE;
        int y1 = y - GamePanel.TILE_SIZE;
        int x2 = x + (GamePanel.TILE_SIZE * 2);
        int y2 = y + (GamePanel.TILE_SIZE) * 2;
        
        return !(dy > y2 || y1 > dy2) && !(dx > x2 || x1 > dx2);

    }
    /*
    * Mainly handles the EntityCollidable interface with the passed panel entity
    * - Set h
    * */
    public boolean checkEntityCollision(PanelEntity other){
        boolean is_colliding = false;

        int this_left_boundary = this.x;
        int this_top_boundary = this.y;
        int this_right_boundary = this.x + this.width;
        int this_bottom_boundary = this.y + this.height;

        int other_left_boundary = other.x;
        int other_top_boundary = other.y;
        int other_right_boundary = other.x + other.width;
        int other_bottom_boundary = other.y + other.height;

        boolean left_is_colliding =
                other_right_boundary > this_left_boundary &&
                other_right_boundary < this_right_boundary &&
                other_bottom_boundary > this_top_boundary &&
                other_top_boundary < this_bottom_boundary;

        boolean right_is_colliding =
                other_left_boundary < this_right_boundary &&
                other_left_boundary > this_left_boundary &&
                other_bottom_boundary > this_bottom_boundary &&
                other_top_boundary < this_bottom_boundary;

        boolean top_is_colliding =
                other_bottom_boundary < this_top_boundary &&
                other_bottom_boundary > this_bottom_boundary &&
                other_right_boundary > this_left_boundary &&
                other_left_boundary < this_right_boundary;

        boolean bottom_is_colliding =
                other_top_boundary < this_bottom_boundary &&
                other_top_boundary > this_top_boundary &&
                other_right_boundary > this_left_boundary &&
                other_left_boundary < this_right_boundary;

        if(this instanceof EntityCollidable){
            if(left_is_colliding){
	            ((EntityCollidable) this).onEntityLeftCollision(other);
                ((EntityCollidable) other).onEntityRightCollision(this);
            }
            if(right_is_colliding){
	            ((EntityCollidable) this).onEntityRightCollision(other);
                ((EntityCollidable) other).onEntityLeftCollision(this);
            }
            if(top_is_colliding){
	            ((EntityCollidable) this).onEntityTopCollision(other);
                ((EntityCollidable) other).onEntityBottomCollision(this);
            }
            if(bottom_is_colliding){
	            ((EntityCollidable) this).onEntityBottomCollision(other);
                ((EntityCollidable) other).onEntityTopCollision(this);
            }
            if(left_is_colliding || bottom_is_colliding || top_is_colliding || right_is_colliding){
                is_colliding = true;
	            ((EntityCollidable) this).onEntityCollision(other);
                ((EntityCollidable) other).onEntityCollision(this);
            }
        }
        return is_colliding;
    }


    /*
     * Calculates the distance of this object
     * and a given PanelEntity in pixel units - Set H
     *
     */
    public double calculateDistance(PanelEntity e){
        return Math.sqrt(Math.pow((this.x - e.x),2) + Math.pow((this.y - e.y),2));
    }


    /*
     * Calculates the angle relative to the x-axis a straight line from
     * this entity to given entity e would form.
     * The value might look wrong at first (i.e why it is negative or positive)
     * mostly because our 0,0 is at the top left - Set H
     *
     */
    public double calculateAngle(PanelEntity e) {
        double x_diff = e.x - x;
        double y_diff = e.y - y;
        double angle_radians = (Math.atan2(y_diff, x_diff));


        return angle_radians;
    }

    public double calculateAngle(int x, int y, int x2, int y2){
        double x_diff = x2 - x;
        double y_diff = y2 - y;
        double angle_radians = (Math.atan2(y_diff, x_diff));

        return angle_radians;
    }

    public void moveAtAngle(double angle_radians){
        deltaY = (int) Math.round(Math.sin(angle_radians) * speed);
        deltaX = (int) Math.round(Math.cos(angle_radians) * speed);
    }

    public void checkEntitySprites(){
        EntitySprite sprite = (EntitySprite) buffer;
        if (deltaY == 0 && deltaX == 0) {
            sprite.setMoving(false);
        } else {
            sprite.setMoving(true);
            if (deltaX > 0) {
                sprite.toRight();
            } else if (deltaX < 0) {
                sprite.toLeft();
            }
        }
    }

    public Point getTilePosition() {
        return new Point(x / GamePanel.TILE_SIZE, y / GamePanel.TILE_SIZE);
    }
}