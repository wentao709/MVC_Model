import javafx.scene.paint.Color;

/**
 * Connect4MoveMessage class
 * store the message in the model and send it to the view class
 * @author wentao
 *
 */
public class Connect4MoveMessage {
	private int row;
	private int column;
	private Color color;

	/**
	 * 
	 * @param row the row# of the board
	 * @param column the column# of the board
	 * @param color the color the view need to show of the board
	 */
	public Connect4MoveMessage(int row, int column, Color color) {
		this.row = row;
		this.column = column;
		this.color = color;
	}
	
	/**
	 * getRow()
	 * provide access to the row for the view
	 * @return row the row of the board
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * getColumn()
	 * provide access to the column for the view
	 * @return column the column of the board
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * getColor()
	 * provide access to the color for the view
	 * @return color of the associated circle
	 */
	public Color getColor() {
		return color;
	}
	
}
