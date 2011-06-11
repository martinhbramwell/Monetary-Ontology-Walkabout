package net.justtrade.rest.handlers.rdf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import net.justtrade.rest.mowa.AbstractStudyExtension;
import net.justtrade.rest.util.RDF_Analyzer;
import net.justtrade.rest.util.UnsupportedTypeException;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Node_Blank;
import com.hp.hpl.jena.graph.Node_Literal;
import com.hp.hpl.jena.graph.Node_URI;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Index;
import com.tinkerpop.blueprints.pgm.IndexableGraph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.util.IndexableGraphHelper;
import com.tinkerpop.rexster.RexsterApplicationGraph;
import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionConfiguration;

public class RDF_Loader {

	public static final String CLASS_NAME = "\nRDF_Loader.";

	private static final String GRAPH_ARCHIVE = "location-of-graph-archive";
	private static final String FIELD_ENTITY_NAME = "name";


	private long refVertexId = -1;
	private Vertex refVertex = null;

	private Index<Vertex> idxVertices = null;

	
	public void injectRDF(AbstractStudyExtension caller, String tripleFile, String refNode, RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "injectRDF() --> ";
		
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();
		ExtensionConfiguration configuration = rag.findExtensionConfiguration(caller.getExtensionNameSpace(), caller.getExtensionName());
		Map<String, String> cfg = configuration.tryGetMapFromConfiguration();

		String pathArchive = cfg.get(GRAPH_ARCHIVE) + "/";
		System.out.println(sMETHOD + "File archive path : " + pathArchive);
		
		try {
			System.out.println(sMETHOD + "Writing " + tripleFile + " contents to triple store.");
			writeToGraphStore(refNode, tripleFile, (IndexableGraph)rag.getGraph());
			
		} catch (MalformedURLException mfuex) {
			System.out.println(sMETHOD + "* * * Bad URL failure * * * \n" + mfuex.getLocalizedMessage() + "\n" + mfuex.getStackTrace());

		} catch (Exception ex) {
			System.out.println(sMETHOD + "* * * Input/output problem with upload file * * * \n" + ex.getLocalizedMessage());

		}

		System.out.println(sMETHOD + "Finished graph injection : " + tripleFile + ".");

		RDF_Analyzer.analyzeModelData();			  	  	


	}

	private void writeToGraphStore (String refNode, String tripleFile, IndexableGraph graph) throws MalformedURLException
	{
		final String sMETHOD = CLASS_NAME + "writeToGraphStore(String, String, IndexableGraph) --> ";

		Model model = ModelFactory.createDefaultModel();
		model.read("file:\\" + tripleFile);

		StmtIterator it = model.listStatements();
		while (it.hasNext()) {
			Triple triple = it.next().asTriple();
			try {
				insertIntoDb(getReferenceVertex(refNode, graph), triple, graph);

			} catch (Exception ex) {
				System.out.println(sMETHOD + " * * *  Hit an exception on triple " + triple + "\n" + ex.getStackTrace());
			}
		}
	}

	/**
	 * Get the reference node if already available, otherwise create it.
	 * @param neoService the reference to the Neo service.
	 * @return a Neo4j Node object reference to the reference node.
	 * @throws Exception if thrown.
	 */
	private Vertex getReferenceVertex (String refNodeName, IndexableGraph graph)
	{ 
		final String sMETHOD = CLASS_NAME + "getReferenceVertex(String, Graph) --> ";

		if (refVertexId > -1)
		{
			refVertex = graph.getVertex(refVertexId);
		} else {
			refVertex = IndexableGraphHelper.addUniqueVertex(graph, null, getVerticesIndex (graph), FIELD_ENTITY_NAME, refNodeName);
			refVertexId = ((Long) refVertex.getId()).longValue();

			System.out.println(sMETHOD + "Created reference vertex with ID : '" + refVertexId + "'.");
		}
		return refVertex;

	}

	/**
	 * Inserts selected entities and relationships from Triples extracted
	 * from the OWL document by the Jena parser. Only entities which have
	 * a non-blank node for the subject and object are used. Further, only
	 * relationship types listed in OntologyRelationshipTypes enum are 
	 * considered. In addition, if the enum specifies that certain 
	 * relationship types have an inverse, the inverse relation is also
	 * created here.
	 * @param neoService a reference to the Neo service.
	 * @param indexService a reference to the Index service (for looking
	 * up Nodes by name).
	 * @param fileNode a reference to the Node that is an entry point into
	 * this ontology. This node will connect to both the subject and object 
	 * nodes of the selected triples via a CONTAINS relationship. 
	 * @param triple a reference to the Triple extracted by the Jena parser.
	 * @throws Exception if thrown.
	 */
	private void insertIntoDb(Vertex referenceVertex, Triple triple, IndexableGraph graph)
	{
		final String sMETHOD = CLASS_NAME + "insertIntoDb(Triple) --> ";

		RDF_Analyzer.collectModelAnalysisData(triple);

		Node subject = triple.getSubject();
		Node predicate = triple.getPredicate();
		Node object = triple.getObject();

		Vertex subjectVertex = null;
		Vertex objectVertex = null;

		try {



// get or create the subject and object nodes
			subjectVertex = getEntityVertex(subject, graph);
			objectVertex = getEntityVertex(object, graph);

			if (isConnected(graph, subjectVertex, objectVertex))
			{

				System.out.println(sMETHOD + " * * *  ALREADY CONNECTED  * * * ");

			} else {

				System.out.println
				(
						"\nTrying for triple between -- "
						+ "\n S : " + subjectVertex.toString()  + " (" + subject + ")."
						+ "\n O : " + objectVertex.toString()  + " (" + object + ")."
				);

				try {

					if ( ! predicate.isURI()) { throw new UnsupportedTypeException
						(
								  "\n**************************************"
								+ "\n**************************************"
								+ "\n***                                ***"
								+ "\n*** Unsupported Jena edge type, as ***"
								+ "\n***        indicated below.        ***"
								+ "\n***                                ***"
								+ "\n**************************************"
								+ "\n**************************************"
								+ "\n" + predicate.toString()
								+ "\n Is Blank ? [" + predicate.isBlank() + "]. Is Concrete ? [" + predicate.isConcrete() + "]. Is Literal ? [" + predicate.isLiteral() + "]. Is Variable ? [" + predicate.isVariable() + "]."  
						);
					} else {
						System.out.println(sMETHOD + "A"
								+ "\n" + ((Node_URI) predicate).getURI() 
								+ "\n" + ((Node_URI) predicate).getLocalName()
								+ "\n" + ((Node_URI) predicate).getNameSpace()
						);
						Edge edge = graph.addEdge(((Node_URI) predicate).getURI(), subjectVertex, objectVertex, ((Node_URI) predicate).getLocalName());
						System.out.println(sMETHOD + "B");
						logTriple(subjectVertex, edge, objectVertex);
						System.out.println(sMETHOD + "D");
					}

				} catch (Throwable th) {
					th.printStackTrace();
				}



			}


		} catch (UnsupportedTypeException ustex) {
			System.out.println("We can just silently ignore these"); 
		}

	}

