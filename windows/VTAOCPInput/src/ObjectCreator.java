import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;;

import javax.swing.*;

@SuppressWarnings("serial")
public class ObjectCreator extends JPanel implements ActionListener{
	
	private static Controller controller;
	
	private JButton submitButton;
	private JButton cancelButton;
	
	private static JFrame frm;
	
    //Labels to identify the fields
    private JLabel runLabel;
    private JLabel runErrorLabel;
    private JLabel blockLabel;
    private JLabel blockErrorLabel;
    private JLabel coachLabel;
    private JLabel coachErrorLabel;
    private JLabel operatorLabel;
    private JLabel operatorErrorLabel;
    private JLabel timeDueLabel;
    private JLabel timeDueAMPMLabel;
    private JLabel timeDueErrorLabel;
    private JLabel firstTimeLabel;
    private JLabel firstTimeAMPMLabel;
    private JLabel firstTimeErrorLabel;
    private JLabel directionLabel;
    private JLabel directionErrorLabel;
    private JLabel lastTimeLabel;
    private JLabel lastTimeAMPMLabel;
    private JLabel lastTimeErrorLabel;
    private JLabel pullInTimeLabel;
    private JLabel pullInTimeAMPMLabel;
    private JLabel pullInTimeErrorLabel;
    private JLabel actualTimeLabel;
    private JLabel actualTimeAMPMLabel;
    private JLabel actualTimeErrorLabel;
    private JLabel ocpLabel; //outlate/cancel/pull
    private JLabel ocpErrorLabel;
    private JLabel oeLabel; //operator/equipment
    private JLabel oeErrorLabel;
 
    //Strings for the labels
    private static String runString = "Run: ";
    private static String blockString = "Block: ";
    private static String coachString = "Coach: ";
    private static String operatorString = "Operator: ";
    private static String timeDueString = "Time Due: ";
    private static String AMPMString = "(00:00)";
    private static String firstTimeString = "First Time: ";
    private static String directionString = "Direction: ";
    private static String lastTimeString = "Last Time: ";
    private static String pullInTimeString = "Pull-In Time: ";
    private static String actualTimeString = "Actual (if outlate): ";
    private static String ocpString = "Outlate/Cancel/Pull: ";
    private static String oeString = "Operator/Equipment: ";
 
    //Fields for data entry
    private JTextField runField;
    private JTextField blockField;
    private JTextField coachField;
    private JTextField operatorField;
    private JFormattedTextField timeDueField;
    private JComboBox<String> timeDueAMPMBox;
    private JFormattedTextField firstTimeField;
    private JComboBox<String> firstTimeAMPMBox;
    private JComboBox<String> directionBox;
    private JFormattedTextField lastTimeField;
    private JComboBox<String> lastTimeAMPMBox;
    private JFormattedTextField pullInTimeField;
    private JComboBox<String> pullInTimeAMPMBox;
    private JFormattedTextField actualTimeField;
    private JComboBox<String> actualTimeAMPMBox;
    private JComboBox<String> ocpBox;
    private JComboBox<String> oeBox;

    private SimpleDateFormat timeDisplayFormat;
    
    String[] dayTimeStrings = {"AM", "PM"};
    String[] nightTimeStrings = {"PM", "AM"};
    String[] directions = {"Choose One", "Northbound", "Westbound", "Southbound", "Eastbound"};
    String[] ocpStrings = {"Choose One", "Outlate", "Cancel", "Pull"};
    String[] oeStrings = {"Choose One", "Operator", "Equipment"};

    Timer resizeTimer;
    
    public ObjectCreator() {
        super(new BorderLayout());
                
        setBackground(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94));
        
        initializeTimeDisplayFormat();
        
