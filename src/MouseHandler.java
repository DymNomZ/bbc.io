package src;

import java.awt.*;
import java.awt.event.*;

public class MouseHandler implements MouseMotionListener, MouseWheelListener, MouseListener{
	int mouse_x;
	int mouse_y;
	Point mouse_location_on_screen;

	boolean left_is_pressed  = false;
	boolean right_is_pressed = false;

	@Override
	public void mouseReleased(MouseEvent e){
		left_is_pressed = false;
		right_is_pressed = false;
	}


	@Override
	public void mousePressed(MouseEvent e){
		int mouse_code = e.getButton();

		switch(mouse_code){
			case 1 -> left_is_pressed = true;
			case 3 -> right_is_pressed = true;
		}

		//handle button dynamic GUI
		handleButtons(e);

	}

	@Override
	public void mouseEntered(MouseEvent e){

	}

	@Override
	public void mouseExited(MouseEvent e){

	}

	private void handleButtons(MouseEvent e){

	}

	@Override
	public void mouseClicked(MouseEvent e){

	}

	@Override
	public void mouseDragged(MouseEvent e){

	}

	@Override
	public void mouseMoved(MouseEvent e){

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e){

	}

}
