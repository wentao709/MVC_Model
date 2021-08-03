import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

/**
 * Connect4Controller: the controller of the game, it manipulates the model in response to actions from the view.
 * it has the human turn and computer turn methods to run the game and finish the game when either human win or computer
 * win or tie.
 * @author wentao
 *
 */
public class Connect4Controller {
	private Connect4Model model = null;
	
	
	/**
	 * the constructor of the controller class. 
	 * set the model variable.
	 * read in the save_game.dat file if it exists, otherwise create a new model.
	 * @param model the model passed in.
	 */
	public Connect4Controller() {
		try {
			FileInputStream fis = new FileInputStream("save_game.dat"); //read in the saved file
			ObjectInputStream ois = new ObjectInputStream(fis);
			this.model = (Connect4Model) ois.readObject(); // the model is the saved one
			ois.close();
		}
		catch (IOException e){
			System.out.println("There is no saved game");
		} catch (ClassNotFoundException e) {
			System.out.println("");
		}
			if (this.model == null) {
				this.model = new Connect4Model(); 
		}
	}
	
	/**
	 * newModel()
	 * reset the statue of the model
	 * need it when the user click the "new game" menu
	 */
	public void newModel() {
		for (int i =0; i<getRows();i++) {
			for (int j =0; j<getCols();j++) {
				this.model.setVal(i, j, "_");
			}
		}
	}
	
	/**
	 * addObserver()
	 * add the observer to the model.
	 * @param view the view class of the game
	 */
	public void addObserver(Connect4View view) {
		model.addObserver(view);
	}
	
	/**
	 * getRow()
	 * @return the row # of the board
	 */
	public int getRows() {
		return model.BOARD_ROW;
	}
	
	/**
	 * getCols()
	 * @return the column # of the board
	 */
	public int getCols() {
		return model.BOARD_COLUMN;
	}
	
	/**
	 * saveGame()
	 * save the game when user exits if the game is in the process
	 * @throws FileNotFoundException throw the exception when not file found
	 * @throws IOException throw this exception if can't read in the file
	 */
	public void saveGame() throws FileNotFoundException, IOException {
		try {
			FileOutputStream out = new FileOutputStream("save_game.dat");
			ObjectOutputStream oos = new ObjectOutputStream(out); // write out the saved model statue
			oos.writeObject(this.model);
			oos.close();
			out.close();
		}
		catch(IOException e) {
			System.out.println("unable to save");
		}
	}
	
	/**
	 * humanTurn()
	 * we call this function when it is human's turn to put the token, it takes the column as parameter and 
	 * replace the "_" with "X" in the corresponding location. 
	 * 
	 * @param column the column the user want to put
	 * @throws Connect4IllegalColumnException throws the Connect4IllegalColumnException when necessary
	 * @throws Connect4FullColumnException throws the Exception Connect4FullColumnException when necessary
	 */
	public void humanTurn(int column) throws Connect4IllegalColumnException, Connect4FullColumnException{
		if (column > 6 || column < 0) {
			throw new Connect4IllegalColumnException("invalid column, the value of column should be integer and between 0 and 6.");
		}
		int row = 5;
		while (!model.getAtLocation(row, column).equals("_")) {
			row -= 1;
			if (row < 0) {
				throw new Connect4FullColumnException("column is full");
			} // move up until the empty place.
		}
		model.placeX(row, column);
	}
	
	/**
	 * computerTurn(): we call this function when it is computer's turn to put the token
	 * it basically put the token at random column.
	 * @throws Connect4IllegalColumnException  throws the Connect4IllegalColumnException when necessary
	 * @throws Connect4FullColumnException throws the Exception Connect4FullColumnException when necessary
	 */
	public void computerTurn(){
		if(!(threeInRow() || threeInCol())) {
			int row = 5;
			Random rand = new Random();
			int column = rand.nextInt(model.BOARD_COLUMN);
			while(fullCol(column)) {
				column = rand.nextInt(model.BOARD_COLUMN);
			}
			while (!model.getAtLocation(row, column).equals("_")) {
				row--;
			}
			model.placeO(row, column);
		}
	}
	
