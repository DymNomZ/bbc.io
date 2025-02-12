package classes.entities;

import classes.asset.sprite.Sprite;
import interfaces.EntityCollidable;
import src.KeyHandler;

public class Player extends MapEntity implements EntityCollidable{

	private final KeyHandler inputs;


	@Override
	public void onEntityCollision(PanelEntity e){

	}

	public Player(String name, int hit_points, int screenWidth, int screenHeight, int side, int allowance, KeyHandler inputs){
		super();
		super.setHit_points(hit_points);
		super.setMax_hit_points(hit_points);
		super.setX(screenWidth);
		super.setY(screenHeight);
		super.setDimensions(side,side + allowance);
		buffer = Sprite.load("default");

		this.x /= 2;
		this.y /= 2;
		this.inputs = inputs;
	}

	public void move(){
		//heheh - dymes
		
		if(inputs.up_pressed == inputs.down_pressed) {
			deltaY = 0;
		}
		else if(inputs.up_pressed)
			deltaY = -speed;
		else if(inputs.down_pressed)
			deltaY = speed;

		if(inputs.right_pressed == inputs.left_pressed){
			deltaX = 0;
		}
		else if(inputs.left_pressed)
			deltaX = -speed;
		else if(inputs.right_pressed)
			deltaX = speed;

		if (inputs.lShift_pressed) {
			speed = 9;
		} else {
			speed = 6;
		}
		//checkEntitySprites();

	}


}
