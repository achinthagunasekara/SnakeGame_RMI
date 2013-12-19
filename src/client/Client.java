/*
 * 2013
 * Archie Gunasekara
 */

package client;

import helpers.ConfigFileReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import messages.MessageTypes;
import serverTools.ClientManager;
import userInterface.MainWindow;

public class Client {

	private static Client instance = null;
	private MainWindow mw; 
	private ClientManager cManager;
	private String userName;
	
	public static void main(String args[]) {
		
		Client c = Client.getClientInstance();
		c.run();
	}
	
	public static Client getClientInstance() {

		if(instance == null) {
			
			instance = new Client();
		}
		
		return instance;
	}
	
	//there can only be one instance of this object and it can be obtained by calling getClientInstance() method
	private Client() {

	}
	
	private void run() {

		mw = new MainWindow();
		mw.setVisible(true);

		try {
			
			ConfigFileReader cfg = ConfigFileReader.getConfigFileReaderInstance();
			cManager = (ClientManager)Naming.lookup(cfg.getPropertyFor("RMI_URL") + ":" + cfg.getPropertyFor("RMI_PORT") + "/" + cfg.getPropertyFor("RMI_STUB"));
			mw.showLoginScreen(this);
		}
		catch (Exception re)
		{
			JOptionPane.showMessageDialog(mw, re.toString());
			System.exit(0);
		}
	}
	
	public void setUserName(String userName) {
		
		this.userName = userName;
	}
	
	public String getUserName() {
		
		return userName;
	}
	
	public ClientManager getClientManager() {
		
		return cManager;
	}
	
	public void joinGame() {
		
		MessageTypes.serverMessageTypes output;
		
		try {
			
			output = cManager.joinGame();
			
			if(output == MessageTypes.serverMessageTypes.SETUP_GAME) {
	
				cManager.incrementCurrentNumberOfPlayers();
				mw.showGameSetup(true, getAvailableLocations(), this);
			}
			//if the game is being setup, player has to try again later
			else if(output == MessageTypes.serverMessageTypes.BEING_SETUP) {
				
				mw.showMessage("Game is being setup, try again later!");
				System.exit(0);
			}
			else if(output == MessageTypes.serverMessageTypes.WELCOME) {
				
				cManager.incrementCurrentNumberOfPlayers();
				mw.showGameSetup(false, getAvailableLocations(), this);
			}
			else if(output == MessageTypes.serverMessageTypes.FULL) {
				
				mw.showMessage("Game is currently full, try again later");
				System.exit(0);
			}
			else {
				
				//this case should not happen
				mw.showMessage("Unknown response from the server - " + output);
				System.exit(0);
			}
		}
		catch (RemoteException e) {

			mw.showMessage(e.toString());
			System.exit(0);
		}
	}
	
	private String[] getAvailableLocations() {
		
		HashMap<String, String> locs = new HashMap<String, String>(); 
		
		try {
			
			locs = cManager.getAvailableLocations();
			
		} catch (RemoteException e) {
			
			mw.showMessage(e.toString());
		}
		
		return locs.values().toArray(new String[0]);
	}
	
	//remove snake from the server and exit game
	public void exitGame() {
		
		try {
			
			cManager.playerQuit(userName);
		}
		catch (RemoteException reEx) {
			
			mw.showMessage("Error removing snake from the game - " + reEx.toString());
		}
		
		System.exit(0);
	}
}