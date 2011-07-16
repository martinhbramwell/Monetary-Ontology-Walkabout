package net.justtrade.rest.util;

import java.util.Comparator;

import com.hp.hpl.jena.graph.Triple;

/**
* Convenience method for checking similarity of Triples
* 
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class TripleComparator implements Comparator<Triple> {

	/**
	 * Respects the Java Comparator standard
	 * 
	 * @param triple1
	 * @param triple2
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Triple triple1, Triple triple2) {
		return ((Triple) triple1).toString().compareTo(((Triple) triple2).toString());
	}

}
