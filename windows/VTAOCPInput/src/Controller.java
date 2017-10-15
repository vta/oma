import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

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
	private OCP_Input oCP_Input;
	private String tableId, metadataId, firstName, lastName, email;
	private TableData tableData;
	private TableData metadataTable;

	public Controller(OCP_Input g) {
		oCP_Input = g;
		display = new Display(oCP_Input, this);
		buttons = new HashMap<String, Button>();
		buttonImages = new HashMap<String, PImage>();
		backgroundImages = new HashMap<String, PImage>();
		loadImages(oCP_Input);
		putbuttons();
		ArrayList<String> s = getMetaData();
		tableId = s.get(0);
		System.out.println(tableId);
		firstName = s.get(1);
		lastName = s.get(2);
		email = s.get(3);
		metadataId = s.get(4);
		tableData = new TableData(tableId);
		metadataTable = new TableData(metadataId);
		checkFileOutdated();
	}

	public void putbuttons() {
		buttons.put("createButton",
				new Button(OCP_Input.SCREEN_WIDTH / 4,
						OCP_Input.SCREEN_HEIGHT / 2 - buttonImages.get("BlankButton-white").height / 2,
						Button.ButtonName.CREATE, buttonImages.get("BlankButton-white")));
		buttons.put("pathButton",
				new Button(OCP_Input.SCREEN_WIDTH * 3 / 4 - buttonImages.get("BlankButton-white").width,
						OCP_Input.SCREEN_HEIGHT / 2 - buttonImages.get("BlankButton-white").height / 2,
						Button.ButtonName.CHANGEPATH, buttonImages.get("BlankButton-white")));
		buttons.put("showFileButton",
				new Button(OCP_Input.SCREEN_WIDTH / 2 - buttonImages.get("BlankButton-white").width / 2,
						OCP_Input.SCREEN_HEIGHT / 2 + buttonImages.get("BlankButton-white").height * 5 / 2,
						Button.ButtonName.SHOWFILE, buttonImages.get("BlankButton-white")));
	}

	public void showDisplay() {
		display.show();
	}

	public void writeLineToFile(List<Object> line) {
		checkFileOutdated();
		tableData.appendLine(line);
		bit64Encode();
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
		logo.resize(OCP_Input.SCREEN_WIDTH, (OCP_Input.SCREEN_WIDTH * logo.pixelHeight / logo.pixelWidth) * 2/3);
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
		while(strings.size() < 5) {
			strings.add("N/A");
		}
		return strings;
	}

	public void openPathWindow() {
		new PathWindow(tableId, firstName, lastName, email, metadataId, this);
	}

	public void changeMetaData(String newTableId, String newFirstName, String newLastName, String newEmail, String newMetadataId) {
		this.tableId = newTableId;
		this.firstName = newFirstName;
		this.lastName = newLastName;
		this.email = newEmail;
		this.metadataId = newMetadataId;
		String txtFile = "Assets/paths.txt";
		PrintWriter printWriter;
		try {
			printWriter = new PrintWriter(new File(txtFile));
		} catch (FileNotFoundException e) {
			System.out.println("missing data file");
			e.printStackTrace();
			return;
		}
		printWriter.println(newTableId);
		printWriter.println(newFirstName);
		printWriter.println(newLastName);
		printWriter.println(newEmail);
		printWriter.print(newMetadataId);
		printWriter.close();
	}

	//Wrties a 64-bit-encoded string to the Metadata Google Sheet
	//Formatted: <First Name>;<Last Name>;<Email Address>;<Mac Address>
	public void bit64Encode() {
		String macAddress = "null";
		try {
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			macAddress = sb.toString();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e){
			e.printStackTrace();
		}

		String code = firstName + ";" + lastName + ";" + email + ";" + macAddress;
		String encoded = Base64.getEncoder().encodeToString(code.getBytes());
		metadataTable.writeMetadataLine(encoded);
	}

	public void openFileLocation() {
//		try {
//			Desktop.getDesktop().open(new File(dataPath));
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