        //create the submit button
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
        runLabel = new JLabel(runString);
        runErrorLabel = new JLabel("");
        blockLabel = new JLabel(blockString);
        blockErrorLabel = new JLabel("");
        coachLabel = new JLabel(coachString);
        coachErrorLabel = new JLabel("");
        operatorLabel = new JLabel(operatorString);
        operatorErrorLabel = new JLabel("");
        timeDueLabel = new JLabel(timeDueString);
        timeDueAMPMLabel = new JLabel(AMPMString);
        timeDueAMPMLabel.setForeground(Color.DARK_GRAY);
        timeDueErrorLabel = new JLabel("");
        firstTimeLabel = new JLabel(firstTimeString);
        firstTimeAMPMLabel = new JLabel(AMPMString);
        firstTimeAMPMLabel.setForeground(Color.DARK_GRAY);
        firstTimeErrorLabel = new JLabel("");
        directionLabel = new JLabel(directionString);
        directionErrorLabel = new JLabel("");
        lastTimeLabel = new JLabel(lastTimeString);
        lastTimeAMPMLabel = new JLabel(AMPMString);
        lastTimeAMPMLabel.setForeground(Color.DARK_GRAY);
        lastTimeErrorLabel = new JLabel("");
        pullInTimeLabel = new JLabel(pullInTimeString);
        pullInTimeAMPMLabel = new JLabel(AMPMString);
        pullInTimeAMPMLabel.setForeground(Color.DARK_GRAY);
        pullInTimeErrorLabel = new JLabel("");
        actualTimeLabel = new JLabel(actualTimeString);
        actualTimeAMPMLabel = new JLabel(AMPMString);
        actualTimeAMPMLabel.setForeground(Color.DARK_GRAY);
        actualTimeErrorLabel = new JLabel("");
        ocpLabel = new JLabel(ocpString);
        ocpErrorLabel = new JLabel("");
        oeLabel = new JLabel(oeString);
        oeErrorLabel = new JLabel("");
        
        
        //Create the JTextFields and set their sizes
        runField = new JTextField();
        runField.setColumns(15);
 
        blockField = new JTextField();
        blockField.setColumns(10);
 
        coachField = new JTextField();
        coachField.setColumns(10);
 
        operatorField = new JTextField();
        operatorField.setColumns(10);
        
        timeDueField = new JFormattedTextField(timeDisplayFormat);
        timeDueField.setColumns(10);
        timeDueAMPMBox = new JComboBox<String>(dayTimeStrings);
        
        firstTimeField = new JFormattedTextField(timeDisplayFormat);
        firstTimeField.setColumns(10);
        firstTimeAMPMBox = new JComboBox<String>(dayTimeStrings);
        
        directionBox = new JComboBox<String>(directions);
        
        lastTimeField = new JFormattedTextField(timeDisplayFormat);
        lastTimeField.setColumns(10);
        lastTimeAMPMBox = new JComboBox<String>(nightTimeStrings);
        
        pullInTimeField = new JFormattedTextField(timeDisplayFormat);
        pullInTimeField.setColumns(10);
        pullInTimeAMPMBox = new JComboBox<String>(nightTimeStrings);
        
        actualTimeField = new JFormattedTextField(timeDisplayFormat);
        actualTimeField.setColumns(10);
        actualTimeAMPMBox = new JComboBox<String>(nightTimeStrings);
        
        ocpBox = new JComboBox<String>(ocpStrings);
        oeBox = new JComboBox<String>(oeStrings);
        
        
        //label each field
        runLabel.setLabelFor(runField);
        blockLabel.setLabelFor(blockField);
        coachLabel.setLabelFor(coachField);
        operatorLabel.setLabelFor(operatorField);
        timeDueLabel.setLabelFor(timeDueField);
        timeDueAMPMLabel.setLabelFor(timeDueAMPMBox);
        firstTimeLabel.setLabelFor(firstTimeField);
        firstTimeAMPMLabel.setLabelFor(firstTimeAMPMBox);
        directionLabel.setLabelFor(directionBox);
        lastTimeLabel.setLabelFor(lastTimeField);
        lastTimeAMPMLabel.setLabelFor(lastTimeAMPMBox);
        pullInTimeLabel.setLabelFor(pullInTimeField);
        pullInTimeAMPMLabel.setLabelFor(pullInTimeAMPMBox);
        actualTimeLabel.setLabelFor(actualTimeField);
        actualTimeAMPMLabel.setLabelFor(actualTimeAMPMBox);
        ocpLabel.setLabelFor(ocpBox);
        oeLabel.setLabelFor(oeBox);
 
        
        //Lay out the labels in a panel
        JPanel labelPane = new JPanel(new GridLayout(0, 3));
        labelPane.add(runLabel);
        labelPane.add(runField);
        labelPane.add(new JLabel(""));
        
        labelPane.add(new JLabel("")); //blank for spacing between fields
        labelPane.add(runErrorLabel);
        labelPane.add(new JLabel(""));
        
