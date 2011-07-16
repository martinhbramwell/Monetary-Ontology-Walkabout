package net.justtrade.rest.handlers.graph;

/**
* Exception to throw when a graph database is somehow confused and has multiple unique reference nodes.
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class MultipleReferenceNodesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8058564244426520149L;
	private static String sCause = "There may only be one";
	
	public MultipleReferenceNodesException() {
		super(sCause);
	}

	public MultipleReferenceNodesException(String message) {
		super(sCause + message);
	}

	public MultipleReferenceNodesException(Throwable cause) {
		super(cause);
	}

	public MultipleReferenceNodesException(String message, Throwable cause) {
		super(sCause + " of these : " + message, cause);
	}

}
