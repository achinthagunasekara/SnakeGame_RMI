/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package userInterface;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.rmi.RemoteException;
import javax.swing.*;
import client.Client;

public class MainWindow extends JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;
	private Container content;
	private JDesktopPane desktop;
	
	//every window for this game will be displayed within the main window
	public MainWindow() {
		
		super("Snakes Game - Assignment 02");
	    content = getContentPane();
	    content.setBackground(Color.white);
	    
	    desktop = new JDesktopPane();
	    desktop.setBackground(Color.white);
	    content.add(desktop, BorderLayout.CENTER);
	    setSize(1150, 750);
	    addWindowListener(this);
	}
	
	public void showLoginScreen(Client client) {
		
		LoginScreen lScreen = new LoginScreen(this, client);
		showFrameOnDesktop(lScreen);
	}
	
	public void showGameSetup(boolean showAll, String[] locations, Client client) {

		GameSetupScreen gsc = new GameSetupScreen(this, showAll, locations, client);
		showFrameOnDesktop(gsc);
	}
	
	public void showSummaryScreen(String userName, String[][] data) {

		SummaryScreen ss = new SummaryScreen(userName, data);
		showFrameOnDesktop(ss);
	}
	
	public void showChatWindow() {

		ChatWindow cw = new ChatWindow(this);
		showFrameOnDesktop(cw);
	}
	
	public void showTextViewScreen(String title, String text) {
		
		TextViewScreen tvs = new TextViewScreen(title, text);
		showFrameOnDesktop(tvs);
	}
	
	public void showGameGrid(Client client) {
		
		GameGrid gg;
		
		try {
			
			setJMenuBar(new GameMenu(this));
			validate();
			showChatWindow();
			
			gg = new GameGrid(this, client);
			showFrameOnDesktop(gg);
			gg.startGame();
		}
		catch (RemoteException e) {
			
			showMessage(e.toString());
		}
	}
	
	//used to view a frame on screen and set it on top
	private void showFrameOnDesktop(JInternalFrame frame) {

		desktop.add(frame);
		this.validate();
		
		try {
			
			frame.setSelected(true);
		}
		catch (PropertyVetoException e) {
			
			showMessage(e.toString());
		}
	}
	
	public void showMessage(String s) {
		
		JOptionPane.showMessageDialog(this, s);
	}

	public JDesktopPane getDesktop() {
		
		return desktop;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Main window closing, exiting the application");
		Client.getClientInstance().exitGame();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
