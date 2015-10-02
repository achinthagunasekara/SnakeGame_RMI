/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package gameItems;

import java.io.Serializable;
import java.util.HashMap;
import modules.Snake;

public class GameData implements Serializable {

	private static final long serialVersionUID = 1L;
	private HashMap<String, Snake> data;
	
	public GameData() {
		
		data = new HashMap<String, Snake>();
	}
	
	public synchronized void addSnake(Snake snake) {
		
		data.put(snake.getName(), snake);
	}
	
	public synchronized Snake getSnake(String name) {
		
		return data.get(name);
	}
	
	public synchronized HashMap<String, Snake> getSnakes() {
		
		return data;
	}
	
	//if a player quit make their snake as dead
	public synchronized void playerQuit(String name) {
		
		data.get(name).setAlive(false);
	}
}
