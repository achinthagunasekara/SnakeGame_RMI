/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package userInterface;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import client.Client;

public class LoginScreen extends  JInternalFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private MainWindow mw;
	private Client client;
	private String userName;
	private String password;
	
	private JLabel userNameLabel;
	private JLabel passwordLabel;
	
	private JTextField userNameInput;
	private JTextField passwordInput;
	
	private JButton register;
	private JButton login;
		
	public LoginScreen(MainWindow mw, Client client) {
		
		super("Login Screen", false, false, false, false);
		this.mw = mw;
		this.client = client;
	    setLocation(10, 10);
	    setSize(400, 400);
	    setBackground(Color.GRAY);
	    setLayout(new GridLayout(5, 2));
	    
	    userNameLabel = new JLabel("Username");
	    passwordLabel = new JLabel("Password");
	    
	    userNameInput = new JTextField();
	    passwordInput = new JTextField();

	    login = new JButton("Login");
	    register = new JButton("Register");
	    
	    add(userNameLabel);
	    add(userNameInput);
	    add(passwordLabel);
	    add(passwordInput);
	    add(new JLabel("")); //empty cell
	    add(new JLabel("")); //empty cell  
	    
	    register.addActionListener(this);
	    login.addActionListener(this);
	    	    
	    add(register);
	    add(login);
	    
	    setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if((JButton)e.getSource() == register) {
			
			UserRegistrationScreen urs = new UserRegistrationScreen(mw);
			this.setVisible(false);
			mw.getDesktop().add(urs);
			urs.setVisible(true);
		}
		else {
			
			userName = userNameInput.getText();
			password = passwordInput.getText();
			
			try {
				if(client.getClientManager().authenticatePlayer(userName, password)) {
					
					client.setUserName(userName);
					client.joinGame();
					this.setVisible(false);
				}
				else {
					
					mw.showMessage("Login Failed!\nPlease check your username and password");
				}
				
			} catch (RemoteException reEx) {
				
				mw.showMessage("Error - " + reEx.toString());
			}
		}
	}
}
