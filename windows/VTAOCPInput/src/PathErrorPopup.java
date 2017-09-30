import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PathErrorPopup extends JPanel implements ActionListener{
	JFrame frame;
	JPanel pane;
	Controller controller;
	
	public PathErrorPopup(Controller c, Exception e) {
		frame = new JFrame("Error");
		controller = c;
		pane = new JPanel(new BorderLayout(0, 3));
		JLabel label = new JLabel();
		label.setText(e.getMessage());
		pane.add(label, BorderLayout.BEFORE_FIRST_LINE);
		JPanel panel = new JPanel(new BorderLayout(0, 2));
		
		JButton b1 = new JButton();
		b1.setText("Create file at this location");
		b1.setActionCommand("createFile");
		b1.addActionListener(this);

		JButton b2 = new JButton();
		b2.setText("Change file path");
		b2.setActionCommand("changeFilePath");
		b2.addActionListener(this);
		
		panel.add(b2, BorderLayout.BEFORE_LINE_BEGINS);
		panel.add(b1, BorderLayout.AFTER_LINE_ENDS);
		pane.add(panel);
		frame.add(pane, BorderLayout.AFTER_LAST_LINE);
		frame.pack();
		frame.setVisible(true);
//				new PathWindow(this.dataPath, this.password, this.username, this)/*.panel.add(label, BorderLayout.BEFORE_FIRST_LINE*/;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("createFile")) {
			frame.dispose();
			SimpleDateFormat noTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
			controller.createNewFile(noTimeFormat.format(new Date()));
		}
		if(e.getActionCommand().equals("changeFilePath")) {
			frame.dispose();
			controller.openPathWindow();
		}
	}
}
