/**
 * Connect4IllegalColumnException
 * exception class, used to handle invalid input
 * @author wentao
 *
 */
public class Connect4IllegalColumnException extends Exception{
	/**
	 * simply call the method in base class.
	 * @param s input string
	 */
	public Connect4IllegalColumnException(String s) {
		super(s);
	}
}
