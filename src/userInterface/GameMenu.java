/*
 * 2013
 * Archie Gunasekara
 */

package userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import client.Client;

public class GameMenu extends JMenuBar implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JMenu menu;
	private JMenuItem myScores, aboutPlayer, about, exit;
	private MainWindow mw;
	
	public GameMenu(MainWindow mw) {
		
		super();
		this.mw = mw;
		createMenu();
	}

	private void createMenu() {
		
		//menu
		menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_M);
		menu.getAccessibleContext().setAccessibleDescription("Create new items");
		add(menu);

		//Options in menu
		myScores = new JMenuItem("My Scores");
		myScores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		myScores.getAccessibleContext().setAccessibleDescription("View my Scores");
		myScores.addActionListener(this);		
		menu.add(myScores);
		
		//Options in menu
		aboutPlayer = new JMenuItem("About Player");
		aboutPlayer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		aboutPlayer.getAccessibleContext().setAccessibleDescription("About Logged in Player");
		aboutPlayer.addActionListener(this);		
		menu.add(aboutPlayer);
		
		//Options in menu
		about = new JMenuItem("About Game");
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
		about.getAccessibleContext().setAccessibleDescription("About Snake Game");
		about.addActionListener(this);		
		menu.add(about);
		
		//Options in menu
		exit = new JMenuItem("Exit");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
		exit.getAccessibleContext().setAccessibleDescription("Exit Game");
		exit.addActionListener(this);		
		menu.add(exit);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JMenuItem itemSelected = (JMenuItem)e.getSource();
		
		if(itemSelected == myScores) {
			
			try {
				
				mw.showSummaryScreen(Client.getClientInstance().getUserName(), Client.getClientInstance().getClientManager().getPlayerData(Client.getClientInstance().getUserName()));
			}
			catch (RemoteException reEx) {
				
				mw.showMessage(reEx.toString());
			}
		}
		else if(itemSelected == aboutPlayer) {
			
			mw.showTextViewScreen("About Logged in Player", getAboutPlayer());
		}
		else if(itemSelected == about) {
			
			mw.showTextViewScreen("About Snake Game", getAbout());
		}
		else if(itemSelected == exit) {
			
			Client.getClientInstance().exitGame();
		}
		else {
		
			System.out.println("This isn't ready yet");
		}
	}
	
	private String getAbout() {
	
		StringBuffer sb = new StringBuffer();
		sb.append("Team Member : Thambawitage Don Achintha Nilupul Gunasekara (S3369984)\n");
		sb.append("Team Member : Prateek Nayak (S3358823)\n");
		sb.append("Subject: COSC2401 - Software Architecture: Design and Implementation\n");
		sb.append("Assignment 02");
		
		return sb.toString();
	}
	
	private String getAboutPlayer() {
		
		StringBuffer sb = new StringBuffer();
		
		try {
			
			String[] s = Client.getClientInstance().getClientManager().getPlayerDetails(Client.getClientInstance().getUserName());
			
			sb.append("First Name : " + s[0] + "\n");
			sb.append("Last Name : " + s[1] + "\n");
			sb.append("Address : " + s[2] + "\n");
			sb.append("Phone Number : " + s[3] + "\n");
			sb.append("User Name : " + s[4] + "\n");
			sb.append("Password : " + s[5] + "\n");
		}
		catch (RemoteException reEx) {
			
			mw.showMessage(reEx.toString());
		}
		
		return sb.toString();
	}
}