	/**
	 * threeInRow()
	 * check if there is three same colors in a row, f it sees three in a row of the opposite color, it should block. 
	 * If it sees three in a row of its color, it should win
	 * @return true if the board can put a token after it find three same color token in a row, otherwise return false
	 */
	private boolean threeInRow() {
		for (int i = 0; i<model.BOARD_ROW ; i++) {
			for (int j = 0; j<model.BOARD_COLUMN-2 ;j++) {
				if ((model.board[i][j] == "X" && model.board[i][j+1] == "X" && model.board[i][j+2] == "X") || 
						(model.board[i][j] == "O" && model.board[i][j+1] == "O" && model.board[i][j+2] == "O")) {
					int column = -1;
					if (j == 0) { // edge case
						if (model.board[i][3].equals("_")) {
							if (!fullCol(3)) {
								if(getRow(3) == i) {
									column = 3;
								}
							}
						}
					}else if(j == 4) { // edge case
						if (model.board[i][j-1].equals("_")) {
							if(!fullCol(j-1)) {
								if(getRow(j-1) == i) {
									column = 3;
								}
							}
						}
					}else{
						if (model.board[i][j-1].equals("_") && model.board[i][j+3].equals("_")) {
							Random rand = new Random();
							int random = rand.nextInt(2);
							if (random == 0) {
								if (!fullCol(j-1)) { //check the left side first, check if the column is full first
									if(getRow(j-1) == i) { // check if the computer can put the token in the correct line
										column = j-1;
									}else if(!fullCol(j+3)) {
										if(getRow(j+3) == i) {
											column = j+3;
										}
									}
								}else if(!fullCol(j+3)) {//if the left side is full, check the right side
									if(getRow(j+3) == i) {
										column = j+3;
									}else if(!fullCol(j-1)) {
										if(getRow(j-1) == i) {
											column = j-1;
										}
									}
								}
							}
							if (random == 1) { // did the same thing when random = 0, except that it goes to the right side first
								if (!fullCol(j+3)) {
									if(getRow(j+3) == i) {
										column = j+3;
									}else if(!fullCol(j-1)) {
										if(getRow(j-1) == i) {
											column = j-1;
										}
									}
								}else if(!fullCol(j-1)) {
									if(getRow(j-1) == i) {
										column = j-1;
									}else if(!fullCol(j+3)) {
										if(getRow(j+3) == i) {
											column = j+3;
										}
									}
								}
							}
						}else if(model.board[i][j-1].equals("_") && !model.board[i][j+3].equals("_")) {  
							if (!fullCol(j-1)) {
								if (getRow(j-1) == i) {
									column = j-1;
								}
							}// the case that the right side is blocked already, then can only check the left side
						}else if(!model.board[i][j-1].equals("_") && model.board[i][j+3].equals("_")) {
							if (!fullCol(j+3)) {
								if(getRow(j+3) == i) {
									column = j+3;
								}
							}
						}// the case that the left side is blocked already, then can only check the right side
					}
					if (column != -1) { 
						int row = getRow(column);
						model.placeO(row, column);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * threeInCol()
	 * check if there is three same colors in a column, if it sees three in a column of the opposite color, it should block. 
	 * If it sees three in a column of its color, it should win
	 * @return true if the board can put a token after it find three same color token in a column, otherwise return false
	 */
	private boolean threeInCol() {
		for (int i = 0; i < model.BOARD_ROW-2; i++) { // i is the row
			for (int j = 0; j<model.BOARD_COLUMN; j++) { // j is the column
				if ((model.board[i][j]=="X" && model.board[i+1][j]=="X" && model.board[i+2][j]=="X") || 
						(model.board[i][j]=="O" && model.board[i+1][j]=="O" && model.board[i+2][j]=="O")) {
					
					int column = -1;
					if (i!=0) { // if the column is not full
						if (!model.board[i-1][j].equals("O")) {
							column = j;
						}
					}
					if (column != -1) {
						model.placeO(i-1, column);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * fullCol()
	 * check if the column is full
	 * @param column the column to be checked
	 * @return true if it's full, otherwise its false
	 */
	private boolean fullCol(int column) {
		int row = 5;
		while (!model.getAtLocation(row, column).equals("_")) {
			row--;
			if (row < 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * getRow()
	 * get the row number that the computer should put the token in
	 * @param column the column position where  the computer should put the token
	 * @return the row number
	 */
	private int getRow(int column) {
		int row = 5;
		while (!model.getAtLocation(row, column).equals("_")) {
			row--;
		}
		return row;
		
	}
	
	/**
	 * playerWin(): determined if the user wins and end the game
	 * check either horizontally, vertically, or diagonally.
	 * @return return true when the player wins, otherwise return false.
	 */
	public boolean playerWin() {
		for (int row = 3; row < 6;row++) { // vertical
			for (int col = 0; col<7 ;col++) {
				if (model.getAtLocation(row, col).equals("X") && model.getAtLocation(row-1, col).equals("X") && 
						model.getAtLocation(row-2, col).equals("X") && model.getAtLocation(row-3, col).equals("X")) {
					return true;
				}
			}
		}
		for (int row = 0; row < 6;row++) {// horizontal
			for (int col = 0; col<4; col++) {
				if (model.getAtLocation(row, col).equals("X") && model.getAtLocation(row, col+1).equals("X") && 
						model.getAtLocation(row, col+2).equals("X") && model.getAtLocation(row, col+3).equals("X")) {
					return true;
				}
			}
		}
		for (int row = 5; row > 2;row--) {
			for (int col = 0; col < 4 ;col++) {
				if (model.getAtLocation(row, col).equals("X") && model.getAtLocation(row-1, col+1).equals("X") 
						&& model.getAtLocation(row-2, col+2).equals("X") && model.getAtLocation(row-3, col+3).equals("X")) {
					return true;
				}
			}
		}
		for (int row = 0; row < 3;row++) {
			for (int col = 0; col < 4 ;col++) {
				if (model.getAtLocation(row, col).equals("X") && model.getAtLocation(row+1, col+1).equals("X")
						&& model.getAtLocation(row+2, col+2).equals("X") && model.getAtLocation(row+3, col+3).equals("X")) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** computerWin(): determind if the computer wins and end the game
	 * check either horizontally, vertically, or diagonally.
	 * @return return true when the player wins, otherwise return false.
	 */
	public boolean computerWin() {
		for (int row = 3; row < 6;row++) {
			for (int col = 0; col<7 ;col++) {
				if (model.getAtLocation(row, col).equals("O") && model.getAtLocation(row-1, col).equals("O") && 
						model.getAtLocation(row-2, col).equals("O") && model.getAtLocation(row-3, col).equals("O")) {
					return true;
				}
			}
		}
		for (int row = 0; row < 6;row++) {// horzontal
			for (int col = 0; col<4; col++) {
				if (model.getAtLocation(row, col).equals("O") && model.getAtLocation(row, col+1).equals("O") && 
						model.getAtLocation(row, col+2).equals("O") && model.getAtLocation(row, col+3).equals("O")) {
					return true;
				}
			}
		}
		
		for (int row = 5; row > 2;row--) { // diagonally.
			for (int col = 0; col < 4 ;col++) {
				if (model.getAtLocation(row, col).equals("O") && model.getAtLocation(row-1, col+1).equals("O")
						&& model.getAtLocation(row-2, col+2).equals("O") && model.getAtLocation(row-3, col+3).equals("O")) {
					return true;
				}
			}
		}
		
		for (int row = 0; row < 3;row++) { // diagonally.
			for (int col = 0; col < 4 ;col++) {
				if (model.getAtLocation(row, col).equals("O") && model.getAtLocation(row+1, col+1).equals("O")
						&& model.getAtLocation(row+2, col+2).equals("O") && model.getAtLocation(row+3, col+3).equals("O")) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * isGameOver() 
	 * determine if the game is over.
	 * @return true if the game is over, false otherwise.
	 */
	public boolean isGameOver() {
		if (playerWin() || computerWin()) {
			return true;
		}
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7;col++) {
				if (model.getAtLocation(row, col).equals("_")) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * getAt(): get access to the board to display the board as part of the view.
	 * @param row the row of the board
	 * @param col the column of the board 
	 * @return return the corresponding string.
	 */
	public String getAt(int row, int col) {
		return model.board[row][col];
	}
}