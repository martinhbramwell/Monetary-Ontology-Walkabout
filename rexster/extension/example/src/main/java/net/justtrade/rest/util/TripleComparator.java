package net.justtrade.rest.util;

import java.util.Comparator;

import com.hp.hpl.jena.graph.Triple;

public class TripleComparator implements Comparator<Triple> {

	public int compare(Triple triple1, Triple triple2) {
		return ((Triple) triple1).toString().compareTo(((Triple) triple2).toString());
	}

}
