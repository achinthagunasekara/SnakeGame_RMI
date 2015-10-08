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
import java.util.HashMap;
import javax.swing.Timer;
import javax.swing.JInternalFrame;
import client.Client;
import messages.MessageTypes;
import modules.FoodItem;
import modules.Snake;

public class GameGrid extends  JInternalFrame {

	private static final long serialVersionUID = 1L;
	private MainWindow mw;
	private Client client;
	private int rows;
	private int cols;
	private HashMap<String, Square> map;
	private Timer timer;
	
	public GameGrid(MainWindow mw, Client client) throws RemoteException {

		super("Game Grid", false, false, false, false);
		
		this.mw = mw;
		this.client = client;
		this.rows = client.getClientManager().getGame().getBoard().getRows();
		this.cols = client.getClientManager().getGame().getBoard().getCols();
		
	    setLocation(10, 10);
	    setSize(680, 680);
	    setBackground(Color.GRAY);
		setLayout(new GridLayout(rows, cols));
		addKeyListener(new GameKeyListener());
		
		map = new HashMap<String, Square>();

		for(int i = 1; i <= rows; i++) {
		
			for(int j = 1; j <= cols; j++) {

				Square s = new Square(i, j);
				s.addKeyListener(new GameKeyListener());
				map.put(s.getName(), s);
				add(s);
			}
		}
		
		ActionListener listener = new ActionListener(){

			public void actionPerformed(ActionEvent event){

				try {
					refreshScreen();
					
				} catch (RemoteException e) {
					
					System.out.println(e.toString());
				}   
			}	  
		};
		
		timer = new Timer(100, listener);
	    setVisible(true);
	}
	
	public void startGame() {
		
		timer.start();
	}
	
	private void refreshScreen() throws RemoteException {
		
		//if not all players are ready, wait...
		if(!client.getClientManager().isReadyToStart()) {

			return;
		}
		
		for(Square s : map.values()){
			
			s.setColour(Color.gray);
			s.resetHead();
		}
	
		HashMap<String, Snake> currentData = client.getClientManager().getSnakes();
		FoodItem foodItem = client.getClientManager().getGame().getFoodItem();
		drawFoodItem(foodItem);
		
		for(Snake currentSnake : currentData.values()) {
			
			drawSnake(currentSnake);
		}
		
		MessageTypes.serverMessageTypes serverOutput = client.getClientManager().moveMySnake(client.getUserName());
		
		if(serverOutput == MessageTypes.serverMessageTypes.CONTINUE_PLAYING) {
			
			//do nothing
		}
		else {
		
			timer.stop();
			String msg;
			
			if(serverOutput == MessageTypes.serverMessageTypes.YOU_WON) {
				
				msg = "Your snake won!";
			}
			else if(serverOutput == MessageTypes.serverMessageTypes.YOU_LOST) {

				msg = "Your snake lost!";
			}
			else {
				
				msg = "Server response unknown";
			}
			
			msg +=  " Your score is " + client.getClientManager().getSnakeScore(client.getUserName());;
			
			mw.showMessage(msg);
			this.setVisible(false);
			mw.showSummaryScreen(client.getUserName(), client.getClientManager().getPlayerData(client.getUserName()));
		}
	}
	
	private void drawSnake(Snake snake) {
		
		Color c;
		
		if(snake.getName().equals(client.getUserName()) && snake.getAlive()) {
			
			c = Color.green;
		}
		else if(!snake.getAlive()) {
			
			c = Color.lightGray;
		}
		else {
			
			c = Color.red;
		}

		map.get(snake.getHead()).setColour(c);
		map.get(snake.getHead()).drawHead();
		
		for(int j = 0; j < snake.getBody().size(); j++) {
	
			map.get(snake.getBody().get(j)).setColour(c);
		}
	}
	
	private void drawFoodItem(FoodItem foodItem) {
		
		Color c = Color.pink;
		map.get(foodItem.getLoc()).setColour(c);
	}
}
