import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

@SuppressWarnings("serial")
public class PathWindow extends JPanel implements ActionListener, KeyListener{
	
	public String newId;
	public String newFirstName;
	public String newLastName;
	public String newEmail;
	public String newMetadataId;

	private JLabel tableIdLabel;
	private JLabel firstNameLabel;
	private JLabel lastNameLabel;
	private JLabel emailLabel;
	private JLabel metadataIdLabel;

	private JTextArea tableIdArea;
	private JTextArea firstNameArea;
	private JTextArea lastNameArea;
	private JTextArea emailArea;
	private JTextArea metadataIdArea;

	private JFrame textAreaFrame;
	private Controller controller;
	
	public PathWindow(String oldTableId, String oldFirstName, String oldLastName, String oldEmail, String oldMetaDataId, Controller c){
		this.controller = c;
		this.newId = oldTableId;
		this.newFirstName = oldFirstName;
		this.newLastName = oldLastName;
		this.newEmail = oldEmail;

		textAreaFrame = new JFrame("Options");
		JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBackground(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94));
		
		tableIdLabel = new JLabel();
		firstNameLabel = new JLabel();
		lastNameLabel = new JLabel();
		emailLabel = new JLabel();
		metadataIdLabel = new JLabel();
		
		tableIdArea = new JTextArea(oldTableId, 2, 30);
		tableIdArea.setBorder(BorderFactory.createLineBorder(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94), 2));
		firstNameArea = new JTextArea(oldFirstName, 1, 30);
		firstNameArea.setBorder(BorderFactory.createLineBorder(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94), 2));
		lastNameArea = new JTextArea(oldLastName, 1, 30);
		lastNameArea.setBorder(BorderFactory.createLineBorder(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94), 2));
		emailArea = new JTextArea(oldEmail, 1, 30);
		emailArea.setBorder(BorderFactory.createLineBorder(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94), 2));
		metadataIdArea = new JTextArea(oldMetaDataId, 1, 30);
		metadataIdArea.setBorder(BorderFactory.createLineBorder(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94), 2));


		tableIdLabel.setText("Data Entries Table ID: ");
		firstNameLabel.setText("First Name: ");
		lastNameLabel.setText("Last Name: ");
		emailLabel.setText("Email: ");
		metadataIdLabel.setText("Metadata Table ID: ");

		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(tableIdLabel);
		panel.add(tableIdArea);
		panel.add(firstNameLabel);
		panel.add(firstNameArea);
		panel.add(lastNameLabel);
		panel.add(lastNameArea);
		panel.add(emailLabel);
		panel.add(emailArea);
		panel.add(metadataIdLabel);
		panel.add(metadataIdArea);

		JButton enterButton = new JButton("Submit Changes");
        enterButton.setActionCommand("submitChanges");
        enterButton.addActionListener(this);
        enterButton.setMnemonic(KeyEvent.VK_ENTER);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        cancelButton.setMnemonic(KeyEvent.VK_ESCAPE);
		
        textAreaFrame.add(panel);

        JPanel buttonPane = new JPanel(new GridLayout(0, 2));
        buttonPane.add(enterButton, BorderLayout.LINE_START);
        buttonPane.add(cancelButton, BorderLayout.LINE_END);
        buttonPane.setBackground(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94));
        
        textAreaFrame.add(buttonPane, BorderLayout.AFTER_LAST_LINE);

        textAreaFrame.pack();
        textAreaFrame.setVisible(true);
	}
	
	public void submitChanges() {
		this.newId = tableIdArea.getText();
		this.newFirstName = firstNameArea.getText();
		this.newLastName = lastNameArea.getText();
		this.newEmail = emailArea.getText();
		this.newMetadataId = metadataIdArea.getText();
		this.controller.changeMetaData(this.newId, this.newFirstName, this.newLastName, this.newEmail, this.newMetadataId);
		this.controller.getFileDate();
		textAreaFrame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		String command = e.getActionCommand();
		if(command.equals("submitChanges")) {
			submitChanges();
		}
		if(command.equals("cancel")) {
			textAreaFrame.dispose();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {		
	}

	@Override
	public void keyPressed(KeyEvent e) {		
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}
}