	private Vertex getEntityVertex(Node node, IndexableGraph graph) throws UnsupportedTypeException
	{
		final String sMETHOD = CLASS_NAME + "getEntityVertex(Node, IndexableGraph) --> ";

		String value = null;
		boolean bSupported = false;

		// Possibilities : Literal, URI, blank
		if (node.isBlank())   {value = ((Node_Blank) node).getBlankNodeId().toString(); bSupported = true;}
		if (node.isLiteral()) {value = ((Node_Literal) node).getLiteral().toString(); bSupported = true;}
		if (node.isURI())     {value = ((Node_URI) node).getURI(); bSupported = true;}

		if ( ! bSupported) { throw new UnsupportedTypeException
			(
					   sMETHOD 
					+ "\n**********************************************"
					+ "\n**********************************************"
					+ "\n***                                        ***"
					+ "\n*** Unsupported Jena node type, 'Variable' ***"
					+ "\n***        on the node below.              ***"
					+ "\n***                                        ***"
					+ "\n**********************************************"
					+ "\n**********************************************"
					+ "\n" + node.toString()
			);}
		//		System.out.println(sMETHOD + "Examine URI '" + value + "'.");

		Vertex vertex = IndexableGraphHelper.addUniqueVertex
		(graph, null, getVerticesIndex (graph), FIELD_ENTITY_NAME, value);

		return vertex;
	}

	/*
	 * 
	 */
	private Index<Vertex> getVerticesIndex (IndexableGraph graph)
	{ 
		final String sMETHOD = CLASS_NAME + "getVerticesIndex(String, Graph) --> ";

		if (idxVertices == null)
		{
			idxVertices = graph.getIndex("vertices", Vertex.class);	
			System.out.println(sMETHOD + "Retrieved vertex index : '" + idxVertices.toString() + "'.");
		}

		return idxVertices;
	}

	/**
	 * Loops through the relationships and returns true if the source
	 * and target nodes are connected using the specified relationship
	 * type and direction.
	 * @param neoService a reference to the GraphDatabaseService.
	 * @param sourceNode the source Node object.
	 * @param relationshipType the type of relationship.
	 * @param direction the direction of the relationship.
	 * @param targetNode the target Node object.
	 * @return true or false.
	 * @throws Exception if thrown.
	 */

	private boolean isConnected
	(
			IndexableGraph graph
			, Vertex vtxSource
			, Vertex vtxTarget
	) 
	{
		
		Object propTarget = vtxTarget.getProperty(FIELD_ENTITY_NAME);
		for (Edge predicate : vtxSource.getOutEdges())
		{
			Object propTrial = predicate.getInVertex().getProperty(FIELD_ENTITY_NAME);
			if (propTrial.equals(propTarget)) return true;
		}

		for (Edge predicate : vtxSource.getInEdges())
		{
			Object propTrial = predicate.getOutVertex().getProperty(FIELD_ENTITY_NAME);
			if (propTrial.equals(propTarget)) return true;
		}

		return false;
	}

	/**
	 * Convenience method to log the triple when it is inserted into the
	 * database.
	 * @param Vertex vtxSource; the subject of the triple.
	 * @param Edge relation; the predicate of the triple.
	 * @param Vertex vtxTarget; the object of the triple.
	 */
	private void logTriple(Vertex vtxSource, Edge edge, Vertex vtxTarget) {
		System.out.println
		(
				"Wrote triple -- "
				+ " S:" + vtxSource.getProperty(FIELD_ENTITY_NAME) 
				+ " P:" + edge.toString()
//				+ " P:" + edge.getProperty(EDGE_NAME)
				+ " O:" + vtxTarget.getProperty(FIELD_ENTITY_NAME)
		);
		//	    log.info("(" + sourceNode.getProperty(FIELD_ENTITY_NAME) +
		//	      "," + ontologyRelationshipType.name() + 
		//	      "," + targetNode.getProperty(FIELD_ENTITY_NAME) + ")");
	}

}
