
import processing.core.PApplet;

public class Emergency_Input extends PApplet{
	
	final static int SCREEN_WIDTH = 800;
	final static int SCREEN_HEIGHT = 400;
	
	Controller controller;
	
	public static void main(String[] args) {
		PApplet.main(new String[] { Emergency_Input.class.getName(), "Emergency_Input" });
	}
	
	public void settings() {
		size(SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	public void setup() {
		controller = new Controller(this);
	}
	
	public void draw() {			
		controller.showMouseOver(mouseX, mouseY);
		controller.showDisplay();
	}
	
	public void mousePressed() {
		if(mouseButton == LEFT) controller.registerclick(mouseX, mouseY);
	}
}
