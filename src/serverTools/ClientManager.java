/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package serverTools;

import gameItems.Game;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import messages.MessageTypes;
import modules.Direction;
import modules.Snake;

public interface ClientManager extends Remote
{
	MessageTypes.serverMessageTypes joinGame() throws RemoteException;
	
	void setupGame(int rows, int cols, int numPlayers) throws RemoteException;
	
	void incrementCurrentNumberOfPlayers() throws RemoteException;
	
	Game getGame() throws RemoteException;
	
	HashMap<String, String> getAvailableLocations() throws RemoteException;
	
	void removeLocation(String location) throws RemoteException;
	
	void addSnake(Snake snake) throws RemoteException;
	
	Snake getSnake(String name) throws RemoteException;
	
	HashMap<String, Snake> getSnakes() throws RemoteException;
	
	MessageTypes.serverMessageTypes moveMySnake(String name) throws RemoteException;
	
	void setMySnakeDirection(String name, Direction.DirectionTypes directrion) throws RemoteException;
	
	void speedUpSnake(Boolean b, String name) throws RemoteException;
	
	int getSnakeScore(String name) throws RemoteException;
	
	boolean isReadyToStart() throws RemoteException;
	
	void registerNewPlayer(String firstName, String lastName, String address, String phone, String userName, String password) throws RemoteException;
	
	boolean authenticatePlayer(String user, String pass) throws RemoteException;
	
	String[][] getPlayerData(String userName) throws RemoteException;
	
	void addChatMessage(String userName, String msg) throws RemoteException;
	
	ArrayList<String> getChatMessages() throws RemoteException;
	
	String[] getPlayerDetails(String userName) throws RemoteException;
	
	void playerQuit(String userName) throws RemoteException;
}
