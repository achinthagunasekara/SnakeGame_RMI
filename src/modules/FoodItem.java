/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package modules;

import gameItems.Game;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class FoodItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private int row;
	private int col;

	public FoodItem(){

	}
	
	public synchronized void resetFoodItem(Game game) {
		
		setupNewFoodItem(game);
	}
	
	/*
	 * Check to see if there are any snakes on a selected square
	 * if not create the food item on that square
	 */
	private void setupNewFoodItem(Game game) {

		ArrayList<Integer> takenRowVals = new ArrayList<Integer>();
		ArrayList<Integer> takenColVals = new ArrayList<Integer>();
		
		for(Snake s : game.getGameData().getSnakes().values()) {
		
			String[] headData = s.getHead().split(",");
			takenRowVals.add(Integer.parseInt(headData[0]));
			takenColVals.add(Integer.parseInt(headData[1]));
			
			ArrayList<String> bodyData = s.getBody();
			
			for(int i = 0; i < bodyData.size(); i++) {
				
				String[] eachBodyData = bodyData.get(i).split(",");
				takenRowVals.add(Integer.parseInt(eachBodyData[0]));
				takenColVals.add(Integer.parseInt(eachBodyData[1]));
			}
		}
		
		int maxXloc = game.getBoard().getRows();
		int maxYloc = game.getBoard().getCols();

		row = getRandomNumNotInList(takenRowVals, maxXloc);
		col = getRandomNumNotInList(takenColVals, maxYloc);
	}
	
	//pick a random square
	private int getRandomNumNotInList(ArrayList<Integer> list, int max) {
		
		Random random = new Random();
		int num = random.nextInt(max) + 1;
		
		while(list.contains(num)) {

			num = random.nextInt(max) + 1;
		}

		return num;
	}

	public synchronized String getLoc() {
		
		return (row + "," + col);
	}
}
