import processing.core.PImage;

public class Button {
	
	protected int x; // (x,y) location of top left corner
	protected int y;
	protected int width, height;
	protected int LEFT_EDGE, RIGHT_EDGE, BOTTOM, TOP;
	protected ButtonName name;
	protected PImage image;
	protected boolean mousedOver = false;
	
	public enum ButtonName {
		CREATE, CHANGEPATH, SHOWFILE;
	}
	
	public Button(int Y, ButtonName n, PImage im) { //constructs a button that is centered in the screen
		y = Y;
		name = n;
		image = im;
		width = im.pixelWidth;
		height = im.pixelHeight;
		x = (Emergency_Input.SCREEN_WIDTH - width)/ 2 ;
		assignEdges();
	}
	
	public Button(int X, int Y, ButtonName n, PImage im) {
		x = X;
		y = Y;
		name = n;
		image = im;
		width = im.pixelWidth;
		height = im.pixelHeight;
		assignEdges();
	}
	
	//used only to construct a button that sorts the ocpList by a given value
	public Button(int X, int Y, int w, int h, PImage im) {
		x = X;
		y = Y;
		image = im;
		image.resize(w, h);
		width = w;
		height = h;
		assignEdges();
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	// executes code based on what type of button has been clicked.
	public void doBehavior(Controller c) {
		switch (name) {
		case CREATE:
			c.createNewObject();
			return;
		case CHANGEPATH:
			c.openPathWindow();
			return;
		case SHOWFILE:
			c.openFileLocation();
			return;
		}
	}
	
	public boolean isInside(int x, int y) {
		if(x < LEFT_EDGE || x > RIGHT_EDGE) return false;
		if(y < TOP || y > BOTTOM) return false;
		return true;
	}
	
	public ButtonName getName() {
		return name;
	}
	
	public PImage getImage() {
		return image;
	}
	
	public void assignEdges() {
		LEFT_EDGE = x;
		RIGHT_EDGE = x + width;
		TOP = y;
		BOTTOM = y + height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public String getLabel() {
		switch (name) {
		case CREATE:
			return "Input New Information";
		case CHANGEPATH:
			return "Change Path to Data File";
		case SHOWFILE:
			return "Open Data File";
		}
		return null;
	}
	
	public String toString() {
		String s = "button labeled: " + getLabel() + " at (" + x + "," + y + ")";
		return s;
	}

	public void setMousedOver(boolean b) {
		mousedOver = b;
	}
	
	public boolean isMousedOver() {
		return mousedOver;
	}

	public void setResizedImage(PImage im) {
		image = im;
		image.resize(width, height);
	}
}