        labelPane.add(blockLabel);
        labelPane.add(blockField);
        labelPane.add(new JLabel(""));
        
        labelPane.add(new JLabel(""));
        labelPane.add(blockErrorLabel);
        labelPane.add(new JLabel(""));
        
        labelPane.add(coachLabel);
        labelPane.add(coachField);
        labelPane.add(new JLabel(""));
        
        labelPane.add(new JLabel(""));
        labelPane.add(coachErrorLabel);
        labelPane.add(new JLabel(""));
        
        labelPane.add(operatorLabel);
        labelPane.add(operatorField);
        labelPane.add(new JLabel(""));
        
        labelPane.add(new JLabel(""));
        labelPane.add(operatorErrorLabel);
        labelPane.add(new JLabel(""));
        
        labelPane.add(timeDueLabel);
        labelPane.add(timeDueField);
        labelPane.add(timeDueAMPMBox);
        
        labelPane.add(timeDueAMPMLabel);
        labelPane.add(timeDueErrorLabel);
        labelPane.add(new JLabel(""));
        
        labelPane.add(firstTimeLabel);
        labelPane.add(firstTimeField);
        labelPane.add(firstTimeAMPMBox);
        
        labelPane.add(firstTimeAMPMLabel);
        labelPane.add(firstTimeErrorLabel);
        labelPane.add(new JLabel(""));
        
        labelPane.add(directionLabel);
        labelPane.add(directionBox);
        labelPane.add(new JLabel(""));
        
        labelPane.add(new JLabel(""));
        labelPane.add(directionErrorLabel);
        labelPane.add(new JLabel(""));
        
        labelPane.add(lastTimeLabel);
        labelPane.add(lastTimeField);
        labelPane.add(lastTimeAMPMBox);
        
        labelPane.add(lastTimeAMPMLabel);
        labelPane.add(lastTimeErrorLabel);
        labelPane.add(new JLabel(""));

        labelPane.add(pullInTimeLabel);
        labelPane.add(pullInTimeField);
        labelPane.add(pullInTimeAMPMBox);
        
        labelPane.add(pullInTimeAMPMLabel);
        labelPane.add(pullInTimeErrorLabel);
        labelPane.add(new JLabel(""));
        
        labelPane.add(actualTimeLabel);
        labelPane.add(actualTimeField);
        labelPane.add(actualTimeAMPMBox);
        
        labelPane.add(actualTimeAMPMLabel);
        labelPane.add(actualTimeErrorLabel);
        labelPane.add(new JLabel(""));
        
        labelPane.add(ocpLabel);
        labelPane.add(ocpBox);
        labelPane.add(new JLabel(""));
        
        labelPane.add(new JLabel(""));
        labelPane.add(ocpErrorLabel);
        labelPane.add(new JLabel(""));
        
        labelPane.add(oeLabel);
        labelPane.add(oeBox);
        labelPane.add(new JLabel(""));
        
        labelPane.add(new JLabel(""));
        labelPane.add(oeErrorLabel);
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
        add(buttons, BorderLayout.AFTER_LAST_LINE); b

