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
	private JLabel tableIdLabel;
	private JTextArea tableIdArea;
	private JFrame textAreaFrame;
	private Controller controller;
	
	public PathWindow(String oldId, Controller c){
		this.controller = c;
		this.newId = oldId;
		
		textAreaFrame = new JFrame("Change File Path");
		JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBackground(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94));
		
		tableIdLabel = new JLabel();
		
		tableIdArea = new JTextArea(oldId, 1, 30);
		tableIdArea.setBorder(BorderFactory.createLineBorder(Color.getHSBColor((float)0.617, (float)0.53, (float)0.94), 2));

		tableIdLabel.setText("Table ID: ");

		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(tableIdLabel);
		panel.add(tableIdArea);

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
		this.controller.changeMetaData(this.newId);
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
