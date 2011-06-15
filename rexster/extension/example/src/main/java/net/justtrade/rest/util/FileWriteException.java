package net.justtrade.rest.util;

public class FileWriteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8058564244426520149L;

	public FileWriteException() {
		// TODO Auto-generated constructor stub
	}

	public FileWriteException(String message) {
		super(message);
	}

	public FileWriteException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public FileWriteException(String message, Throwable cause) {
		super("Cannot use that type here.", cause);
	}

}
