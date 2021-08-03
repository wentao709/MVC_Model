import java.util.Observable;

import javafx.scene.paint.Color;

/**
 * Connect4Model
 * the model of the connect4 game, it store the states of the board and it interact with the controller.
 * @author wentao
 *
 */
public class Connect4Model extends Observable implements java.io.Serializable{
	/**
	 * add the serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	public static int BOARD_ROW = 6;
	public static int BOARD_COLUMN = 7;
	String[][] board;
	
	/**
	 * the constructor of the model class
	 * it generate the empty board, which initialize each place to "_".
	 */
	public Connect4Model() {
		board = new String[BOARD_ROW][BOARD_COLUMN];
		for (int i = 0; i<BOARD_ROW;i++) {
			for (int j = 0; j < BOARD_COLUMN; j++) {
				board[i][j]="_";
			}
		}
	}
	
	public void setVal(int row, int col, String s) {
		board[row][col]=s; 
	}
	
	/**
	 * placeX()
	 * it's human turn, human play the X on the board, and set the change to the view class, 
	 * which contains the row, column and color.
	 * @param row the row of the board
	 * @param column the column of the board
	 */
	public void placeX(int row, int column){
		board[row][column] = "X";
		Connect4MoveMessage message = new Connect4MoveMessage(row, column, Color.YELLOW);
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * placeO()
	 * it's computer turn, human play the O on the board, and set the change to the view class,
	 * which contains the row, column and color.
	 * @param row the row of the board
	 * @param column the column of the board
	 */
	public void placeO(int row, int column) {
		board[row][column] = "O";
		Connect4MoveMessage message = new Connect4MoveMessage(row, column, Color.RED);
		setChanged();
		notifyObservers(message);
	}
	/**
	 * getAtLocation();
	 * get the correspond marker in the board.
	 * @param row the row of the board
	 * @param column the column of the board
	 * @return the corresponding string
	 */
	public String getAtLocation(int row, int column) {
		return board[row][column];
	}
}