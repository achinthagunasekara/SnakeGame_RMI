/*
 * 2013
 * Archie Gunasekara
 */

package gameItems;

import java.io.Serializable;
import java.util.HashMap;
import modules.FoodItem;
import modules.Referee;
import modules.StartLocations;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean needToSetup = true;
	private boolean isBeingSetup = false;
	private boolean isGameFinished = false;
	private int maxNumberOfPlayers = 0;
	private int currentNumberOfPlayers = 0;
	private Board board;
	private HashMap<String, String> availableLocations;
	private GameData gameData;
	private FoodItem foodItem;
	private Referee referee;
	
	public Game() {
		
		setupLocations();
		gameData = new GameData();
		foodItem = new FoodItem();
		referee = new Referee(this);
	}

	public synchronized boolean isNeedToSetup() {
		
		return needToSetup;
	}
	public synchronized void setNeedToSetup(boolean needToSetup) {
		
		this.needToSetup = needToSetup;
	}
	
	public synchronized boolean isBeingSetup() {
		
		return isBeingSetup;
	}
	
	public synchronized void setBeingSetup(boolean isBeingSetup) {
		
		this.isBeingSetup = isBeingSetup;
	}
	
	public synchronized boolean isGameFull() {

		return (maxNumberOfPlayers == currentNumberOfPlayers) ? true : false;
	}
	
	public synchronized boolean isGameFinished() {

		return isGameFinished;
	}
	
	public void setGameFinished(boolean isGameFinished) {

		this.isGameFinished = isGameFinished;
	}
	
	public synchronized int getMaxNumberOfPlayers() {
		
		return maxNumberOfPlayers;
	}
	
	public synchronized void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
		
		this.maxNumberOfPlayers = maxNumberOfPlayers;
	}
	
	public int getCurrentNumberOfPlayers() {
		
		return currentNumberOfPlayers;
	}
	
	public synchronized void incrementCurrentNumberOfPlayers() {

		currentNumberOfPlayers++;
	}
	
	public synchronized Board getBoard() {
		
		return board;
	}
	
	public synchronized void setBoard(Board board) {
		
		this.board = board;
	}
	
	public synchronized int getNumberOfPlayersReadyToStart() {
		
		return gameData.getSnakes().size();
	}
	
	public synchronized void setupLocations() {
		
		availableLocations = new HashMap<String, String>();
		availableLocations.put(StartLocations.locations.TOP_LEFT.toString(), StartLocations.locations.TOP_LEFT.toString());
		availableLocations.put(StartLocations.locations.TOP_RIGHT.toString(), StartLocations.locations.TOP_RIGHT.toString());
		availableLocations.put(StartLocations.locations.BOTTOM_LEFT.toString(), StartLocations.locations.BOTTOM_LEFT.toString());
		availableLocations.put(StartLocations.locations.BOTTOM_RIGHT.toString(), StartLocations.locations.BOTTOM_RIGHT.toString());
	}
	
	public synchronized HashMap<String, String> getAvailableLocations() {

		return availableLocations;
	}
	
	public synchronized void removeLocationFromList(String loc) {
		
		availableLocations.remove(loc);
	}
	
	public synchronized GameData getGameData() {
	
		return gameData;
	}

	public synchronized FoodItem getFoodItem() {
		
		return foodItem;
	}

	public synchronized void setFoodItem(FoodItem foodItem) {
		
		this.foodItem = foodItem;
	}
	
	public synchronized boolean isGameReadyToStart() {
	
		return (gameData.getSnakes().size() == maxNumberOfPlayers);
	}
	
	public synchronized Referee getReferee() {
		
		return referee;
	}
}