/*
 * 2013
 * Archie Gunasekara
 */

package userInterface;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import client.Client;
import modules.Direction;
import modules.Snake;

class GameKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		
		//do nothing
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == 38) {
			
			changeSnakeSpeed(Direction.KeyTypes.UP);
		}
		else if(e.getKeyCode() == 39) {
			
			changeSnakeDirection(Direction.KeyTypes.RIGHT);
		}
		else if(e.getKeyCode() == 40) {
			
			changeSnakeSpeed(Direction.KeyTypes.DOWN);
		}
		else if(e.getKeyCode() == 37) {

			changeSnakeDirection(Direction.KeyTypes.LEFT);
		}
		else {
			//do nothing
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		//do nothing		
	}
	
	private void changeSnakeSpeed(Direction.KeyTypes k) {
	
		Client client = Client.getClientInstance();

		try {
			
			if(k == Direction.KeyTypes.UP) {
				
				client.getClientManager().speedUpSnake(true, client.getUserName());
			}
			else if(k == Direction.KeyTypes.DOWN) {
				
				client.getClientManager().speedUpSnake(false, client.getUserName());
			}
		}
		catch (RemoteException e) {

			e.printStackTrace();
		}
	}

	private void changeSnakeDirection(Direction.KeyTypes k) {
		
		Client client = Client.getClientInstance();
		Snake mySnake;
		
		try {
			
			mySnake = client.getClientManager().getSnake(client.getUserName());
			Direction.DirectionTypes d = getSelectedDirection(mySnake.getSnakeDirection(), k);
			client.getClientManager().setMySnakeDirection(client.getUserName(), d);
		}
		catch (RemoteException e) {

			e.printStackTrace();
		}
	}
	
	private Direction.DirectionTypes getSelectedDirection(Direction.DirectionTypes d, Direction.KeyTypes k) {

		if(d == Direction.DirectionTypes.NORTH) {
			
			if(k == Direction.KeyTypes.RIGHT) {
				
				return Direction.DirectionTypes.EAST;
			}
			else {
				
				return Direction.DirectionTypes.WEST;
			}
		}
		else if(d == Direction.DirectionTypes.EAST) {
			
			if(k == Direction.KeyTypes.RIGHT) {
				
				return Direction.DirectionTypes.SOUTH;
			}
			else {
				
				return Direction.DirectionTypes.NORTH;
			}
		}
		else if(d == Direction.DirectionTypes.SOUTH) {
			
			if(k == Direction.KeyTypes.RIGHT) {
				
				return Direction.DirectionTypes.EAST;
			}
			else {
				
				return Direction.DirectionTypes.WEST;
			}
		}
		else {
			
			if(k == Direction.KeyTypes.RIGHT) {
				
				return Direction.DirectionTypes.NORTH;
			}
			else {
				
				return Direction.DirectionTypes.SOUTH;
			}
		}
	}
}