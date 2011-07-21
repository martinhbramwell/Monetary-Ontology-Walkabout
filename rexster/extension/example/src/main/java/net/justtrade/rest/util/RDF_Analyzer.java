package net.justtrade.rest.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

/**
 * RDF_Analyzer is a debugging tool. It helps figure out Jena's 
 * terminology of Nodes that may be specialized as one or more of :
 * <ul>
 *   <li>Blank</li>
 *   <li>Blank</li>
 *   <li>Concrete</li>
 *   <li>Literal</li>
 *   <li>URI</li>
 *   <li>Variable</li>
 * </ul>
 *  
 *  It builds a map of the possible combinations actually used, and 
 *  for each combination, collects an array of the Triples that 
 *  have those combinations.
 *  
 *  @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
 */
public class RDF_Analyzer {

	public static final String CLASS_NAME = "\nRDF_Analyzer.";	

	private static HashMap<Long, ArrayList<Triple>> categories = new HashMap<Long, ArrayList<Triple>>();
	private static HashMap<Long, StringBuffer> flagsUsed = new HashMap<Long, StringBuffer>();
	
	private static final Logger logger = LoggerFactory.getLogger(RDF_Analyzer.class);
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	/**
	 * Collect data about the RDF file to be processed. Builds
	 * a profile of the triple and adds the triple to the analysis
	 * map under that profile.
	 * 
	 * @param triple -- This a JENA triple to be analyzed.
	 */
	public static void collectModelAnalysisData(Triple triple)
	{
		final String sMETHOD = CLASS_NAME + "collectModelAnalysisData(Triple) --> ";

		Node subject = triple.getSubject();
		Node predicate = triple.getPredicate();
		Node object = triple.getObject();

		StringBuffer attributes;

		String sep = "";

		long flags = 0;
		int flag = 1;
		Long attrKey;

		attributes = new StringBuffer();
		sep = "";

		flag = 1;
		if ( object.isBlank()) {attributes.append(sep).append("Blank"); flags |= flag; sep = ",";}
		flag *=2;
		if ( object.isConcrete()) {attributes.append(sep).append("Concrete"); flags |= flag; sep = ",";}
		flag *=2;
		if ( object.isLiteral()) {attributes.append(sep).append("Literal"); flags |= flag; sep = ",";}
		flag *=2;
		if ( object.isURI()) {attributes.append(sep).append("URI"); flags |= flag; sep = ",";}
		flag *=2;
		if ( object.isVariable()) {attributes.append(sep).append("Variable"); flags |= flag; sep = ",";}

		attrKey = new Long(Util.getByteX(flags, 0));
		if (flagsUsed.get(attrKey) == null)
		{
			logger.debug(sMETHOD + "Saving -- Key : " + Long.toBinaryString(attrKey) + " Attributes : " + attributes);
			flagsUsed.put(attrKey, attributes);
		}

		attributes = new StringBuffer();
		sep = "";

		flag = 256;	    
		if ( predicate.isBlank()) {attributes.append(sep).append("Blank"); flags |= flag; sep = ",";}	    
		flag *=2;
		if ( predicate.isConcrete()) {attributes.append(sep).append("Concrete"); flags |= flag; sep = ",";}
		flag *=2;
		if ( predicate.isLiteral()) {attributes.append(sep).append("Literal"); flags |= flag; sep = ",";}
		flag *=2;
		if ( predicate.isURI()) {attributes.append(sep).append("URI"); flags |= flag; sep = ",";}
		flag *=2;
		if ( predicate.isVariable()) {attributes.append(sep).append("Variable"); flags |= flag; sep = ",";}

		attrKey = new Long(Util.getByteX(flags, 0));
		if (flagsUsed.get(attrKey) == null)
		{
			logger.debug(sMETHOD + "Saving -- Key : " + Long.toBinaryString(attrKey) + " Attributes : " + attributes);
			flagsUsed.put(attrKey, attributes);
		}

		attributes = new StringBuffer();
		sep = "";

		flag = 65536;
		if ( subject.isBlank()) {attributes.append(sep).append("Blank"); flags |= flag; sep = ",";}
		flag *=2;

		if ( subject.isConcrete()) {attributes.append(sep).append("Concrete"); flags |= flag; sep = ",";}
		flag *=2;
		if ( subject.isLiteral()) {attributes.append(sep).append("Literal"); flags |= flag; sep = ",";}
		flag *=2;
		if ( subject.isURI()) {attributes.append(sep).append("URI"); flags |= flag; sep = ",";}
		flag *=2;
		if ( subject.isVariable()) {attributes.append(sep).append("Variable"); flags |= flag; sep = ",";}

		attrKey = new Long(Util.getByteX(flags, 0));
		if (flagsUsed.get(attrKey) == null)
		{
			logger.debug(sMETHOD + "Saving -- Key : " + Long.toBinaryString(attrKey) + " Attributes : " + attributes);
			flagsUsed.put(attrKey, attributes);
		}


		//			logger.debug(sMETHOD + " Flags are : " + Long.toBinaryString(flags));
		Long key = new Long(flags);

		ArrayList<Triple> triples = categories.get(key);
		if (triples == null) {
			triples = new ArrayList<Triple>();
		}
		triples.add(triple);
		categories.put(key, triples);


	}

