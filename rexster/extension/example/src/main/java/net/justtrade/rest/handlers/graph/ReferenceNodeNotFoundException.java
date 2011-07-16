package net.justtrade.rest.handlers.graph;

/**
* 
* An exception to throw when a graph database somehow gets confused and loses its reference node.
* 
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class ReferenceNodeNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8058564244426520149L;
	private static String sCause = "Failed to find : ";
	
	public ReferenceNodeNotFoundException() {
		super(sCause + "a reference node.");
	}

	public ReferenceNodeNotFoundException(String message) {
		super(sCause + message);
	}

	public ReferenceNodeNotFoundException(Throwable cause) {
		super(cause);
	}

	public ReferenceNodeNotFoundException(String message, Throwable cause) {
		super(sCause + message, cause);
	}

}
