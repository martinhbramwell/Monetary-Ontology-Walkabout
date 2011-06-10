package net.justtrade.rest.util;

public class UnsupportedTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8058564244426520149L;

	public UnsupportedTypeException() {
		// TODO Auto-generated constructor stub
	}

	public UnsupportedTypeException(String message) {
		super(message);
	}

	public UnsupportedTypeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UnsupportedTypeException(String message, Throwable cause) {
		super("Cannot use that type here.", cause);
	}

}
