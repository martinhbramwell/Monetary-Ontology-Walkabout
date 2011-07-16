package net.justtrade.rest.handlers.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.IndexableGraph;
import com.tinkerpop.blueprints.pgm.Vertex;

/**
* 
* Convenience methods to simplify maintaining structure.
* 
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class StructureHelper {

	private static final Logger logger = LoggerFactory.getLogger(StructureHelper.class);
	
	public static final int EITHER_DIRECTION = 0;
	public static final int FROM_SOURCE_TO_TARGET_ONLY = EITHER_DIRECTION + 1;
	public static final int BOTH_DIRECTIONS = FROM_SOURCE_TO_TARGET_ONLY + 1;
	
	/**
	 * Creates an edge between two nodes iff none have been created previously.
	 * 
	 * @param _subjectVertex the out node
	 * @param _objectVertex the in node
	 * @param _edgeName a name to give to the new edge
	 * @param _graph which graph we are supposed to be working with
	 * @throws  none
	 * @throws Exception if thrown.
	 */
	
	public static void connect
	(
		  Vertex _subjectVertex
		, Vertex _objectVertex
		, String _edgeName
		, IndexableGraph _graph
	) {
		
		final String sMETHOD = "connect(Vertex, Vertex, String, IndexableGraph) --> ";
		
		
		logger.debug(sMETHOD + "Already connected ?  Skip out.");
		if (isConnected(_subjectVertex, _objectVertex, StructureHelper.FROM_SOURCE_TO_TARGET_ONLY)) return;
		
		
		logger.debug(sMETHOD + "Connecting...");
		_graph.addEdge(null, _subjectVertex, _objectVertex, _edgeName);
		
		logger.debug(sMETHOD + "Done.");
	}
	
	/**
	 * Loops through the relationships of the source and returns true if it connects 
	 * to the target node using the specified relationship type and direction.
	 * 
	 * @param vtxSource a reference to the GraphDatabaseService.
	 * @param vtxTarget the source Node object.
	 * @param edgeType the type of relationship.
	 * @return true or false.
	 * @throws  none
	 * @throws Exception if thrown.
	 */

	public static boolean isConnected
	(
			  Vertex vtxSource
			, Vertex vtxTarget
			, int edgeType
	) 
	{
		int cnt = 0;
		int idx = 0;
		final String sMETHOD = "isConnected(Vertex, Vertex , int) --> ";
		
		switch (edgeType) {
		
			case EITHER_DIRECTION :
			case FROM_SOURCE_TO_TARGET_ONLY :
				for (Edge predicate : vtxSource.getOutEdges())
				{
					idx++;
					logger.debug(sMETHOD + "Got # " + idx);
					if (vtxTarget.equals(predicate.getInVertex())) {
						cnt++;
						break;
					}
				}
				idx = 0;
				logger.debug(sMETHOD + "Returning " + (cnt) + " == 1? " + (cnt == 1));
				if (edgeType == FROM_SOURCE_TO_TARGET_ONLY) return (cnt == 1);
				
			case BOTH_DIRECTIONS :
				for (Edge predicate : vtxSource.getInEdges())
				{
					idx++;
					logger.debug(sMETHOD + "Got # " + idx);
					if (vtxTarget.equals(predicate.getOutVertex())) {
						cnt++;
						break;
					}
				}
				logger.debug(sMETHOD + "Returning " + (cnt) + " == 2? " + (cnt == 2));
				if (edgeType == BOTH_DIRECTIONS) return (cnt == 2);
				
		}
		logger.debug(sMETHOD + "Returning " + (cnt) + " > 0? " + (cnt > 0));
		return (cnt > 0);
		
	}

	

}
