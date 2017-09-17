import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ObjectCreator extends JPanel implements ActionListener, KeyListener {

	private static Controller controller;
	
	private JFrame textAreaFrame;
	
	//private static Controller controller;
	
	private JButton submitButton;
	private JButton whatHappenedButton;
	private JButton cancelButton;
	
	private static JFrame frm;
	
    //Labels to identify the fields
    private JLabel priorityLabel;
    private JLabel descriptorLabel;
    private JLabel timeLabel;
    private JLabel dateLabel;
    private JLabel locationLabel;
    private JLabel directionLabel;
    private JLabel vehicle1Label;
    private JLabel vehicle2Label;
    private JLabel blockLabel;
    private JLabel badge1Label;
    private JLabel badge2Label;
    private JLabel name1Label;
    private JLabel name2Label;
    private JLabel respondingAgenciesLabel;
    private JLabel mediaOnSceneLabel;
    private JLabel respondingSupervisorsLabel;
    private JLabel injuriesLabel;
    private JLabel postAccidentTestRequiredLabel;
    private JLabel whatHappenedLabel;
    
    //Fields for data entry
    private JTextField descriptorField;
    private JFormattedTextField timeField;
    private JFormattedTextField dateField;
    private JTextField locationField;
    private JTextField directionField;
    private JTextField vehicle1Field;
    private JTextField vehicle2Field;
    private JTextField blockField;
    private JFormattedTextField badge1Field;
    private JFormattedTextField badge2Field;
    private JTextField name1Field;
    private JTextField name2Field;
    private JTextField respondingAgenciesField;
    private JTextField mediaOnSceneField;
    private JTextField respondingSupervisorsField;
    private JTextField injuriesField;
    private JComboBox<String> postAccidentTestRequiredBox;
    private JComboBox<String> priorityBox;
    private String description = "";
    private JTextArea textArea;

    String[] priorityStrings = {"Low", "Medium", "High"};
    String[] yesNoStrings = {"No", "Yes"};
    
    public ObjectCreator() {
        super(new BorderLayout());
                
        setBackground(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94));
                
        //initialize the buttons
        whatHappenedButton = new JButton("Enter a description");
        whatHappenedButton.setActionCommand("enterDescription");
        whatHappenedButton.addActionListener(this);
        
        submitButton = new JButton("Submit");
        submitButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        submitButton.setHorizontalTextPosition(AbstractButton.LEADING); 
        submitButton.setMnemonic(KeyEvent.VK_ENTER);
        submitButton.setActionCommand("submit");
        submitButton.addActionListener(this);
        submitButton.setToolTipText("click submit when all fields have been filled.");
        
        cancelButton = new JButton("Cancel");
        cancelButton.setHorizontalTextPosition(AbstractButton.LEADING);
        cancelButton.setMnemonic(KeyEvent.VK_ESCAPE);
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        cancelButton.setToolTipText("cancel");
        
        //Create JTextField labels
        priorityLabel = new JLabel("Level of Priority:");
        descriptorLabel = new JLabel("Descriptor: ");
        timeLabel = new JLabel("Time: ");
        dateLabel = new JLabel("Date: ");
        locationLabel = new JLabel("Location: ");
        directionLabel = new JLabel("Direction: ");
        vehicle1Label = new JLabel("vehicle #1: ");
        vehicle2Label = new JLabel("vehicle #2: ");
        blockLabel = new JLabel("Block: ");
        badge1Label = new JLabel("Badge #1: ");
        badge2Label = new JLabel("Badge #2: ");
        name1Label = new JLabel("Name #1: ");
        name2Label = new JLabel("Name #2: ");
        respondingAgenciesLabel = new JLabel("Responding Agencies: ");
        mediaOnSceneLabel = new JLabel("Media on Scene: ");
        respondingSupervisorsLabel = new JLabel("Responding Supervisors: ");
        injuriesLabel = new JLabel("Injuries: ");
        postAccidentTestRequiredLabel = new JLabel("Post Accident Test Required: ");
        whatHappenedLabel = new JLabel("What Happened:");
        
        //Create the JTextFields and set their sizes
        priorityBox = new JComboBox<String>(priorityStrings);
        descriptorField = new JFormattedTextField();
        timeField = new JFormattedTextField();
        dateField = new JFormattedTextField();
        locationField = new JTextField();
        directionField = new JTextField();
        vehicle1Field = new JTextField();
        vehicle2Field = new JTextField();
        blockField = new JTextField();
        badge1Field = new JFormattedTextField();
        badge2Field = new JFormattedTextField();
        name1Field = new JTextField();
        name2Field = new JTextField();
        respondingAgenciesField = new JTextField();
        mediaOnSceneField = new JTextField();
        respondingSupervisorsField = new JTextField();
        injuriesField = new JTextField();
        postAccidentTestRequiredBox = new JComboBox<String>(yesNoStrings);
        
        //label each field
        priorityLabel.setLabelFor(priorityBox);
        descriptorLabel.setLabelFor(descriptorField);
        timeLabel.setLabelFor(timeField);
        dateLabel.setLabelFor(dateField);
        locationLabel.setLabelFor(locationField);
        directionLabel.setLabelFor(directionField);
        vehicle1Label.setLabelFor(vehicle1Field);
        vehicle2Label.setLabelFor(vehicle2Field);
        blockLabel.setLabelFor(blockField);
        badge1Label.setLabelFor(badge1Field);
        badge2Label.setLabelFor(badge2Field);
        name1Label.setLabelFor(name1Field);
        name2Label.setLabelFor(name2Field);
        respondingAgenciesLabel.setLabelFor(respondingAgenciesField);
        mediaOnSceneLabel.setLabelFor(mediaOnSceneField);
        respondingSupervisorsLabel.setLabelFor(respondingSupervisorsField);
        injuriesLabel.setLabelFor(injuriesField);
        postAccidentTestRequiredLabel.setLabelFor(postAccidentTestRequiredBox);
        whatHappenedLabel.setLabelFor(whatHappenedButton); 
        
        //Lay out the labels in a panel
        JPanel labelPane = new JPanel(new GridLayout(0, 2));
        labelPane.add(priorityLabel);
        labelPane.add(priorityBox);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(descriptorLabel);
        labelPane.add(descriptorField);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(timeLabel);
        labelPane.add(timeField);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(dateLabel);
        labelPane.add(dateField);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(locationLabel);
        labelPane.add(locationField);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(directionLabel);
        labelPane.add(directionField);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(vehicle1Label);
        labelPane.add(vehicle1Field);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(vehicle2Label);
        labelPane.add(vehicle2Field);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(blockLabel);
        labelPane.add(blockField);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(badge1Label);
        labelPane.add(badge1Field);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(badge2Label);
        labelPane.add(badge2Field);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(name1Label);
        labelPane.add(name1Field);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(name2Label);
        labelPane.add(name2Field);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(respondingAgenciesLabel);
        labelPane.add(respondingAgenciesField);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(mediaOnSceneLabel);
        labelPane.add(mediaOnSceneField);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(respondingSupervisorsLabel);
        labelPane.add(respondingSupervisorsField);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(injuriesLabel);
        labelPane.add(injuriesField);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(postAccidentTestRequiredLabel);
        labelPane.add(postAccidentTestRequiredBox);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));
        labelPane.add(whatHappenedLabel);
        labelPane.add(whatHappenedButton);
        labelPane.add(new JLabel(""));
        labelPane.add(new JLabel(""));

        labelPane.setBackground(getBackground());

        //Put the panels in this panel, labels on left,
        //text fields on right.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(labelPane, BorderLayout.LINE_START);
        
        JPanel buttons = new JPanel(new GridLayout(0, 2));
        buttons.add(submitButton, BorderLayout.LINE_START);
        buttons.add(cancelButton, BorderLayout.LINE_END);
        buttons.setBackground(getBackground());
        add(buttons, BorderLayout.AFTER_LAST_LINE);
    }
	
	private static void showInterface() {
        //Create and set up the window.
        JFrame frame = new JFrame("Emergency");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Add contents to the window.
        frame.add(new ObjectCreator());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frm = frame;
    }
	
	public static void start(Controller c) {
		controller = c;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            UIManager.put("swing.boldMetal", Boolean.FALSE);
                showInterface();
            }
        });
    }
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.equals(KeyEvent.VK_ENTER)) {
			submitDescription();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("submit")) { //Checks if the user just pressed the submit button
			writeEmergencyToFile();
			frm.dispose();
		}
		if(command.equals("enterDescription")) {
			makeTextArea("Description");
		}
		if(command.equals("submitDescription")) {
			submitDescription();
		}
		if(command.equals("cancel")) {
			System.out.println("cancelling");
			frm.dispose();
		}
	}
	
	public void submitDescription() {
		description = (textArea.getText());
		textAreaFrame.dispose();
		whatHappenedButton.setText("Edit Description");
	}
	
	//writes the input values to the 'data.csv' file
	//written in the format:
	//descriptor,time,date,location,direction,vehicle1,vehicle2,block,badge1,badge2,name1,name2,respondingAgencies,mediaOnScene,respondingSupervisors,injuries, postAccidentTestRequired,whatHappened
	public void writeEmergencyToFile() {
		description = description.replace(",", "ß");
		String s = "";
		s += priorityBox.getSelectedItem() + ",";
	    s += descriptorField.getText() + ",";
	    s += timeField.getText() + ",";
	    s += dateField.getText() + ",";
	    s += locationField.getText() + ",";
	    s += directionField.getText() + ",";
	    s += vehicle1Field.getText() + ",";
	    s += vehicle2Field.getText() + ",";
	    s += blockField.getText() + ",";
	    s += badge1Field.getText() + ",";
	    s += badge2Field.getText() + ",";
	    s += name1Field.getText() + ",";
	    s += name2Field.getText() + ",";
	    s += respondingAgenciesField.getText() + ",";
	    s += mediaOnSceneField.getText() + ",";
	    s += respondingSupervisorsField.getText() + ",";
	    s += injuriesField.getText() + ",";
	    s += postAccidentTestRequiredBox.getSelectedItem() + ",";
	    if(description == "") {
	    		s += "N/A";
	    } else {
	    	s += description;
	    }
	    while(s.contains(",,")){
	    	int i = s.indexOf(",,");
	    	String first = s.substring(0, i + 1);
	    	String rest = s.substring(i + 1, s.length());
	    	s = first + "N/A" + rest;
	    }
	    controller.writeLineToFile(s);
	}
	
	
	public void makeTextArea(String label) {
		textAreaFrame = new JFrame(label);
		JPanel panel = new JPanel();
		
		//panel.setBackground(Color.WHITE);
		textArea = new JTextArea(description, 20, 30);
		textArea.setLineWrap(true);   
		
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(textArea);
		
        JButton enterButton = new JButton("Submit Description");
        enterButton.setActionCommand("submitDescription");
        enterButton.addActionListener(this);
        enterButton.setMnemonic(KeyEvent.VK_ENTER);
		
        textAreaFrame.add(panel);
        textAreaFrame.add(enterButton, BorderLayout.AFTER_LAST_LINE);

        textAreaFrame.pack();
        textAreaFrame.setVisible(true);
	}
		
	public void displayValues(String[] values) {
		String[] variables = {"Priority", "Descriptor", "Time", "Date", "Location", "Direction", "Vehicle #1",
				"Vehicle #2", "Block", "Badge #1", "Badge #2", "Responding Agents", "Media on Scene",
				"responding supervisors", "Injuries", "Post Accident Test Required", "What Happened"};
		System.out.println("Creating new " + values[11] + " with the following values:");
		for(int i = 0; i < values.length; i++) {
			System.out.println(variables[i] + ": " + values[i]);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