	/**
	 * Converts the bit map profile into a text profile and
	 * then prints out all the triples that match that profile
	 * 
	 */
	public static void analyzeModelData() {
		ArrayList<Triple> triples;
		for(Long key : categories.keySet() )
		{
			logger.debug("With key '" + Long.toBinaryString(key) + "' we got these triples :");
			logger.debug("Subject   : '" + flagsUsed.get(Util.getByteX(key.longValue(), 2)) + "'.");
			logger.debug("Predicate : '" + flagsUsed.get(Util.getByteX(key.longValue(), 1)) + "'.");
			logger.debug("Object    : '" + flagsUsed.get(Util.getByteX(key.longValue(), 0)) + "'.");

			triples = categories.get(key);
			java.util.Collections.sort(triples, new TripleComparator());
			for(Triple triple : triples )
			{
				logger.debug(" - Triple '" + triple + "'.");
			}
		}
	}
}


/*

All three elements are concrete.

Subjects are '01010' or '00011'  

Predicates '01010'

Objects are '00011', '01010' or '00110'


        1 -- 1 -- 1 -- 1 -- 1
        ======================
        |    |    |    |    + - Blank
        |    |    |    + - Concrete
        |    |    + - Literal
        |    + - URI
        + - Variable


With key '110000101000000110' we got these triples :
Subject   : 'Blank,Concrete'.
Predicate : 'Concrete,URI'.
Object    : 'Concrete,Literal'.
 - Triple '-2839338:1307a6170ab:-7fab @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
 - Triple '-2839338:1307a6170ab:-7fad @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
 - Triple '-2839338:1307a6170ab:-7fb9 @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
 - Triple '-2839338:1307a6170ab:-7fbc @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
 - Triple '-2839338:1307a6170ab:-7fbe @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
 - Triple '-2839338:1307a6170ab:-7fda @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
 - Triple '-2839338:1307a6170ab:-7fdc @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
 - Triple '-2839338:1307a6170ab:-7fe4 @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
 - Triple '-2839338:1307a6170ab:-7fe6 @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
 - Triple '-2839338:1307a6170ab:-7ff6 @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
 - Triple '-2839338:1307a6170ab:-7ffa @owl:minCardinality "1"^^http://www.w3.org/2001/XMLSchema#int'.
With key '110000101000000011' we got these triples :
Subject   : 'Blank,Concrete'.
Predicate : 'Concrete,URI'.
Object    : 'Blank,Concrete'.
 - Triple '-2839338:1307a6170ab:-7fa6 @rdf:rest -2839338:1307a6170ab:-7fa5'.
 - Triple '-2839338:1307a6170ab:-7fa7 @owl:intersectionOf -2839338:1307a6170ab:-7fa6'.
 - Triple '-2839338:1307a6170ab:-7faa @rdf:first -2839338:1307a6170ab:-7fab'.
 - Triple '-2839338:1307a6170ab:-7fac @rdf:first -2839338:1307a6170ab:-7fad'.
 - Triple '-2839338:1307a6170ab:-7fac @rdf:rest -2839338:1307a6170ab:-7faa'.
 - Triple '-2839338:1307a6170ab:-7fae @rdf:first -2839338:1307a6170ab:-7faf'.
 - Triple '-2839338:1307a6170ab:-7fae @rdf:rest -2839338:1307a6170ab:-7fa9'.
 - Triple '-2839338:1307a6170ab:-7faf @owl:unionOf -2839338:1307a6170ab:-7fac'.
 - Triple '-2839338:1307a6170ab:-7fb0 @owl:intersectionOf -2839338:1307a6170ab:-7fae'.
 - Triple '-2839338:1307a6170ab:-7fb2 @rdf:rest -2839338:1307a6170ab:-7fb1'.
 - Triple '-2839338:1307a6170ab:-7fb3 @owl:intersectionOf -2839338:1307a6170ab:-7fb2'.
 - Triple '-2839338:1307a6170ab:-7fb5 @rdf:rest -2839338:1307a6170ab:-7fb4'.
 - Triple '-2839338:1307a6170ab:-7fb6 @owl:intersectionOf -2839338:1307a6170ab:-7fb5'.
 - Triple '-2839338:1307a6170ab:-7fb8 @rdf:first -2839338:1307a6170ab:-7fb9'.
 - Triple '-2839338:1307a6170ab:-7fb8 @rdf:rest -2839338:1307a6170ab:-7fb7'.
 - Triple '-2839338:1307a6170ab:-7fba @owl:intersectionOf -2839338:1307a6170ab:-7fb8'.
 - Triple '-2839338:1307a6170ab:-7fbb @rdf:first -2839338:1307a6170ab:-7fbc'.
 - Triple '-2839338:1307a6170ab:-7fbd @rdf:first -2839338:1307a6170ab:-7fbe'.
 - Triple '-2839338:1307a6170ab:-7fbd @rdf:rest -2839338:1307a6170ab:-7fbb'.
 - Triple '-2839338:1307a6170ab:-7fbf @rdf:first -2839338:1307a6170ab:-7fc0'.
 - Triple '-2839338:1307a6170ab:-7fc0 @owl:unionOf -2839338:1307a6170ab:-7fbd'.
 - Triple '-2839338:1307a6170ab:-7fc1 @rdf:rest -2839338:1307a6170ab:-7fbf'.
 - Triple '-2839338:1307a6170ab:-7fc2 @owl:intersectionOf -2839338:1307a6170ab:-7fc1'.
 - Triple '-2839338:1307a6170ab:-7fc4 @rdf:first -2839338:1307a6170ab:-7fc5'.
 - Triple '-2839338:1307a6170ab:-7fc4 @rdf:rest -2839338:1307a6170ab:-7fc3'.
 - Triple '-2839338:1307a6170ab:-7fc6 @owl:intersectionOf -2839338:1307a6170ab:-7fc4'.
 - Triple '-2839338:1307a6170ab:-7fc8 @rdf:rest -2839338:1307a6170ab:-7fc7'.
 - Triple '-2839338:1307a6170ab:-7fc9 @owl:unionOf -2839338:1307a6170ab:-7fc8'.
 - Triple '-2839338:1307a6170ab:-7fcb @rdf:rest -2839338:1307a6170ab:-7fca'.
 - Triple '-2839338:1307a6170ab:-7fcc @owl:oneOf -2839338:1307a6170ab:-7fcb'.
 - Triple '-2839338:1307a6170ab:-7fce @rdf:rest -2839338:1307a6170ab:-7fcd'.
 - Triple '-2839338:1307a6170ab:-7fcf @owl:intersectionOf -2839338:1307a6170ab:-7fce'.
 - Triple '-2839338:1307a6170ab:-7fd1 @rdf:rest -2839338:1307a6170ab:-7fd0'.
 - Triple '-2839338:1307a6170ab:-7fd2 @rdf:rest -2839338:1307a6170ab:-7fd1'.
 - Triple '-2839338:1307a6170ab:-7fd3 @rdf:rest -2839338:1307a6170ab:-7fd2'.
 - Triple '-2839338:1307a6170ab:-7fd4 @rdf:rest -2839338:1307a6170ab:-7fd3'.
 - Triple '-2839338:1307a6170ab:-7fd5 @rdf:rest -2839338:1307a6170ab:-7fd4'.
 - Triple '-2839338:1307a6170ab:-7fd6 @rdf:rest -2839338:1307a6170ab:-7fd5'.
 - Triple '-2839338:1307a6170ab:-7fd7 @owl:unionOf -2839338:1307a6170ab:-7fd6'.
 - Triple '-2839338:1307a6170ab:-7fd9 @rdf:first -2839338:1307a6170ab:-7fda'.
 - Triple '-2839338:1307a6170ab:-7fdb @rdf:first -2839338:1307a6170ab:-7fdc'.
 - Triple '-2839338:1307a6170ab:-7fdb @rdf:rest -2839338:1307a6170ab:-7fd9'.
 - Triple '-2839338:1307a6170ab:-7fdd @rdf:first -2839338:1307a6170ab:-7fde'.
 - Triple '-2839338:1307a6170ab:-7fde @owl:unionOf -2839338:1307a6170ab:-7fdb'.
 - Triple '-2839338:1307a6170ab:-7fdf @rdf:rest -2839338:1307a6170ab:-7fdd'.
 - Triple '-2839338:1307a6170ab:-7fe0 @owl:intersectionOf -2839338:1307a6170ab:-7fdf'.
 - Triple '-2839338:1307a6170ab:-7fe3 @rdf:first -2839338:1307a6170ab:-7fe4'.
 - Triple '-2839338:1307a6170ab:-7fe5 @rdf:first -2839338:1307a6170ab:-7fe6'.
 - Triple '-2839338:1307a6170ab:-7fe5 @rdf:rest -2839338:1307a6170ab:-7fe3'.
 - Triple '-2839338:1307a6170ab:-7fe7 @rdf:first -2839338:1307a6170ab:-7fe8'.
 - Triple '-2839338:1307a6170ab:-7fe7 @rdf:rest -2839338:1307a6170ab:-7fe2'.
 - Triple '-2839338:1307a6170ab:-7fe8 @owl:unionOf -2839338:1307a6170ab:-7fe5'.
 - Triple '-2839338:1307a6170ab:-7fe9 @owl:intersectionOf -2839338:1307a6170ab:-7fe7'.
 - Triple '-2839338:1307a6170ab:-7feb @rdf:first -2839338:1307a6170ab:-7fec'.
 - Triple '-2839338:1307a6170ab:-7feb @rdf:rest -2839338:1307a6170ab:-7fea'.
 - Triple '-2839338:1307a6170ab:-7fed @owl:intersectionOf -2839338:1307a6170ab:-7feb'.
 - Triple '-2839338:1307a6170ab:-7fef @rdf:rest -2839338:1307a6170ab:-7fee'.
 - Triple '-2839338:1307a6170ab:-7ff0 @owl:intersectionOf -2839338:1307a6170ab:-7fef'.
 - Triple '-2839338:1307a6170ab:-7ff2 @rdf:rest -2839338:1307a6170ab:-7ff1'.
 - Triple '-2839338:1307a6170ab:-7ff3 @owl:intersectionOf -2839338:1307a6170ab:-7ff2'.
 - Triple '-2839338:1307a6170ab:-7ff5 @rdf:first -2839338:1307a6170ab:-7ff6'.
 - Triple '-2839338:1307a6170ab:-7ff5 @rdf:rest -2839338:1307a6170ab:-7ff4'.
 - Triple '-2839338:1307a6170ab:-7ff7 @owl:intersectionOf -2839338:1307a6170ab:-7ff5'.
 - Triple '-2839338:1307a6170ab:-7ff9 @rdf:first -2839338:1307a6170ab:-7ffa'.
 - Triple '-2839338:1307a6170ab:-7ff9 @rdf:rest -2839338:1307a6170ab:-7ff8'.
 - Triple '-2839338:1307a6170ab:-7ffb @owl:intersectionOf -2839338:1307a6170ab:-7ff9'.
 - Triple '-2839338:1307a6170ab:-7ffd @rdf:rest -2839338:1307a6170ab:-7ffc'.
 - Triple '-2839338:1307a6170ab:-7ffe @owl:intersectionOf -2839338:1307a6170ab:-7ffd'.
With key '10100000101000001010' we got these triples :
Subject   : 'Concrete,URI'.
Predicate : 'Concrete,URI'.
Object    : 'Concrete,URI'.
 - Triple 'http://a.com/ontology @owl:imports http://swrl.stanford.edu/ontologies/3.3/swrla.owl'.
 - Triple 'http://a.com/ontology @owl:imports http://swrl.stanford.edu/ontologies/built-ins/3.3/abox.owl'.
 - Triple 'http://a.com/ontology @owl:imports http://swrl.stanford.edu/ontologies/built-ins/3.3/query.owl'.
 - Triple 'http://a.com/ontology @owl:imports http://swrl.stanford.edu/ontologies/built-ins/3.3/swrlx.owl'.
 - Triple 'http://a.com/ontology @owl:imports http://swrl.stanford.edu/ontologies/built-ins/3.3/tbox.owl'.
 - Triple 'http://a.com/ontology @owl:imports http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl'.
 - Triple 'http://a.com/ontology @owl:imports http://www.w3.org/2003/11/swrl'.
 - Triple 'http://a.com/ontology @owl:imports http://www.w3.org/2003/11/swrlb'.
 - Triple 'http://a.com/ontology @rdf:type owl:Ontology'.
 - Triple 'http://a.com/ontology#Aunt @owl:disjointWith http://a.com/ontology#Uncle'.
 - Triple 'http://a.com/ontology#Aunt @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Aunt @rdfs:subClassOf http://a.com/ontology#Relative'.
 - Triple 'http://a.com/ontology#Brother @owl:disjointWith http://a.com/ontology#Sister'.
 - Triple 'http://a.com/ontology#Brother @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Child @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Daugther @owl:disjointWith http://a.com/ontology#Son'.
 - Triple 'http://a.com/ontology#Daugther @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Father @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Female @rdf:type http://a.com/ontology#Gender'.
 - Triple 'http://a.com/ontology#Gender @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Male @rdf:type http://a.com/ontology#Gender'.
 - Triple 'http://a.com/ontology#Man @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Mother @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Nephew @owl:disjointWith http://a.com/ontology#Niece'.
 - Triple 'http://a.com/ontology#Nephew @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Nephew @rdfs:subClassOf http://a.com/ontology#Relative'.
 - Triple 'http://a.com/ontology#Niece @owl:disjointWith http://a.com/ontology#Nephew'.
 - Triple 'http://a.com/ontology#Niece @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Niece @rdfs:subClassOf http://a.com/ontology#Relative'.
 - Triple 'http://a.com/ontology#Parent @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Person @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Relative @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Relative @rdfs:subClassOf http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#Sibling @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Sister @owl:disjointWith http://a.com/ontology#Brother'.
 - Triple 'http://a.com/ontology#Sister @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Son @owl:disjointWith http://a.com/ontology#Daugther'.
 - Triple 'http://a.com/ontology#Son @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Uncle @owl:disjointWith http://a.com/ontology#Aunt'.
 - Triple 'http://a.com/ontology#Uncle @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#Uncle @rdfs:subClassOf http://a.com/ontology#Relative'.
 - Triple 'http://a.com/ontology#Woman @rdf:type owl:Class'.
 - Triple 'http://a.com/ontology#hasAunt @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasAunt @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasAunt @rdfs:range http://a.com/ontology#Woman'.
 - Triple 'http://a.com/ontology#hasBrother @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasBrother @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasBrother @rdfs:range http://a.com/ontology#Man'.
 - Triple 'http://a.com/ontology#hasBrother @rdfs:subPropertyOf http://a.com/ontology#hasSibling'.
 - Triple 'http://a.com/ontology#hasChild @owl:inverseOf http://a.com/ontology#hasParent'.
 - Triple 'http://a.com/ontology#hasChild @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasChild @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasChild @rdfs:range http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasConsort @rdf:type owl:FunctionalProperty'.
 - Triple 'http://a.com/ontology#hasConsort @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasConsort @rdf:type owl:SymmetricProperty'.
 - Triple 'http://a.com/ontology#hasConsort @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasConsort @rdfs:range http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasDaughter @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasDaughter @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasDaughter @rdfs:range http://a.com/ontology#Woman'.
 - Triple 'http://a.com/ontology#hasDaughter @rdfs:subPropertyOf http://a.com/ontology#hasChild'.
 - Triple 'http://a.com/ontology#hasFather @rdf:type owl:FunctionalProperty'.
 - Triple 'http://a.com/ontology#hasFather @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasFather @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasFather @rdfs:range http://a.com/ontology#Man'.
 - Triple 'http://a.com/ontology#hasFather @rdfs:subPropertyOf http://a.com/ontology#hasParent'.
 - Triple 'http://a.com/ontology#hasMother @rdf:type owl:FunctionalProperty'.
 - Triple 'http://a.com/ontology#hasMother @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasMother @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasMother @rdfs:range http://a.com/ontology#Woman'.
 - Triple 'http://a.com/ontology#hasMother @rdfs:subPropertyOf http://a.com/ontology#hasParent'.
 - Triple 'http://a.com/ontology#hasNephew @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasNephew @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasNephew @rdfs:range http://a.com/ontology#Man'.
 - Triple 'http://a.com/ontology#hasNiece @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasNiece @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasNiece @rdfs:range http://a.com/ontology#Woman'.
 - Triple 'http://a.com/ontology#hasParent @owl:inverseOf http://a.com/ontology#hasChild'.
 - Triple 'http://a.com/ontology#hasParent @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasParent @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasParent @rdfs:range http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasSex @rdf:type owl:FunctionalProperty'.
 - Triple 'http://a.com/ontology#hasSex @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasSex @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasSex @rdfs:range http://a.com/ontology#Gender'.
 - Triple 'http://a.com/ontology#hasSibling @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasSibling @rdf:type owl:SymmetricProperty'.
 - Triple 'http://a.com/ontology#hasSibling @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasSibling @rdfs:range http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasSister @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasSister @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasSister @rdfs:range http://a.com/ontology#Woman'.
 - Triple 'http://a.com/ontology#hasSister @rdfs:subPropertyOf http://a.com/ontology#hasSibling'.
 - Triple 'http://a.com/ontology#hasSon @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasSon @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasSon @rdfs:range http://a.com/ontology#Man'.
 - Triple 'http://a.com/ontology#hasSon @rdfs:subPropertyOf http://a.com/ontology#hasChild'.
 - Triple 'http://a.com/ontology#hasUncle @rdf:type owl:ObjectProperty'.
 - Triple 'http://a.com/ontology#hasUncle @rdfs:domain http://a.com/ontology#Person'.
 - Triple 'http://a.com/ontology#hasUncle @rdfs:range http://a.com/ontology#Man'.
 - Triple 'http://grandpa.mowa.justtrade.net @rdf:type owl:Ontology'.
 - Triple 'http://grandpa.mowa.justtrade.net#NotRecorded @rdf:type owl:Class'.
 - Triple 'http://grandpa.mowa.justtrade.net#NotRecorded @rdfs:subClassOf http://grandpa.mowa.justtrade.net#Person'.
 - Triple 'http://grandpa.mowa.justtrade.net#Person @rdf:type owl:Class'.
 - Triple 'http://grandpa.mowa.justtrade.net#StepMother @rdf:type owl:Class'.
 - Triple 'http://grandpa.mowa.justtrade.net#StepParent @rdf:type owl:Class'.
 - Triple 'http://grandpa.mowa.justtrade.net#StepParent @rdfs:subClassOf http://grandpa.mowa.justtrade.net#Person'.
 - Triple 'http://grandpa.mowa.justtrade.net#Woman @rdf:type owl:Class'.
 - Triple 'http://www.w3.org/2003/11/swrl#argument2 @rdf:type owl:ObjectProperty'.
With key '10100000101000000110' we got these triples :
Subject   : 'Concrete,URI'.
Predicate : 'Concrete,URI'.
Object    : 'Concrete,Literal'.
 - Triple 'http://test#PersonA @http://test#litProperty "Bob"'.
 - Triple 'http://test#PersonB @http://test#litProperty "Tom"'.
With key '10100000101000000011' we got these triples :
Subject   : 'Concrete,URI'.
Predicate : 'Concrete,URI'.
Object    : 'Blank,Concrete'.
 - Triple 'http://a.com/ontology#Aunt @owl:equivalentClass -2839338:1307a6170ab:-7fc2'.
 - Triple 'http://a.com/ontology#Brother @owl:equivalentClass -2839338:1307a6170ab:-7fb6'.
 - Triple 'http://a.com/ontology#Child @owl:equivalentClass -2839338:1307a6170ab:-7fba'.
 - Triple 'http://a.com/ontology#Daugther @owl:equivalentClass -2839338:1307a6170ab:-7ffe'.
 - Triple 'http://a.com/ontology#Father @owl:equivalentClass -2839338:1307a6170ab:-7fb3'.
 - Triple 'http://a.com/ontology#Gender @owl:equivalentClass -2839338:1307a6170ab:-7fcc'.
 - Triple 'http://a.com/ontology#Man @owl:equivalentClass -2839338:1307a6170ab:-7fed'.
 - Triple 'http://a.com/ontology#Mother @owl:equivalentClass -2839338:1307a6170ab:-7fcf'.
 - Triple 'http://a.com/ontology#Nephew @owl:equivalentClass -2839338:1307a6170ab:-7fb0'.
 - Triple 'http://a.com/ontology#Nephew @rdfs:subClassOf -2839338:1307a6170ab:-7fa8'.
 - Triple 'http://a.com/ontology#Niece @owl:equivalentClass -2839338:1307a6170ab:-7fe9'.
 - Triple 'http://a.com/ontology#Niece @rdfs:subClassOf -2839338:1307a6170ab:-7fe1'.
 - Triple 'http://a.com/ontology#Parent @owl:equivalentClass -2839338:1307a6170ab:-7ffb'.
 - Triple 'http://a.com/ontology#Person @owl:equivalentClass -2839338:1307a6170ab:-7fc9'.
 - Triple 'http://a.com/ontology#Relative @owl:equivalentClass -2839338:1307a6170ab:-7fd7'.
 - Triple 'http://a.com/ontology#Sibling @owl:equivalentClass -2839338:1307a6170ab:-7ff7'.
 - Triple 'http://a.com/ontology#Sister @owl:equivalentClass -2839338:1307a6170ab:-7ff3'.
 - Triple 'http://a.com/ontology#Son @owl:equivalentClass -2839338:1307a6170ab:-7ff0'.
 - Triple 'http://a.com/ontology#Uncle @owl:equivalentClass -2839338:1307a6170ab:-7fe0'.
 - Triple 'http://a.com/ontology#Uncle @rdfs:subClassOf -2839338:1307a6170ab:-7fd8'.
 - Triple 'http://a.com/ontology#Woman @owl:equivalentClass -2839338:1307a6170ab:-7fc6'.
 - Triple 'http://grandpa.mowa.justtrade.net#StepMother @owl:equivalentClass -2839338:1307a6170ab:-7fa7'.
With key '110000101000001010' we got these triples :
Subject   : 'Blank,Concrete'.
Predicate : 'Concrete,URI'.
Object    : 'Concrete,URI'.
 - Triple '-2839338:1307a6170ab:-7fa5 @rdf:first http://grandpa.mowa.justtrade.net#Woman'.
 - Triple '-2839338:1307a6170ab:-7fa5 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fa6 @rdf:first http://grandpa.mowa.justtrade.net#StepParent'.
 - Triple '-2839338:1307a6170ab:-7fa7 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fa8 @owl:hasValue http://a.com/ontology#Male'.
 - Triple '-2839338:1307a6170ab:-7fa8 @owl:onProperty http://a.com/ontology#hasSex'.
 - Triple '-2839338:1307a6170ab:-7fa8 @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fa9 @rdf:first http://a.com/ontology#Man'.
 - Triple '-2839338:1307a6170ab:-7fa9 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7faa @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fab @owl:onProperty http://a.com/ontology#hasAunt'.
 - Triple '-2839338:1307a6170ab:-7fab @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fad @owl:onProperty http://a.com/ontology#hasUncle'.
 - Triple '-2839338:1307a6170ab:-7fad @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7faf @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fb0 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fb1 @rdf:first http://a.com/ontology#Man'.
 - Triple '-2839338:1307a6170ab:-7fb1 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fb2 @rdf:first http://a.com/ontology#Parent'.
 - Triple '-2839338:1307a6170ab:-7fb3 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fb4 @rdf:first http://a.com/ontology#Man'.
 - Triple '-2839338:1307a6170ab:-7fb4 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fb5 @rdf:first http://a.com/ontology#Sibling'.
 - Triple '-2839338:1307a6170ab:-7fb6 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fb7 @rdf:first http://a.com/ontology#Person'.
 - Triple '-2839338:1307a6170ab:-7fb7 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fb9 @owl:onProperty http://a.com/ontology#hasParent'.
 - Triple '-2839338:1307a6170ab:-7fb9 @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fba @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fbb @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fbc @owl:onProperty http://a.com/ontology#hasNiece'.
 - Triple '-2839338:1307a6170ab:-7fbc @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fbe @owl:onProperty http://a.com/ontology#hasNephew'.
 - Triple '-2839338:1307a6170ab:-7fbe @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fbf @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fc0 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fc1 @rdf:first http://a.com/ontology#Woman'.
 - Triple '-2839338:1307a6170ab:-7fc2 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fc3 @rdf:first http://a.com/ontology#Person'.
 - Triple '-2839338:1307a6170ab:-7fc3 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fc5 @owl:hasValue http://a.com/ontology#Female'.
 - Triple '-2839338:1307a6170ab:-7fc5 @owl:onProperty http://a.com/ontology#hasSex'.
 - Triple '-2839338:1307a6170ab:-7fc5 @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fc6 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fc7 @rdf:first http://a.com/ontology#Woman'.
 - Triple '-2839338:1307a6170ab:-7fc7 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fc8 @rdf:first http://a.com/ontology#Man'.
 - Triple '-2839338:1307a6170ab:-7fc9 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fca @rdf:first http://a.com/ontology#Male'.
 - Triple '-2839338:1307a6170ab:-7fca @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fcb @rdf:first http://a.com/ontology#Female'.
 - Triple '-2839338:1307a6170ab:-7fcc @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fcd @rdf:first http://a.com/ontology#Woman'.
 - Triple '-2839338:1307a6170ab:-7fcd @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fce @rdf:first http://a.com/ontology#Parent'.
 - Triple '-2839338:1307a6170ab:-7fcf @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fd0 @rdf:first http://a.com/ontology#Sibling'.
 - Triple '-2839338:1307a6170ab:-7fd0 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fd1 @rdf:first http://a.com/ontology#Uncle'.
 - Triple '-2839338:1307a6170ab:-7fd2 @rdf:first http://a.com/ontology#Niece'.
 - Triple '-2839338:1307a6170ab:-7fd3 @rdf:first http://a.com/ontology#Nephew'.
 - Triple '-2839338:1307a6170ab:-7fd4 @rdf:first http://a.com/ontology#Aunt'.
 - Triple '-2839338:1307a6170ab:-7fd5 @rdf:first http://a.com/ontology#Parent'.
 - Triple '-2839338:1307a6170ab:-7fd6 @rdf:first http://a.com/ontology#Child'.
 - Triple '-2839338:1307a6170ab:-7fd7 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fd8 @owl:hasValue http://a.com/ontology#Male'.
 - Triple '-2839338:1307a6170ab:-7fd8 @owl:onProperty http://a.com/ontology#hasSex'.
 - Triple '-2839338:1307a6170ab:-7fd8 @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fd9 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fda @owl:onProperty http://a.com/ontology#hasNiece'.
 - Triple '-2839338:1307a6170ab:-7fda @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fdc @owl:onProperty http://a.com/ontology#hasNephew'.
 - Triple '-2839338:1307a6170ab:-7fdc @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fdd @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fde @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fdf @rdf:first http://a.com/ontology#Man'.
 - Triple '-2839338:1307a6170ab:-7fe0 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fe1 @owl:hasValue http://a.com/ontology#Female'.
 - Triple '-2839338:1307a6170ab:-7fe1 @owl:onProperty http://a.com/ontology#hasSex'.
 - Triple '-2839338:1307a6170ab:-7fe1 @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fe2 @rdf:first http://a.com/ontology#Woman'.
 - Triple '-2839338:1307a6170ab:-7fe2 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fe3 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fe4 @owl:onProperty http://a.com/ontology#hasAunt'.
 - Triple '-2839338:1307a6170ab:-7fe4 @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fe6 @owl:onProperty http://a.com/ontology#hasUncle'.
 - Triple '-2839338:1307a6170ab:-7fe6 @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fe8 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fe9 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fea @rdf:first http://a.com/ontology#Person'.
 - Triple '-2839338:1307a6170ab:-7fea @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fec @owl:hasValue http://a.com/ontology#Male'.
 - Triple '-2839338:1307a6170ab:-7fec @owl:onProperty http://a.com/ontology#hasSex'.
 - Triple '-2839338:1307a6170ab:-7fec @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7fed @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7fee @rdf:first http://a.com/ontology#Child'.
 - Triple '-2839338:1307a6170ab:-7fee @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7fef @rdf:first http://a.com/ontology#Man'.
 - Triple '-2839338:1307a6170ab:-7ff0 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7ff1 @rdf:first http://a.com/ontology#Woman'.
 - Triple '-2839338:1307a6170ab:-7ff1 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7ff2 @rdf:first http://a.com/ontology#Sibling'.
 - Triple '-2839338:1307a6170ab:-7ff3 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7ff4 @rdf:first http://a.com/ontology#Person'.
 - Triple '-2839338:1307a6170ab:-7ff4 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7ff6 @owl:onProperty http://a.com/ontology#hasSibling'.
 - Triple '-2839338:1307a6170ab:-7ff6 @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7ff7 @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7ff8 @rdf:first http://a.com/ontology#Person'.
 - Triple '-2839338:1307a6170ab:-7ff8 @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7ffa @owl:onProperty http://a.com/ontology#hasChild'.
 - Triple '-2839338:1307a6170ab:-7ffa @rdf:type owl:Restriction'.
 - Triple '-2839338:1307a6170ab:-7ffb @rdf:type owl:Class'.
 - Triple '-2839338:1307a6170ab:-7ffc @rdf:first http://a.com/ontology#Woman'.
 - Triple '-2839338:1307a6170ab:-7ffc @rdf:rest rdf:nil'.
 - Triple '-2839338:1307a6170ab:-7ffd @rdf:first http://a.com/ontology#Child'.
 - Triple '-2839338:1307a6170ab:-7ffe @rdf:type owl:Class'.

-2839338:1307a6170ab:-7ff8
*/