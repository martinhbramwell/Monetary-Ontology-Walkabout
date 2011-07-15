package net.justtrade.rest.util;

/**
* 
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class UnsupportedTypeException extends Exception {

	private static final long serialVersionUID = 8058564244426520149L;
	private static final String intro = 
		  "\n**************************************"
		+ "\n**************************************"
		+ "\n***                                ***"
		+ "\n***    Unsupported Jena type,      ***"
		+ "\n***      as indicated below.       ***"
		+ "\n***                                ***"
		+ "\n**************************************"
		+ "\n**************************************"
		;

	public UnsupportedTypeException() {
		super(intro);
	}

	public UnsupportedTypeException(String message) {
		super(intro + message);
	}

	public UnsupportedTypeException(Throwable cause) {
		super(cause);
	}

	public UnsupportedTypeException(String message, Throwable cause) {
		super(intro + message, cause);
	}

}
