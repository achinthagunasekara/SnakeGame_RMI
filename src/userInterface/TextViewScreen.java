/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package userInterface;

import java.awt.Color;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextViewScreen extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea details;
	private JScrollPane scrollPane;
	
	//this screen can be used to display a block of text
	public TextViewScreen(String title, String text) {

		super(title , false, true, false, false);
	    setLocation(10, 10);
	    setSize(400, 400);
	    setBackground(Color.GRAY);
	    
		details = new JTextArea(5,20);
		details.setEditable(false);
		details.append(text);
		scrollPane = new JScrollPane(details);
		add(scrollPane);
		this.setVisible(true);
	}
}
