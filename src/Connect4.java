import javafx.application.Application;

/*
 * Wentao Zhou
 * section: A
 * section leader: Taite Nazifi
 * */ 

/**
 * the main class of the connect4 game, which has the main method
 * it launches the view.
 * @author wentao zhou
 *
 */
public class Connect4 {
	/**
	 * main() method
	 * A main class, that launches your view using
	 * @param args the input from stdin
	 */
	public static void main(String []args) {
		Application.launch(Connect4View.class, args);
	}
}