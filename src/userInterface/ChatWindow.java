/*
 * 2013
 * Archie Gunasekara
 */

package userInterface;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import client.Client;

public class ChatWindow extends  JInternalFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private MainWindow mw;
	private JTextArea chatView;
	private JTextField text;
	private JButton submit;
	private Timer timer;
		
	public ChatWindow(MainWindow mw) {
		
		super("ChatWindow - " + Client.getClientInstance().getUserName(), false, false, false, false);
		this.mw = mw;
	    setLocation(700, 10);
	    setSize(400, 400);
	    setLayout(new GridLayout(2, 1));
	    
	    chatView = new JTextArea();
	    chatView.setEditable(false);
	    JScrollPane scrollPane = new JScrollPane(chatView);
	    
	    text = new JTextField();
	    submit = new JButton("Send");
	    submit.addActionListener(this);
	    
	    JPanel topPanel = new JPanel();
	    topPanel.setLayout(new GridLayout(1, 1));
	    topPanel.add(scrollPane);
	    
	    JPanel bottomPanel = new JPanel();
	    bottomPanel.setLayout(new GridLayout(1, 2));
	    bottomPanel.add(text);
	    bottomPanel.add(submit);
	    
	    add(topPanel);
	    add(bottomPanel);
	    
	    ActionListener listener = new ActionListener(){

			public void actionPerformed(ActionEvent event){

				try {
					
					refreshScreen();
					
				} catch (RemoteException e) {
					
					System.out.println(e.toString());
				}   
			}	  
		};
		
		timer = new Timer(650, listener);
		timer.start();
	    setVisible(true);
	    
	    
	    setVisible(true);    
	}
	
	//refresh chat window and display new messages
	private void refreshScreen() throws RemoteException {
		
		StringBuilder sb = new StringBuilder();
		ArrayList<String> messages = Client.getClientInstance().getClientManager().getChatMessages();
		
		for(int i = 0; i < messages.size(); i++) {
			
			sb.append(messages.get(i) + "\n");
		}
		
		chatView.setText(sb.toString());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if((JButton)e.getSource() == submit) {
			
			try {
				
				Client.getClientInstance().getClientManager().addChatMessage(Client.getClientInstance().getUserName(), text.getText());
				text.setText("");
				
			}
			catch (RemoteException reEx) {
				
				mw.showMessage(reEx.toString());
			}
		}
	}
}