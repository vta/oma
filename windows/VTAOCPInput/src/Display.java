
import processing.core.PFont;
import processing.core.PImage;

public class Display {
	
	private OCP_Input oCP_Input;
	private PImage logo;
	private Controller controller;
	private PFont font;
	
	public Display(OCP_Input g, Controller c) {
		oCP_Input = g;
		controller = c;
		font = oCP_Input.createFont("Charter-Roman", 16);
	}
	
	//a generic display that is shown regardless of what the current State is
	public void show() {
		oCP_Input.background(0, 96, 153);
		oCP_Input.image(controller.backgroundImages.get("VTALogo"), 0, 0);
		showButtons();
	}
	
	public void showButtons() {
		oCP_Input.textFont(font, 15);
		oCP_Input.fill(0);
		oCP_Input.textAlign(OCP_Input.CENTER);
		for(Button b: controller.buttons.values()) {
			showButton(b);
		}
		oCP_Input.textAlign(OCP_Input.LEFT);
	}
	
	public void showButton(Button button) {
		PImage image = button.getImage();
		if(button.isMousedOver()) image = controller.buttonImages.get("BlankButton-blue");
		oCP_Input.image(image, button.getX(), button.getY());
		oCP_Input.text(button.getLabel(), button.getX() + button.getWidth() / 2, button.getY() + button.getHeight() - 5);
	}
	
	public PImage getLogo() {
		return logo;
	}
	
	public void setLogo(PImage im) {
		logo = im;
	}

}
