/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package server;

import gameItems.Board;
import gameItems.Game;
import helpers.ConfigFileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import database.DatabaseManager;
import messages.MessageTypes;
import modules.ChatManager;
import modules.Direction;
import modules.FoodItem;
import modules.Snake;
import serverTools.ClientManager;

public class Server extends UnicastRemoteObject implements ClientManager
{
	private static final long serialVersionUID = 1L;
	private static final int PORT = Integer.parseInt(ConfigFileReader.getConfigFileReaderInstance().getPropertyFor("RMI_PORT"));
	private static final String POLICY_FILE = ConfigFileReader.getConfigFileReaderInstance().getPropertyFor("POLICY_FILE");
	private static final String RMI_URL = ConfigFileReader.getConfigFileReaderInstance().getPropertyFor("RMI_URL");
	private gameItems.Game game;
	private ChatManager chatManager;
	
	public Server() throws RemoteException {

	}

	public static void main(String args[]) throws IOException{

		//needs to be before setting the security manager
		System.setProperty("java.security.policy", POLICY_FILE);
		System.setSecurityManager(new SecurityManager());
		LocateRegistry.createRegistry(PORT);
		
		try {
			
			Server server = new Server();
			Naming.rebind(RMI_URL + ":" + PORT + "/ClientManager", server);
			System.out.println("Snake Game Server Has Started!");	
		}
		catch (RemoteException re) {
			
			re.printStackTrace();
		}
		catch (MalformedURLException me) {
			
			me.printStackTrace();
		}
	}
	
	//handles client join requests to the current game
	public MessageTypes.serverMessageTypes joinGame() {
		
		//if no game is setup or current game is finished, start a new game
		if(game == null || game.isGameFinished()) {
			
			game = new Game();
			chatManager = new ChatManager();
			System.out.println("Creating new game...");
		}
		
		if(game.isNeedToSetup()) {
			
			game.setNeedToSetup(false);
			game.setBeingSetup(true);
			return MessageTypes.serverMessageTypes.SETUP_GAME;
		}
		else if(game.isBeingSetup()) {
			
			return MessageTypes.serverMessageTypes.BEING_SETUP;
		}
		else if(game.isGameFull()) {
			
			return MessageTypes.serverMessageTypes.FULL;
		}
		else {
			
			return MessageTypes.serverMessageTypes.WELCOME;
		}
	}
	
	public void setupGame(int rows, int cols, int numPlayers) {
		
		Board board = new Board(rows, cols);
		game.setBoard(board);
		game.setMaxNumberOfPlayers(numPlayers);
		game.setBeingSetup(false);
		FoodItem foodItem = new FoodItem();
		foodItem.resetFoodItem(game);
		game.setFoodItem(foodItem);
	}
	
	public void incrementCurrentNumberOfPlayers() {
		
		game.incrementCurrentNumberOfPlayers();
	}
	
	public Game getGame() {
		
		return game;
	}
	
	public HashMap<String, String> getAvailableLocations() {
		
		return this.game.getAvailableLocations();
	}
	
	public void removeLocation(String location) {
	
		this.game.removeLocationFromList(location);
	}
	
	public void addSnake(Snake snake) {
		
		game.getGameData().addSnake(snake);
	}
	
	public Snake getSnake(String name) {
		
		return game.getGameData().getSnake(name);
	}
	
	public HashMap<String, Snake> getSnakes() {
		
		return game.getGameData().getSnakes();
	}
	
	public MessageTypes.serverMessageTypes moveMySnake(String name) {
		
		Snake s = game.getGameData().getSnake(name);
		//check if there are any crashes
		game.getReferee().checkIfOnOtherSnake(name);

		if(s.moveOneSpot(game) && !s.isLastSnake()) {
			
			return MessageTypes.serverMessageTypes.CONTINUE_PLAYING;
		}
		else if(s.isLastSnake()) {
			
			game.setGameFinished(true);
			System.out.println("Current game has ended!");
			return MessageTypes.serverMessageTypes.YOU_WON;
		}
		else {
			
			return MessageTypes.serverMessageTypes.YOU_LOST;
		}
	}
	
	public void setMySnakeDirection(String name, Direction.DirectionTypes directrion) {
		
		Snake s = game.getGameData().getSnake(name);
		s.setSnakeDirection(directrion);
	}
	
	public void speedUpSnake(Boolean b, String name) {
		
		Snake s = game.getGameData().getSnake(name);
		
		if(b) {
			
			s.speedUp();
		}
		else {
			
			s.resetSpeed();
		}
	}
	
	public int getSnakeScore(String name) {
		
		Snake s = game.getGameData().getSnake(name);
		return s.getSnakeScore();
	}
	
	public boolean isReadyToStart() {
		
		return game.isGameReadyToStart();
	}
	
	public void registerNewPlayer(String firstName, String lastName, String address, String phone, String userName, String password) throws RemoteException {
		
		try {
			
			DatabaseManager.getDatabaseManagerInstance().registerNewPlayer(firstName, lastName, address, phone, userName, password);
		}
		catch (Exception ex) {
			
			throw new RemoteException(ex.toString());
		}
	}
	
	public boolean authenticatePlayer(String user, String pass) {
		
		return DatabaseManager.getDatabaseManagerInstance().authenticatePlayer(user, pass);
	}
	
	public String[][] getPlayerData(String userName) {
		
		return DatabaseManager.getDatabaseManagerInstance().getPlayerData(userName);
	}

	public void addChatMessage(String userName, String msg) {
	
		chatManager.addMessage(userName, msg);
	}

	public ArrayList<String> getChatMessages() throws RemoteException {

		return chatManager.getMessages();
	}
	
	public String[] getPlayerDetails(String userName) {
		
		return DatabaseManager.getDatabaseManagerInstance().getPlayerDetails(userName);
	}
	
	public void playerQuit(String name) {
		
		game.getGameData().playerQuit(name);
		game.getReferee().clacScore(name);
	}
}
