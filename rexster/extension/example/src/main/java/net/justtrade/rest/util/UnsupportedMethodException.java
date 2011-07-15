package net.justtrade.rest.util;

/**
* 
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class UnsupportedMethodException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8058564244426520149L;

	public UnsupportedMethodException() {
		super("Class publishes no such public method.");
	}

	public UnsupportedMethodException(String message) {
		super(message);
	}

	public UnsupportedMethodException(Throwable cause) {
		super(cause);
	}

	public UnsupportedMethodException(String message, Throwable cause) {
		super("Cannot use that method here.", cause);
	}

}
