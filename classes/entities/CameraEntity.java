package classes.entities;

import java.awt.Graphics;

public class CameraEntity extends PanelEntity{

	public CameraEntity(int width, int height){
		super();
		super.setWidth(width);
		super.setHeight(height);
		buffer = null;
	}
	
	@Override
	public void move(int offsetX, int offsetY) {
        x += offsetX;
        y += offsetY;
    }

	@Override
	public void display(Graphics g, CameraEntity cam) {
		throw new RuntimeException("Displaying camera is a major dumb2 move betch");
	}
}
