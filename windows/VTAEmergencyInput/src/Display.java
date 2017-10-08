import processing.core.PFont;
import processing.core.PImage;

public class Display {
	
	private Emergency_Input emergency_Input;
	private PImage logo;
	private Controller controller;
	private PFont font;
	
	public Display(Emergency_Input g, Controller c) {
		emergency_Input = g;
		controller = c;
		font = emergency_Input.createFont("Charter-Roman", 16);
	}
	
	//a generic display that is shown regardless of what the current State is
	public void show() {
		emergency_Input.background(0, 96, 153);
		emergency_Input.image(controller.backgroundImages.get("VTALogo"), 0, 0);
		showButtons();
	}
	
	public void showButtons() {
		emergency_Input.textFont(font, 15);
		emergency_Input.fill(0);
		emergency_Input.textAlign(Emergency_Input.CENTER);
		for(Button b: controller.buttons.values()) {
			showButton(b);
		}
		emergency_Input.textAlign(Emergency_Input.LEFT);
	}
	
	public void showButton(Button button) {
		PImage image = button.getImage();
		if(button.isMousedOver()) image = controller.buttonImages.get("BlankButton-blue");
		emergency_Input.image(image, button.getX(), button.getY());
		emergency_Input.text(button.getLabel(), button.getX() + button.getWidth() / 2, button.getY() + button.getHeight() - 5);
	}
	
	public PImage getLogo() {
		return logo;
	}
	
	public void setLogo(PImage im) {
		logo = im;
	}

}
