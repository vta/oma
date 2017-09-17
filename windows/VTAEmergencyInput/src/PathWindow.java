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
	
	public String newPath = null;
	public String newUsername = null;
	public String newPassword = null;
	private JLabel pathLabel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextArea pathArea;
	private JTextArea usernameArea;
	private JTextArea passwordArea;
	private JFrame textAreaFrame;
	private Controller controller;
	
	public PathWindow(String oldPath, String oldUsername, String oldPassword, Controller c){
		this.controller = c;
		this.newPath = oldPath;
		this.newUsername = oldUsername;
		this.newPassword = oldPassword;
		
		textAreaFrame = new JFrame("Change File Path");
		JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBackground(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94));
		
		pathLabel = new JLabel();
		usernameLabel = new JLabel();
		passwordLabel = new JLabel();
		
		pathArea = new JTextArea(oldPath, 1, 30);
		pathArea.setBorder(BorderFactory.createLineBorder(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94), 2));
		usernameArea = new JTextArea(oldUsername, 1, 30);
		usernameArea.setBorder(BorderFactory.createLineBorder(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94), 2));
		passwordArea = new JTextArea(oldPassword, 1, 30);
		passwordArea.setBorder(BorderFactory.createLineBorder(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94), 2));
		
		pathLabel.setText("File Path: ");
		usernameLabel.setText("Username: ");
		passwordLabel.setText("Password: ");

		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(pathLabel);
		panel.add(pathArea);
		panel.add(usernameLabel);
		panel.add(usernameArea);
		panel.add(passwordLabel);
		panel.add(passwordArea);
		
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
		this.newPath = pathArea.getText();
		this.newUsername = usernameArea.getText();
		this.newPassword = passwordArea.getText();
		this.controller.changeMetaData(this.newPath, this.newUsername, this.newPassword);
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
