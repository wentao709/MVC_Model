import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * Connect4View class
 * it extends the application class. it shows the image of the game, 
 * uses JavaFX to display the GUI described above and is an Observer
 * and create an mouseEvent handler to let user interact with the game.
 * @author wentao
 *
 */
public class Connect4View extends Application implements Observer{
	double xPos;
	double yPos;
	Connect4Controller controller = new Connect4Controller();
	TilePane tilePane = new TilePane();
	Circle[][] array = new Circle[controller.getRows()][controller.getCols()]; // create a array to store the circle
	
	/**
	 * start()
	 * create a bunch of circles and locate them use tilepane.
	 * the background is blue, yellow represents player's move, red represents computer's. 
	 * white is empty space.
	 * @param stage the stage of the game
	 */
	@Override
	public void start(Stage stage) {
		controller.addObserver(this);
		int radius = 20;
		
		// update the board view when each game starts, the board could be either saved or unsaved
		for (int k= 0; k < controller.getRows();k++) {
			for (int l = 0; l < controller.getCols();l++) {
				if (controller.getAt(k, l).equals("X")) {
					Circle circle = new Circle(radius,Color.YELLOW); 
					tilePane.getChildren().add(circle);
					array[k][l] = circle;
				}
				else if (controller.getAt(k, l).equals("O")) {
					Circle circle = new Circle(radius,Color.RED); 
					tilePane.getChildren().add(circle);
					array[k][l] = circle;
				}else {
					Circle circle = new Circle(radius,Color.WHITE); 
					tilePane.getChildren().add(circle);
					array[k][l] = circle;
				}
			}
		}

		tilePane.setPrefColumns(controller.getCols());
		tilePane.setPrefRows(controller.getRows());
		tilePane.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		tilePane.setHgap(8);
		tilePane.setVgap(8);
		tilePane.setPadding(new Insets(8, 8, 8, 8));
		
		// add the menu 
		MenuBar menu = new MenuBar(); 
		Menu menu1 = new Menu("File");
		MenuItem game = new MenuItem("New game");
		
		menu1.getItems().add(game);
		menu.getMenus().add(menu1); 
		
        StackPane root = new StackPane();
		VBox pane = new VBox();
		pane.getChildren().addAll(menu,tilePane);
        root.getChildren().add(pane);

        // when click on "new game" menu, delete the save file and reset all the circle to be white.
        // also reset the statue of the board
        game.setOnAction(new EventHandler<ActionEvent>() {
        	@Override public void handle(ActionEvent e) {
        		EventHandler<MouseEvent> handler = new MyMouseHandler();
        		tilePane.setOnMouseClicked(handler);	
        		//delete old file first
        		File file = new File("save_game.dat"); 
        		file.delete();
        		controller.newModel(); 
        		for (int k= 0; k < controller.getRows();k++) {
        			for (int l = 0; l < controller.getCols();l++) {
        					array[k][l].setFill(Color.WHITE);
        			}
        		}
        	}      		
        });
        
		EventHandler<MouseEvent> handler = new MyMouseHandler();
		tilePane.setOnMouseClicked(handler);
		
		// when quit, save the game if the game is not finished
		stage.setOnCloseRequest((event)->{
			try {
				if(!controller.computerWin()&&!controller.playerWin()&& !controller.isGameOver()) {
					controller.saveGame();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		});
		
        Scene scene = new Scene(root);
        
        
        stage.setResizable(false);
	    stage.setTitle("Connect4"); 
	    stage.setScene(scene);
	    stage.show();
	}
	
	/**
	 * MyMouseHandler class
	 * handler the mouse clicked event. transfer the click position to the associated column.
	 * and do the humanturn and computerturn method in the controller.
	 * @author wentao
	 *
	 */
	private class MyMouseHandler implements EventHandler<MouseEvent>{
	    public void handle(MouseEvent e) {
	    	xPos = e.getX();
	    	yPos = e.getY();
	    	int column = 0;
	    	for (int i =0; i < controller.getCols();i++) {
	    		if (i ==0) {
		    		if (xPos<= 52) {
		    			column = 0;
		    		}
		    	}else if( i == controller.getCols()-1) {
		    		if (xPos > 52 + 48*(i-1)) {
		    			column = controller.getCols()-1;
		    		}
		    	}else {
		    		if( xPos > (52 + 48*(i-1)) && xPos <= 52+48*i) {
		    			column = i;
		    		}
		    	}
	    	} // transfer the click position to the associated column
	    	try {
				controller.humanTurn(column);
				if (controller.playerWin()) {
					new Alert(Alert.AlertType.INFORMATION, "you win!").showAndWait();
					tilePane.setOnMouseClicked(null); // disable any further move
	    			File file = new File("save_game.dat");
	        		file.delete();
				}
				if(!controller.playerWin()){
					controller.computerTurn();
					if (controller.computerWin()) {
						new Alert(Alert.AlertType.INFORMATION, "you lose!").showAndWait();
						tilePane.setOnMouseClicked(null); // disable any further move
			    		File file = new File("save_game.dat");
			       		file.delete();
			       	}
				}
				if(!controller.computerWin() && !controller.playerWin() && controller.isGameOver()) {
					new Alert(Alert.AlertType.INFORMATION, "tie game!").showAndWait();
					tilePane.setOnMouseClicked(null);
					File file = new File("save_game.dat");
		       		file.delete();
				} // tie game
			} catch (Connect4IllegalColumnException | Connect4FullColumnException e1) {
				new Alert(Alert.AlertType.ERROR, "column is full!").showAndWait();
			}
	    }
	  }

	/**
	 * update()
	 * view class is the observer so I need the update method to check some update 
	 * made in the model and show them to the user.
	 * @param observable the observable class
	 * @param message the message from the model, which has row, column and color.
	 */
	@Override
	public void update(Observable observable, Object message) {
		int row = ((Connect4MoveMessage) message).getRow();
		int column = ((Connect4MoveMessage) message).getColumn();
	   	array[row][column].setFill(((Connect4MoveMessage) message).getColor());
	}
}

