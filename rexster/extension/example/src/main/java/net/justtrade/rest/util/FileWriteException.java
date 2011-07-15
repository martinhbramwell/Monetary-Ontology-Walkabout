package net.justtrade.rest.util;

/**
* 
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class FileWriteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8058564244426520149L;

	public FileWriteException() {
		super("Problem writing to file.");
	}

	public FileWriteException(String message) {
		super(message);
	}

	public FileWriteException(Throwable cause) {
		super(cause);
	}

	public FileWriteException(String message, Throwable cause) {
		super("Cannot use that type here.", cause);
	}

}
