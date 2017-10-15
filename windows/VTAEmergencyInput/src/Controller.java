import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class Controller {

	// today's entries are stored in a text file that is
	// comma delineated, with the following fomat:
	// run,block,coach,operator,timeDue,firstTime,direction,lastTime,pullInTime,actualTime,OCP,OE
	HashMap<String, PImage> buttonImages;
	HashMap<String, PImage> backgroundImages;
	HashMap<String, Button> buttons;

	private Display display;
	private Emergency_Input emergency_Input;
	private String tableId, firstName, lastName, email;
	private TableData tableData;

	public Controller(Emergency_Input g) {
		emergency_Input = g;
		display = new Display(emergency_Input, this);
		buttons = new HashMap<String, Button>();
		buttonImages = new HashMap<String, PImage>();
		backgroundImages = new HashMap<String, PImage>();
		loadImages(emergency_Input);
		putbuttons();
		ArrayList<String> s = getMetaData();
		tableId = s.get(0);
		System.out.println(tableId);
		firstName = s.get(1);
		lastName = s.get(2);
		email = s.get(3);
		tableData = new TableData(tableId);
		checkFileOutdated();
	}

	public void putbuttons() {
		buttons.put("createButton",
				new Button(Emergency_Input.SCREEN_WIDTH / 4,
						Emergency_Input.SCREEN_HEIGHT / 2 - buttonImages.get("BlankButton-white").height / 2,
						Button.ButtonName.CREATE, buttonImages.get("BlankButton-white")));
		buttons.put("optionsButton",
				new Button(Emergency_Input.SCREEN_WIDTH * 3 / 4 - buttonImages.get("BlankButton-white").width,
						Emergency_Input.SCREEN_HEIGHT / 2 - buttonImages.get("BlankButton-white").height / 2,
						Button.ButtonName.OPTIONS, buttonImages.get("BlankButton-white")));
		buttons.put("showFileButton",
				new Button(Emergency_Input.SCREEN_WIDTH / 2 - buttonImages.get("BlankButton-white").width / 2,
						Emergency_Input.SCREEN_HEIGHT / 2 + buttonImages.get("BlankButton-white").height * 5 / 2,
						Button.ButtonName.SHOWFILE, buttonImages.get("BlankButton-white")));
	}

	public void showDisplay() {
		display.show();
	}

	public void writeLineToFile(List<Object> line) {
		checkFileOutdated();
		tableData.appendLine(line);

		System.out.println("data has been written to the file");
	}

	public void registerclick(int x, int y) {
		for (Button b : buttons.values()) {
			if (b.isInside(x, y)) {
				b.doBehavior(this);
				return;
			}
		}
	}

	public void createNewObject() {
		ObjectCreator.start(this);
	}

	public void loadImages(PApplet p) {
		File[] files = new File("Assets/ButtonImages").listFiles();
		for (File f : files) {
			String str = f.getAbsolutePath();
			String s = f.getName();
			s = s.substring(0, s.indexOf("."));
			buttonImages.put(s, p.loadImage(str));
			System.out.println("Loaded " + s);
		}
		files = new File("Assets/BackgroundImages").listFiles();
		for (File f : files) {
			String str = f.getAbsolutePath();
			String s = f.getName();
			s = s.substring(0, s.indexOf("."));
			backgroundImages.put(s, p.loadImage(str));
			System.out.println("Loaded " + s);
		}
		PImage logo = backgroundImages.get("VTALogo");
		logo.resize(Emergency_Input.SCREEN_WIDTH, (Emergency_Input.SCREEN_WIDTH * logo.pixelHeight / logo.pixelWidth) * 2/3);
		display.setLogo(logo);
	}

	// checks when the mouse is over any button
	public void showMouseOver(int mouseX, int mouseY) {
		for (Button b : buttons.values()) {
			b.setMousedOver(b.isInside(mouseX, mouseY));
		}
	}

	public ArrayList<String> getMetaData() {
		String txtFile = "Assets/paths.txt";
		Scanner sc = null;
		try {
			sc = new Scanner(new File(txtFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<String> strings = new ArrayList<String>();
		while(sc.hasNext()) {
			strings.add(sc.nextLine());
		}
		sc.close();
		while(strings.size() < 4) {
			strings.add("N/A");
		}
		return strings;
	}

	public void openPathWindow() {
		new PathWindow(tableId, firstName, lastName, email,this);
	}

	public void changeMetaData(String newId, String newFirstName, String newLastName, String newEmail) {
		this.tableId = newId;
		this.firstName = newFirstName;
		this.lastName = newLastName;
		this.email = newEmail;
		String txtFile = "Assets/paths.txt";
		PrintWriter printWriter;
		try {
			printWriter = new PrintWriter(new File(txtFile));
		} catch (FileNotFoundException e) {
			System.out.println("missing data file");
			e.printStackTrace();
			return;
		}
		printWriter.println(newId);
		printWriter.println(newFirstName);
		printWriter.println(newLastName);
		printWriter.print(newEmail);
		printWriter.close();
	}
	
	public void openFileLocation() {
//		try {
//			Desktop.getDesktop().open(new File(tableId));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public String getFileDate() {
		return tableData.getDate();
	}
	
	public void showPathErrorPopup() { openPathWindow();}
	
	public boolean checkFileOutdated() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat noTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
		String s = getFileDate();
		if (s.length() != 0) {
			String oldDateString = s;
			Date oldDate = null;
			try {
				oldDate = format.parse(oldDateString + " 02:00:00");
			} catch (ParseException e) {
				tableData.createNewFile(noTimeFormat.format(now));
				return true;
			}
			if (oldDate != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(oldDate);
				cal.add(Calendar.DATE, 1);
				Date resetDate = cal.getTime();
				if (now.before(resetDate)) {
					return false;
				}
			}
		}
		tableData.createNewFile(noTimeFormat.format(now));
		return true;
	}
}
