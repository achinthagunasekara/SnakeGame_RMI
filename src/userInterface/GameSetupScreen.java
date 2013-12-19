/*
 * 2013
 * Archie Gunasekara
 */

package userInterface;

import gameItems.Board;
import gameItems.Game;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import client.Client;
import modules.Snake;
import modules.StartLocations;

public class GameSetupScreen extends  JInternalFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private Client client;
	
	private int numPlayersEntered;
	private int numRowsEntered;
	private int numColsEntered;
	private String locEntered;
	private MainWindow mw;
	private boolean showAll;
	
	private JLabel numPlayersLabel;
	private JComboBox<String> numPlayers;
	private JLabel locLabel;
	private JComboBox<String> loc;
	private JLabel numRowsLabel;
	private JTextField numRows;
	private JLabel numColsLabel;
	private JTextField numCols;
	private JButton enter;
	
	public GameSetupScreen(MainWindow mw, boolean showAll, String[] locations, Client client) {
		
		super("Game Setup", false, false, false, false);
		this.mw = mw;
		this.showAll = showAll;
		this.client = client;
	    setLocation(10, 10);
	    setSize(400, 400);
	    setBackground(Color.GRAY);
	    setLayout(new GridLayout(9, 2));
	    
	    locLabel = new JLabel("Players Location");	    
	    loc = new JComboBox<String>(locations);
	       
	    numPlayersLabel = new JLabel("Number of Players (2 - 4)");
	    String[] numPlayersVals = {"2", "3", "4"};
	    numPlayers = new JComboBox<String>(numPlayersVals);
	    
	    numRowsLabel = new JLabel("Number of rows (10 - 40)");
	    numRows = new JTextField("10");
	    
	    numColsLabel = new JLabel("Number of cols (10 - 40)");
	    numCols = new JTextField("10");
	    
	    enter = new JButton("Enter");
	    enter.addActionListener(this);
	    

	    add(locLabel);
	    add(loc);
	    add(new JLabel("")); //empty cell
	    add(new JLabel("")); //empty cell   

	    if(showAll) {
	    	
		    add(numPlayersLabel);
		    add(numPlayers);
		    add(new JLabel("")); //empty cell
		    add(new JLabel("")); //empty cell   
		    
		    add(numRowsLabel);
		    add(numRows);
		    add(new JLabel("")); //empty cell
		    add(new JLabel("")); //empty cell
	
		    add(numColsLabel);
		    add(numCols);
		    add(new JLabel("")); //empty cell
		    add(new JLabel("")); //empty cell
		    
	    }
	    
	    add(new JLabel("")); //empty cell
	    add(enter);
	    
	    setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		locEntered = loc.getSelectedItem().toString();
		
		if(showAll) {
			
			sendFullGameSetup();
		}
		
		setUpMySnakeAndRun(locEntered);
	}
	
	private void sendFullGameSetup() {
	
		try {
			
			numPlayersEntered = Integer.parseInt(numPlayers.getSelectedItem().toString());
			numRowsEntered = Integer.parseInt(numRows.getText());
			numColsEntered = Integer.parseInt(numCols.getText());
			
			if(numRowsEntered < 10 || numRowsEntered > 40 || numColsEntered < 10 || numColsEntered > 40) {
				
				throw new Exception("Minimum value rows and columns is 10 and max value is 40");
			}
			
			client.getClientManager().setupGame(numRowsEntered, numColsEntered, numPlayersEntered);
		}
		catch(NumberFormatException nfEx) {
			
			JOptionPane.showMessageDialog(this, "Invalid Values");
		}
		catch(Exception ex) {
			
			JOptionPane.showMessageDialog(this, ex.toString());
		}
	}
	
	private void setUpMySnakeAndRun(String locEntered) {
		
		try {
		
			Game game = client.getClientManager().getGame();
			Board board = game.getBoard();
			Snake mySnake = new Snake(client.getUserName(), StartLocations.locations.valueOf(locEntered), board.getRows(), board.getCols());
			client.getClientManager().removeLocation(locEntered);
			client.getClientManager().addSnake(mySnake);
			this.setVisible(false);
			mw.showGameGrid(client);
		}
		catch (RemoteException rEx) {
		
			mw.showMessage(rEx.toString());
		}
	}
}