
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import processing.core.PApplet;
import processing.core.PImage;

public class Controller {

	// today's entries are stored in a text file that is
	// comma delineated, with the following fomat:
	// run,block,coach,operator,timeDue,firstTime,direction,lastTime,pullInTime,actualTime,OCP,OE
	HashMap<String, PImage> buttonImages;
	HashMap<String, PImage> backgroundImages;
	HashMap<String, Button> staticButtons;
	private Display display;
	private OCP_Input oCP_Input;
	private String dataPath;
	private String username;
	private String password;

	public Controller(OCP_Input g) {
		oCP_Input = g;
		display = new Display(oCP_Input, this);
		staticButtons = new HashMap<String, Button>();
		buttonImages = new HashMap<String, PImage>();
		backgroundImages = new HashMap<String, PImage>();
		loadImages(oCP_Input);
		putStaticButtons();
		String[] metaData = getMetaData();
		this.dataPath = metaData[0];
		this.username = metaData[1];
		this.password = metaData[2];
		checkFileOutdated();
	}

	public void putStaticButtons() {
		staticButtons.put("createButton",
				new Button(OCP_Input.SCREEN_WIDTH / 4,
						OCP_Input.SCREEN_HEIGHT / 2 - buttonImages.get("BlankButton-white").height / 2,
						Button.ButtonName.CREATE, buttonImages.get("BlankButton-white")));
		staticButtons.put("pathButton",
				new Button(OCP_Input.SCREEN_WIDTH * 3 / 4 - buttonImages.get("BlankButton-white").width,
						OCP_Input.SCREEN_HEIGHT / 2 - buttonImages.get("BlankButton-white").height / 2,
						Button.ButtonName.CHANGEPATH, buttonImages.get("BlankButton-white")));
		staticButtons.put("showFileButton",
				new Button(OCP_Input.SCREEN_WIDTH / 2 - buttonImages.get("BlankButton-white").width / 2,
						OCP_Input.SCREEN_HEIGHT / 2 + buttonImages.get("BlankButton-white").height * 5 / 2,
						Button.ButtonName.SHOWFILE, buttonImages.get("BlankButton-white")));
	}

	public void showDisplay() {
		display.show();
	}

	public void writeLineToFile(String line) {
		checkFileOutdated();
		
		if (dataPath.startsWith("smb")) {

		    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",username, password);
			try {
				SmbFile smbFile = new SmbFile(dataPath, auth);
			    SmbFileOutputStream smbfos = new SmbFileOutputStream(smbFile);
			    smbfos.write(line.getBytes());
			    smbfos.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (SmbException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		    
		} else {
			PrintWriter printWriter;
			try {
				printWriter = new PrintWriter(new FileOutputStream(new File(dataPath), true));
			} catch (FileNotFoundException e) {
				System.out.println("missing data file");
				e.printStackTrace();
				return;
			}
			printWriter.println(line);
			printWriter.close();
		}
		System.out.println("data has been written to the file");
	}

	public void registerclick(int x, int y) {
		for (Button b : staticButtons.values()) {
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
		for (Button b : staticButtons.values()) {
			b.setMousedOver(b.isInside(mouseX, mouseY));
		}
	}

	public String[] getMetaData() {
		String txtFile = "Assets/paths.txt";
		Scanner sc = null;
		try {
			sc = new Scanner(new File(txtFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String[] s = {"<none>", "<none>", "<none>"};
		for(int i = 0; i <  3 && sc.hasNext(); i++) {
			s[i] = sc.next();
		}
		sc.close();
		return s;
	}

	public void openPathWindow() {
		new PathWindow(this.dataPath, this.username, this.password, this);
	}

	public void changeMetaData(String newPath, String newUsername, String newPassword) {
		this.dataPath = newPath;
		this.username = newUsername;
		this.password = newPassword;
		String txtFile = "Assets/paths.txt";
		PrintWriter printWriter;
		try {
			printWriter = new PrintWriter(new File(txtFile));
		} catch (FileNotFoundException e) {
			System.out.println("missing data file");
			e.printStackTrace();
			return;
		}
		printWriter.print(newPath + "\n" + newUsername + "\n" + newPassword);
		printWriter.close();
	}
	
	public void openFileLocation() {
		try {
			Desktop.getDesktop().open(new File(dataPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getFileDate() {
		if(dataPath.startsWith("smb")){
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, this.username, this.password);
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new SmbFileInputStream(new SmbFile(dataPath, auth))))) {
			    return reader.readLine();
			} catch (SmbException e) {
				e.printStackTrace();
				return null;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			Scanner sc = null;
			try {
				sc = new Scanner(new File(dataPath));
			} catch (FileNotFoundException e) {
				new PathErrorPopup(this, e);
				e.printStackTrace();
			}
			return sc.next();
		}
	}
	
	public void resetGui() {
		oCPInput = new OCP_Input();
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
				createNewFile(noTimeFormat.format(now));
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
		createNewFile(noTimeFormat.format(now));
		return true;
	}
	
	public void createNewFile(String date) {
		System.out.println("clearing file for new day.");
		if (dataPath.startsWith("smb")) {
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", username, password);
			SmbFile sFile;
			try {
				sFile = new SmbFile(dataPath, auth);
				SmbFileOutputStream sfos = new SmbFileOutputStream(sFile);
				sfos.write("".getBytes());
				sfos.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (SmbException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			PrintWriter printWriter;
			try {
				printWriter = new PrintWriter(new File(dataPath));
			} catch (FileNotFoundException e) {
				System.out.println("missing data file");
				e.printStackTrace();
				return;
			}
			printWriter.print("");
			printWriter.close();
			try {
				printWriter = new PrintWriter(new File(dataPath));
			} catch (FileNotFoundException e) {
				System.out.println("missing data file");
				e.printStackTrace();
				return;
			}
			printWriter.println(date
					+ "\nRun,Block,Coach,Operator,TimeDue,FirstTime,Direction,LastTime,Pull-InTime,Actual,O/C/P,Op/Eq");
			printWriter.close();
		}
	}
}
