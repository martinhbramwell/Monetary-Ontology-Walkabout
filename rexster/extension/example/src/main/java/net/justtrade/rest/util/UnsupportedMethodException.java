package net.justtrade.rest.util;

public class UnsupportedMethodException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8058564244426520149L;

	public UnsupportedMethodException() {
		// TODO Auto-generated constructor stub
	}

	public UnsupportedMethodException(String message) {
		super(message);
	}

	public UnsupportedMethodException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UnsupportedMethodException(String message, Throwable cause) {
		super("Cannot use that method here.", cause);
		// TODO Auto-generated constructor stub
	}

}
