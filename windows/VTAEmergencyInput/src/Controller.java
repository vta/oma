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

import processing.core.PApplet;
import processing.core.PImage;

public class Controller {

	// today's entries are stored in a text file that is
	// comma delineated, with the following fomat:
	// run,block,coach,operator,timeDue,firstTime,direction,lastTime,pullInTime,actualTime,OCP,OE
	HashMap<String, PImage> buttonImages;
	HashMap<String, PImage> backgroundImages;
	HashMap<String, Button> buttons; // buttons that display regardless of
											// state
	HashMap<String, Button> variableButtons; // buttons that are
												// state-dependant; will be
												// reset at each state change
	private Display display;
	private Emergency_Input emergency_Input;
	private String tableId;
	private TableData tableData;

	public Controller(Emergency_Input g) {
		emergency_Input = g;
		display = new Display(emergency_Input, this);
		buttons = new HashMap<String, Button>();
		buttonImages = new HashMap<String, PImage>();
		backgroundImages = new HashMap<String, PImage>();
		loadImages(emergency_Input);
		putbuttons();
		tableId = getMetaData();
		tableData = new TableData(tableId);
		checkFileOutdated();
	}

	public void putbuttons() {
		buttons.put("createButton",
				new Button(Emergency_Input.SCREEN_WIDTH / 4,
						Emergency_Input.SCREEN_HEIGHT / 2 - buttonImages.get("BlankButton-white").height / 2,
						Button.ButtonName.CREATE, buttonImages.get("BlankButton-white")));
		buttons.put("pathButton",
				new Button(Emergency_Input.SCREEN_WIDTH * 3 / 4 - buttonImages.get("BlankButton-white").width,
						Emergency_Input.SCREEN_HEIGHT / 2 - buttonImages.get("BlankButton-white").height / 2,
						Button.ButtonName.CHANGEPATH, buttonImages.get("BlankButton-white")));
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

	public String getMetaData() {
		String txtFile = "Assets/paths.txt";
		Scanner sc = null;
		try {
			sc = new Scanner(new File(txtFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String s = "<none>";
		if(sc.hasNext()) {
			s = sc.nextLine();
		}
		sc.close();
		return s;
	}

	public void openPathWindow() {
		new PathWindow(tableId, this);
	}

	public void changeMetaData(String newId) {
		this.tableId = newId;
		String txtFile = "Assets/paths.txt";
		PrintWriter printWriter;
		try {
			printWriter = new PrintWriter(new File(txtFile));
		} catch (FileNotFoundException e) {
			System.out.println("missing data file");
			e.printStackTrace();
			return;
		}
		printWriter.print("New Id: " + newId);
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
	
	public void showPathErrorPopup() {
		new PathWindow(this.tableId, this);
	}
	
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
