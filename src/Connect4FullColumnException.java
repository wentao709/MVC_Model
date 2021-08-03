/**
 * Connect4FullColumnException
 * exception class, used to handle the issue that when the user want to put the token in the column which is full.
 * @author wentao
 *
 */
public class Connect4FullColumnException extends Exception{
	/**
	 * simply call the method in base class.
	 * @param s input string 
	 */
	public Connect4FullColumnException(String s) {
		super(s);
	}
}