        resizeTimer = new Timer(2, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // Actually perform the resizing of the image...
                System.out.println("yo");
                if(getWidth() > labelPane.getWidth() * 2) {
                    System.out.println("hi");
                }
                resizeTimer.restart();
            }
        });
    }
    
    public void showErrorLabel(JLabel label, String s) {
    	label.setForeground(Color.RED);
    	label.setText(s);
    }
    
    public void showErrorLabel(JLabel label, JTextField field, String s) {
    	field.setBackground(Color.PINK);
    	label.setForeground(Color.RED);
    	label.setText(s);
    }
	
	private static void showInterface() {
        //Create and set up the window.
        JFrame frame = new JFrame("Outlate/Cancel/Pull");
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
	
	//attempts to submit the information and create an object
	//returns true if submit is successful, and false if more
	//information is needed to be input.
	public boolean attemptSubmit() {
		boolean isComplete = true;
		
		//if any values (except actuals when not an outlate) are
		//not input, method returns false
		if(runField.getText().isEmpty()) {
			showErrorLabel(runErrorLabel, runField, "Please enter a run.");
			isComplete = false;
		}
		if(blockField.getText().isEmpty()) {
			showErrorLabel(blockErrorLabel, blockField, "Please enter a block.");
			isComplete = false;
		}
		if(coachField.getText().isEmpty()) {
			showErrorLabel(coachErrorLabel, coachField, "Please enter a coach.");
			isComplete = false;
		}
		if(operatorField.getText().isEmpty()) {
			showErrorLabel(operatorErrorLabel, operatorField, "Please enter an operator.");
			isComplete = false;
		}
		if(timeDueField.getValue() == null) {
			showErrorLabel(timeDueErrorLabel, timeDueField, "Please enter a due time.");
			isComplete = false;
		}
		if(firstTimeField.getValue() == null) {
			showErrorLabel(firstTimeErrorLabel, firstTimeField, "Please enter a first time.");
			isComplete = false;
		}
		if(directionBox.getSelectedIndex() == 0) {
			showErrorLabel(directionErrorLabel, "Please choose a direction.");
			isComplete = false;
		}
		if(lastTimeField.getValue() == null) {
			showErrorLabel(lastTimeErrorLabel, lastTimeField, "Please enter a last time.");
			isComplete = false;
		}
		if(pullInTimeField.getValue() == null) {
			showErrorLabel(pullInTimeErrorLabel, pullInTimeField, "Please enter a pull-in time.");
			isComplete = false;
		}
		if(ocpBox.getSelectedIndex() == 0) {
			showErrorLabel(ocpErrorLabel, "Please make a selection.");
			isComplete = false;
		}
		if(oeBox.getSelectedIndex() == 0) {
			showErrorLabel(oeErrorLabel, "Please make a selection.");
			isComplete = false;
		}
		
		if(ocpBox.getSelectedIndex() == 1) { //If the input information indicates an outlate
			if(actualTimeField.getValue() == null) {
				showErrorLabel(actualTimeErrorLabel, actualTimeField, "Please make a selection or deselect outlate.");
				isComplete = false;
			}
		}
		return isComplete;
	}

	public void initializeTimeDisplayFormat() {
		timeDisplayFormat = new SimpleDateFormat("hh:mm");
        timeDisplayFormat.setLenient(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("submit")) { //Checks if the user just pressed the submit button
			System.out.println("Attempting to submit...");
			if(attemptSubmit()) {
				//There is enough information provided
				System.out.println("Successful submit");
				createOCP();
				frm.dispose();
			} else {
				//There is not enough information provided
				System.out.println("Unsuccessful submit");
			}
		}
		if(command.equals("cancel")) {
			System.out.println("cancelling");
			frm.dispose();
		}
	}
	
	public void createOCP() {
		String s = "";
		s += runField.getText() + ",";
		s += blockField.getText() + ",";
		s += coachField.getText() + ",";
		s += operatorField.getText() + ",";
		s += timeDueField.getText() + timeDueAMPMBox.getSelectedItem() + ",";
		s += firstTimeField.getText() + firstTimeAMPMBox.getSelectedItem() + ",";
		s += directionBox.getSelectedItem() + ",";
		s += lastTimeField.getText() + lastTimeAMPMBox.getSelectedItem() + ",";
		s += pullInTimeField.getText() + pullInTimeAMPMBox.getSelectedItem() + ",";
		if(ocpBox.getSelectedItem().equals("Outlate")) { // checks if the actualTime field is needed
			s += actualTimeField.getText() + actualTimeAMPMBox.getSelectedItem() + ",";
		} else {
			s += "N/A" + ",";
		}
		s += ocpBox.getSelectedItem() + ",";
		s += oeBox.getSelectedItem() + ",";
        while(s.contains(",,")){
            int i = s.indexOf(",,");
            String first = s.substring(0, i + 1);
            String rest = s.substring(i + 1, s.length());
            s = first + "N/A" + rest;
        }
        List<Object> list = new ArrayList<Object>();
        for(String str: s.split(",")) {
            list.add(str);
        }
        controller.writeLineToFile(list);
	}
	
	public void displayValues(String[] values) {
		String[] variables = {"Run", "Block", "Coach", "Operator", "Time Due", "First Time", "Direction",
				"Last Time", "Pull-In Time", "Actual", "Outlate/Cancel/Pull", "Operator/Equipment"};
		System.out.println("Creating new " + values[10] + " with the following values:");
		for(int i = 0; i < values.length; i++) {
			System.out.println(variables[i] + ": " + values[i]);
		}
	}

}
