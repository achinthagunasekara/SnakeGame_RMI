/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package modules;

import gameItems.Game;
import java.io.Serializable;
import java.util.ArrayList;
import modules.Direction.DirectionTypes;

public class Snake implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String head;
	private ArrayList<String> body;
	private Direction.DirectionTypes snakeDirection;
	private int speed = 1;
	private long lastMoveTime = 0;
	private boolean alive = true;
	private int score = 0;
	private boolean lastSnake = false;
	
	public Snake(String name, StartLocations.locations loc, int rows, int cols) {
		
		this.name = name;
		body = new ArrayList<String>();
		initializeSnake(loc, rows, cols);
	}
	
	//setup snake based on the location picked by the player
	private void initializeSnake(StartLocations.locations loc, int rows, int cols) {
		
		if(loc == StartLocations.locations.TOP_LEFT) {
			
			head = "1,2";
			body.add("1,1");
			setSnakeDirection(Direction.DirectionTypes.EAST);
		}
		else if(loc == StartLocations.locations.TOP_RIGHT) {
			
			head = "1," + (cols - 1); 
			body.add("1," + cols);
			setSnakeDirection(Direction.DirectionTypes.WEST);
		}
		else if(loc == StartLocations.locations.BOTTOM_LEFT) {
			
			head = rows + "," + 2; 
			body.add(rows + "," + 1);
			setSnakeDirection(Direction.DirectionTypes.EAST);
		}
		else if(loc == StartLocations.locations.BOTTOM_RIGHT) {
			
			head = rows + "," + (cols - 1); 
			body.add(rows + "," + cols);
			setSnakeDirection(Direction.DirectionTypes.WEST);
		}
	}

	public String getName() {
		
		return name;
	}

	public String getHead() {
		
		return head;
	}

	public ArrayList<String> getBody() {
		
		return body;
	}

	public Direction.DirectionTypes getSnakeDirection() {
		
		return snakeDirection;
	}

	public void setSnakeDirection(Direction.DirectionTypes snakeDirection) {
		
		this.snakeDirection = snakeDirection;
	}
	
	public void speedUp() {

		if(speed < 3) {

			speed++;
		}
	}
	
	public void resetSpeed() {
		
		if(speed > 1) {

			speed = 1;
		}
		else {
			
			/*
			 * if the snake is moving on the slowest speed and slow down is pressed
			 * reverse the snake direction
			 */
			reverseSnake();
		}
	}
	
	private boolean speedTick() {
		
		if(lastMoveTime == 0) {
		
			lastMoveTime = System.nanoTime();
		}
		
		long now = System.nanoTime();
		
		if(speed == 3) {

			if((now - lastMoveTime) > 100000000) {

				lastMoveTime = now;
				return true;
			}
		}
		else if ((speed == 2)) {

			if((now - lastMoveTime) > 500000000) {

				lastMoveTime = now;
				return true;
			}
			
		}
		else if ((speed == 1)) {

			if((now - lastMoveTime) > 900000000) {

				lastMoveTime = now;
				return true;
			}
		}
		
		return false;
	}
	
	private void reverseSnake() {
		
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add(head);
		
		for(int i = 1; i < body.size(); i++) {
			
			tmp.add(body.get(i));
		}
		
		head = body.get(0);
		body = tmp;
		
		if(snakeDirection == Direction.DirectionTypes.NORTH) {
			
			setSnakeDirection(Direction.DirectionTypes.SOUTH);
		}
		else if(snakeDirection == Direction.DirectionTypes.SOUTH) {
			
			setSnakeDirection(Direction.DirectionTypes.NORTH);
		}
		else if(snakeDirection == Direction.DirectionTypes.EAST) {
			
			setSnakeDirection(Direction.DirectionTypes.WEST);
		}
		else if(snakeDirection == Direction.DirectionTypes.WEST) {
			
			setSnakeDirection(Direction.DirectionTypes.EAST);
		}
	}
	
	public boolean moveOneSpot(Game game) {
		
		if(speedTick() && alive) {
			
			moveSnakeByOneSpot(game);
		}
		
		return alive;
	}

	private int[] splitLoc(String loc) {
		
		String[] s = loc.split(",");
		int[] arr = new int[s.length];
		
		for(int i = 0; i < s.length; i++) {
			
			arr[i] = Integer.parseInt(s[i]);
		}
		
		return arr;
	}
	
	public void moveSnakeByOneSpot(Game game) {

		if(snakeDirection == DirectionTypes.NORTH) {
			
			moveNorth(game);
		}
		else if(snakeDirection == DirectionTypes.EAST) {
			
			moveEast(game);
		}
		else if(snakeDirection == DirectionTypes.SOUTH) {
			
			moveSouth(game);
		}
		else if(snakeDirection == DirectionTypes.WEST) {
			
			moveWest(game);
		}
		else {
			//unknown direction do nothing
		}
	}
	
	private void moveNorth(Game game) {

		int[] currentHead = splitLoc(head);
		int[] newHead = new int[2];
		
		newHead[0] = currentHead[0] - 1;
		newHead[1] = currentHead[1];
		
		if(newHead[0] < 1) {
			
			newHead[0] = game.getBoard().getRows();
		}
		
		moveSnake(newHead[0], newHead[1], game);
	}
	
	private void moveEast(Game game) {

		int[] currentHead = splitLoc(head);
		int[] newHead = new int[2];
		
		newHead[0] = currentHead[0];
		newHead[1] = currentHead[1] + 1;
		
		if(newHead[1] > game.getBoard().getCols()) {
			
			newHead[1] = 1;
		}
		
		moveSnake(newHead[0], newHead[1], game);
	}
	
	private void moveSouth(Game game) {

		int[] currentHead = splitLoc(head);
		int[] newHead = new int[2];
		
		newHead[0] = currentHead[0] + 1;
		newHead[1] = currentHead[1];
		
		if(newHead[0] > game.getBoard().getRows()) {
			
			newHead[0] = 1;
		}
		
		moveSnake(newHead[0], newHead[1], game);
	}
	
	private void moveWest(Game game) {

		int[] currentHead = splitLoc(head);
		int[] newHead = new int[2];
		
		newHead[0] = currentHead[0];
		newHead[1] = currentHead[1] - 1;
		
		if(newHead[1] < 1) {
			
			newHead[1] = game.getBoard().getCols();
		}
		
		moveSnake(newHead[0], newHead[1], game);
	}
	
	private void moveSnake(int newHeadRow, int newHeadCol, Game game) {

		String previousHead = head;
		head = newHeadRow + "," + newHeadCol;
		
		ArrayList<String> tempSquares = new ArrayList<String>();
		
		int j;
		
		if(checkIfHeadOnFood(game)) {

			j = 0;
		}
		else {
			
			j = 1;
		}
		
		for(int i = j; i < body.size(); i++) {
			
			tempSquares.add(body.get(i));
		}
		
		tempSquares.add(previousHead);
		body = tempSquares;
	}
	
	private boolean checkIfHeadOnFood(Game game) {
		
		FoodItem foodItem = game.getFoodItem();
		
		if(head.equals(foodItem.getLoc())) {

			foodItem.resetFoodItem(game);
			return true;
		}
		
		return false;
	}
	
	public void setAlive(boolean b) {
		
		this.alive = b;
	}

	public Boolean getAlive() {
		
		return alive;
	}
	
	public void setSnakeScore(int score) {
		
		this.score = score;
	}
	
	public int getSnakeScore() {
		
		return score;
	}
	
	public void setIsLastSnake(boolean b) {
		
		this.lastSnake = b;
	}
	
	public boolean isLastSnake() {
		
		return lastSnake;
	}
}
