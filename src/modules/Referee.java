/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package modules;

import java.io.Serializable;
import database.DatabaseManager;
import gameItems.Game;

public class Referee implements Serializable {

	private static final long serialVersionUID = 1L;
	private Game game;
	
	public Referee(Game game) {
		
		this.game = game;
	}

	/*
	 * Check to see if two snakes are on top of each other
	 * and decide the which snake is the winner
	 */
	public synchronized void checkIfOnOtherSnake(String name) {
		
		Snake thisSnake = game.getGameData().getSnake(name);
		String head = thisSnake.getHead();
		thisSnake.setIsLastSnake(true);
		
		for(Snake snake : game.getGameData().getSnakes().values()) {

			if(!name.equals(snake.getName()) && snake.getAlive()) {

				if(head.equals(snake.getHead())) {
	
					checkForWinnerWhenHeadCollapse(thisSnake, snake);
				}
				else {
					
					//touching other snake would kill this snake
					if(snake.getBody().contains(head)) {
						
						thisSnake.setAlive(false);
					}
				}
				
				thisSnake.setIsLastSnake(false);
			}
		}
		
		//calculate score if snake is dead
		if(!thisSnake.getAlive() || thisSnake.isLastSnake()) {
		
			thisSnake.setSnakeScore(clacScore(name));
		}
	}
	
	private void checkForWinnerWhenHeadCollapse(Snake firstSnake, Snake secondSnake) {

		boolean fistSnakeAlive;
		
		if(firstSnake.getSnakeDirection() == Direction.DirectionTypes.NORTH && secondSnake.getSnakeDirection() == Direction.DirectionTypes.SOUTH) {
			
			fistSnakeAlive = (firstSnake.getBody().size() > secondSnake.getBody().size()) ? true : false;
		}
		else if(firstSnake.getSnakeDirection() == Direction.DirectionTypes.SOUTH && secondSnake.getSnakeDirection() == Direction.DirectionTypes.NORTH) {

			fistSnakeAlive = (firstSnake.getBody().size() > secondSnake.getBody().size()) ? true : false;
		}
		else if(firstSnake.getSnakeDirection() == Direction.DirectionTypes.EAST && secondSnake.getSnakeDirection() == Direction.DirectionTypes.WEST) {
			
			fistSnakeAlive = (firstSnake.getBody().size() > secondSnake.getBody().size()) ? true : false;
		}
		else if(firstSnake.getSnakeDirection() == Direction.DirectionTypes.WEST && secondSnake.getSnakeDirection() == Direction.DirectionTypes.EAST) {
			
			fistSnakeAlive = (firstSnake.getBody().size() > secondSnake.getBody().size()) ? true : false;
		}
		else {
			
			fistSnakeAlive = false;
		}
		
		firstSnake.setAlive(fistSnakeAlive);
		secondSnake.setAlive(!fistSnakeAlive);
	}
	
	public int clacScore(String name) {
		
		int score = 0;
		
		for(Snake s : game.getGameData().getSnakes().values()) {
			
			if(!s.getAlive() && !s.getName().equals(name)) {
				
				score++;
			}
		}
		
		//save score to the database
		DatabaseManager.getDatabaseManagerInstance().addPlayerScore(name, score);
		return score;
	}
}
