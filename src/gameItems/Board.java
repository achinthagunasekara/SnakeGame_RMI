/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package gameItems;

import java.io.Serializable;

public class Board implements Serializable {

	private static final long serialVersionUID = -8469618236176913234L;
	private int rows;
	private int cols;
	
	public Board(int rows, int cols) {
		
		this.rows = rows;
		this.cols = cols;
	}
	
	public synchronized int getRows() {
		
		return rows;
	}

	public synchronized int getCols() {
		
		return cols;
	}
}
