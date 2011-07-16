package net.justtrade.rest.util;

/**
* 
* Thrown when analysis of Jena types is unable to determine the node type.
* 
* Jena has some very specific types of Nodes, that cause NullPointerExceptions if not correctly identified
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
