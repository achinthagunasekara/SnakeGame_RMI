/*
 * 2013
 * Archie Gunasekara
 */

package userInterface;

import java.awt.Color;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SummaryScreen extends  JInternalFrame {

	private static final long serialVersionUID = 1L;
	String userName;
	
	//this screen is used to display score summary for a user
	public SummaryScreen(String userName, String[][] data) {
		
		super("Game History for player - " + userName , false, true, false, false);
		this.userName = userName;
	    setLocation(10, 10);
	    setSize(400, 400);
	    setBackground(Color.GRAY);

		String[] headers = {"Date", "Score"}; 
		DefaultTableModel model = new DefaultTableModel(data, headers); 
		JTable table = new JTable(model); 
		JScrollPane scroll = new JScrollPane(table); 
		getContentPane().add(scroll);
		setVisible(true);
	}
}