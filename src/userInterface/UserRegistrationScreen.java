/*
 * 2013
 * Archie Gunasekara
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

public class UserRegistrationScreen extends  JInternalFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private MainWindow mw;
	private String firstName;
	private String lastName;
	private String address;
	private String phoneNumber;
	private String userName;
	private String password;
	
	private JLabel firstNameLabel;
	private JLabel lastNameLabel;
	private JLabel addressLabel;
	private JLabel phoneNumberLabel;
	private JLabel userNameLabel;
	private JLabel passwordLabel;
	
	private JTextField firstNameInput;
	private JTextField lastNameInput;
	private JTextField addressInput;
	private JTextField phoneNumberInput;
	private JTextField userNameInput;
	private JTextField passwordInput;
	
	private JButton submit;
		
	public UserRegistrationScreen(MainWindow mw) {
		
		super("New User Regisration Screen", false, false, false, false);
		this.mw = mw;
	    setLocation(10, 10);
	    setSize(400, 400);
	    setBackground(Color.GRAY);
	    setLayout(new GridLayout(9, 2));
	    
	    firstNameLabel = new JLabel("First Name");
	    lastNameLabel = new JLabel("Last Name");
	    addressLabel = new JLabel("Address");
	    phoneNumberLabel = new JLabel("Phone Number");
	    userNameLabel = new JLabel("Username");
	    passwordLabel = new JLabel("Password");
	    
	    firstNameInput = new JTextField();
	    lastNameInput = new JTextField();
	    addressInput = new JTextField();
	    phoneNumberInput = new JTextField();
	    userNameInput = new JTextField();
	    passwordInput = new JTextField();

	    submit = new JButton("Save");
	    
	    add(firstNameLabel);
	    add(firstNameInput);
	    add(lastNameLabel);
	    add(lastNameInput);
	    add(addressLabel);
	    add(addressInput);
	    add(phoneNumberLabel);
	    add(phoneNumberInput);
	    add(userNameLabel);
	    add(userNameInput);
	    add(passwordLabel);
	    add(passwordInput);
	    add(new JLabel("")); //empty cell
	    add(new JLabel("")); //empty cell  
	    add(new JLabel("")); //empty cell

	    submit.addActionListener(this);
	    
	    add(submit);
	        
	    setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		firstName = firstNameInput.getText();
		lastName = lastNameInput.getText();
		address = addressInput.getText();
		phoneNumber = phoneNumberInput.getText();
		userName = userNameInput.getText();
		password = passwordInput.getText();

		if(firstName.equals("") || userName.equals("") || password.equals("")) {
			
			mw.showMessage("You must enter follwing fields\n\n1. First Name\n2. UserName\n3. Password");
			return;
		}
		
		try {
			
			Client.getClientInstance().getClientManager().registerNewPlayer(firstName, lastName, address, phoneNumber, userName, password);
			Client.getClientInstance().setUserName(userName);
			Client.getClientInstance().joinGame();
			this.setVisible(false);
		}
		catch (RemoteException reEx) {
		
			mw.showMessage(reEx.toString());
		}
	}
}