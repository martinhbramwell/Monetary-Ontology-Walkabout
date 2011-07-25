package net.justtrade.rest.handlers.graph;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.pgm.CloseableSequence;
import com.tinkerpop.blueprints.pgm.Index;
import com.tinkerpop.blueprints.pgm.IndexableGraph;
import com.tinkerpop.blueprints.pgm.TransactionalGraph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.util.IndexableGraphHelper;

/**
 * Vertex management involves connecting all nodes of a particular collection to a single organizing node.
 * Thus, if you load a file of nodes, you would connect each one of them to an extra node created solely for the purpose of storing details of that file.
 * That node would, in its turn, be connected to the graph's reference node.
 * 
 * When the need arises to work with all nodes of a particular collection, deleting them for example, they can all be found from a single index created for that purpose. 
 * 
 * @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
 */
public class ManagementIndexHelper {

	public static final String MANAGEMENT_INDEX_NAME = "mgmnt";
	public static final String MANAGEMENT_NODES_KEY = "mngd";
	public static final String REFERENCE_NODE = "referenceVertex";

	private static final Logger logger = LoggerFactory.getLogger(ManagementIndexHelper.class);

	/**
	 * Delete a collection of nodes bound to a single root reference node.
	 * @param _refNodeName The value to be indexed by the management index
	 * @param _graph the reference to the graph to be accessed.
	 * @return a Set of vertex IDs all directly related by a single reference node.
	 * @throws Exception if thrown.
	 */
	public static Long deleteVertexCollection 
	(		  String _refNodeName
			, TransactionalGraph _graph
	) throws ReferenceNodeNotFoundException, CollectionIndexNotFoundException
	{ 
		final String sMETHOD = "deleteVertexCollection(String, TransactionalGraph) --> ";
		logger.info(sMETHOD + " Starting.");
		
		long cnt = 0;
		
		CloseableSequence<Vertex> itr = getVertexCollection(_refNodeName, _graph);
		for ( Vertex vtx : itr) {
			
			logger.info(sMETHOD + " Murdering vertex " + vtx + "and all connected adges.");
			_graph.removeVertex(vtx);
			cnt++;
		}
		
		logger.info(sMETHOD + " Done.");
		return new Long(cnt);
	}



	/**
	 * Get a collection of nodes bound to a single root reference node.
	 * @param _refNodeName The value to be indexed by the management index
	 * @param _graph the reference to the graph to be accessed.
	 * @return a Set of vertex IDs all directly related by a single reference node.
	 * @throws Exception if thrown.
	 */
	public static CloseableSequence<Vertex> getVertexCollection 
	(		  String _refNodeName
			, TransactionalGraph _graph
	) throws ReferenceNodeNotFoundException, CollectionIndexNotFoundException
	{ 
		final String sMETHOD = "getVertexCollection(String, TransactionalGraph) --> ";
		logger.info(sMETHOD + " Starting.");
		
		Index<Vertex> idx = null;
		CloseableSequence<Vertex> vertices = null;
		
		if (logger.isDebugEnabled()) {
			for(Index<?> index : ((IndexableGraph)_graph).getIndices())
			{
				logger.info(sMETHOD + " Index " + index.getIndexName());
			}
		}

		try 
		{
			idx = ((IndexableGraph)_graph).getIndex(MANAGEMENT_INDEX_NAME, Vertex.class);
			if (null == idx) throw new CollectionIndexNotFoundException(_refNodeName);
			
			vertices = idx.get(MANAGEMENT_INDEX_NAME, _refNodeName);
			if (null == vertices) throw new ReferenceNodeNotFoundException(_refNodeName);
			
		} catch (NullPointerException rtex) {
			
			throw new ReferenceNodeNotFoundException(_refNodeName);
			
		} catch (RuntimeException rtex) {
			
			throw new CollectionIndexNotFoundException(_refNodeName);
			
			
		}
		
		
		logger.info(sMETHOD + " Done.");
		return vertices;
	}



	/**
	 * Get a collection reference node if already available, otherwise possibly create it.
	 * The node will be bound to the root reference node.
	 * Nodes intended to be treated as a group, all from the same original data file for example
	 * can be discovered rapidly from this node.
	 * Allows a subsidiary reference node value to be specified explicitly.
	 * The default value of the name of the index and the name of indexable key will be used.  
	 * @param _refNodeName The value to be indexed by the management index
	 * @param _graph the reference to the graph to be accessed.
	 * @param _makeIfNotFound if true then the method will create a new node if there isn't one already
	 * @return the unique vertex to which all nodes are indirectly or directly connected.
	 * @throws ReferenceNodeNotFoundException 
	 * @throws MultipleReferenceNodesException 
	 * @throws Exception if thrown.
	 */
	public static Vertex getCollectionRefVertex 
	(		  String _refNodeName
			, TransactionalGraph _graph
			, boolean _makeIfNotFound
	)
		throws ReferenceNodeNotFoundException, MultipleReferenceNodesException
	{ 
		final String sMETHOD = "getCollectionRefVertex(String, TransactionalGraph, boolean) --> ";
		logger.debug(sMETHOD + " Starting.");
		
		Vertex rsltVertex = getReferenceVertex
		(
			  MANAGEMENT_NODES_KEY
			, _refNodeName
			, MANAGEMENT_INDEX_NAME
			, _graph
			, _makeIfNotFound
		);
		
		Vertex rootVertex = getReferenceVertex (_graph, true);
		
		StructureHelper.connect(rootVertex, rsltVertex, "Reference node : " + _refNodeName, ((IndexableGraph)_graph));
		
		logger.debug(sMETHOD + " Done.");
		return rsltVertex;
	}

	/**
	 * Get THE reference node if already available, otherwise possibly create it.
	 * Uses default values for index name, reference node key and reference node value implicitly  
	 *    (respectively MANAGEMENT_INDEX_NAME, MANAGEMENT_NODES_KEY, REFERENCE_NODE) 
	 * @param _graph the reference to the graph to be accessed.
	 * @param _makeIfNotFound if true will create a node if there isn't one.
	 * @return the unique vertex to which all nodes are indirectly or directly connected.
	 * @throws ReferenceNodeNotFoundException 
	 * @throws MultipleReferenceNodesException 
	 * @throws Exception if thrown.
	 */
	public static Vertex getReferenceVertex 
	(		  TransactionalGraph _graph
			, boolean _makeIfNotFound
	)
		throws ReferenceNodeNotFoundException, MultipleReferenceNodesException
	{ 
		final String sMETHOD = "getCollectionRefVertex(TransactionalGraph, boolean) --> ";
		logger.debug(sMETHOD + " Starting.");
		
		return getReferenceVertex
		(
			  MANAGEMENT_NODES_KEY
			, REFERENCE_NODE
			, MANAGEMENT_INDEX_NAME
			, _graph
			, _makeIfNotFound
		);
	}

	/**
	 * Get a reference node if already available, otherwise possibly create it.
	 * This can be used to specify a different index name, but still use the default property details
	 * The index name must be specified explicitly, while reference node key and reference node 
	 * value are set to the default values MANAGEMENT_NODES_KEY and REFERENCE_NODE
	 * @param _refIndxName The name of the management index
	 * @param _graph the reference to the graph to be accessed.
	 * @param _makeIfNotFound if true will create a node if there isn't one.
	 * @return the unique vertex to which all nodes are indirectly or directly connected.
	 * @throws ReferenceNodeNotFoundException 
	 * @throws MultipleReferenceNodesException 
	 * @throws Exception if thrown.
	 */
	public static Vertex getReferenceVertex 
	(		  String _refIndxName
			, TransactionalGraph _graph
			, boolean _makeIfNotFound
	)
		throws ReferenceNodeNotFoundException, MultipleReferenceNodesException
	{ 
		final String sMETHOD = "getCollectionRefVertex(String, TransactionalGraph, boolean) --> ";
		logger.debug(sMETHOD + " Starting.");
		
		return getReferenceVertex
		(
			  MANAGEMENT_NODES_KEY
			, REFERENCE_NODE
			, _refIndxName
			, _graph
			, _makeIfNotFound
		);
	}
	
	/**
	 * Get a reference node if already available, otherwise possibly create it.
	 * Allows index, reference node key and reference node value to be specified explicitly  
	 * @param _refNodeKey The property key for the management index
	 * @param _refNodeName The value to be indexed by the management index
	 * @param _refIndxName The name of the management index
	 * @param _graph the reference to the graph to be accessed.
	 * @param _makeIfNotFound if true will create a node if there isn't one.
	 * @return the unique vertex to which all nodes are indirectly or directly connected.
	 * @throws ReferenceNodeNotFoundException 
	 * @throws MultipleReferenceNodesException 
	 * @throws Exception if thrown.
	 */
	public static Vertex getReferenceVertex 
	(
			  String _refNodeKey
			, String _refNodeName
			, String _refIndxName
			, TransactionalGraph _graph
			, boolean _makeIfNotFound
	)
		throws ReferenceNodeNotFoundException, MultipleReferenceNodesException
	{ 
		final String sMETHOD = "getReferenceVertex(String, String, String, TransactionalGraph, boolean) --> ";
		long refVertexId;
		CloseableSequence<Vertex> vertices = null;
		Vertex refVertex = null;
		Index<Vertex> index = null;

		logger.debug(sMETHOD + "Find vertex with property : " 
				+ "\n    Key '" + _refNodeKey + "' = value '" + _refNodeName + "' in "
				+ "\n    Index " + _refIndxName + "."
				+ "\n    Make one ? " + _makeIfNotFound + "."
				+ "\n    (Graph " + ((_graph == null)  ?  "not found"  :  _graph.toString()) + ").");
		
		logger.debug(sMETHOD + "Possibly there's no index, if not make one."); 
		try {
			index = ((IndexableGraph)_graph).getIndex(_refIndxName, Vertex.class);
		} catch (RuntimeException rtex) {
			String msg = rtex.getLocalizedMessage();
			int pos = msg.indexOf("No such index");
			if (-1 < pos) {
				index = null;
			} else {
				logger.warn("(" + pos + ")" + rtex.getLocalizedMessage());
				rtex.printStackTrace();
			}
		}
		if (index == null) {
			logger.info(sMETHOD + "No index : " + _refIndxName + ". Creating now."); 
			index = ((IndexableGraph)_graph).createManualIndex(_refIndxName, Vertex.class);
			logger.debug(sMETHOD + "New index : " + _refIndxName + ". "); 
		}
		
		
		
		logger.debug(sMETHOD + "Got an index to work with. Look for property -- key : '" + _refNodeKey + "' ; '" + _refNodeName + "'."); 
		long cnt = index.count(_refNodeKey, _refNodeName);
		if (cnt == 1) {
			
			logger.debug(sMETHOD + "Found a vertex."); 
			vertices = index.get(_refNodeKey, _refNodeName);
			for (Vertex vtex : vertices) {
				refVertex = vtex;
			}
			logger.debug(sMETHOD + "Found reference vertex ? " + ((refVertex == null)  ?  "no"  :  "yes")); 
			vertices.close();
			
		} else if (cnt < 1) { 
			if (_makeIfNotFound) {

				logger.debug(sMETHOD + "Creating reference vertex named '" + _refNodeName + "'.");
				
				refVertex = IndexableGraphHelper.addUniqueVertex(((IndexableGraph)_graph), null, index, _refNodeKey, _refNodeName);
				index.put(_refNodeKey, _refNodeName, refVertex);
				refVertexId = ((Long) refVertex.getId()).longValue();

				logger.debug(sMETHOD + "Created reference vertex # '" + refVertexId + "' named '" + refVertex.toString() + "'.");
				
			} else {
				throw new ReferenceNodeNotFoundException(_refNodeKey + ":" + _refNodeName);
			}
		} else {
			throw new MultipleReferenceNodesException();
		}
		
		logger.debug(sMETHOD + " Done.");
		
		return refVertex;
	}
	
	/**
	 * Connects a node to the node that points to a collection of nodes, and adds it to that collection's index.
	 * @param _graph an indexable graph
	 * @param _managerVertex the vertex kept as a pointer to a collection of vertices loaded in the same action
	 * @param _managedVertex one of the vertices to be included in the collection.
	 * @return the vertex that was connected to its manager vertex.
	 * @throws  none
	 * @throws Exception if thrown.
	 */
	public static Vertex manageVertex (IndexableGraph _graph, Vertex _managerVertex, Vertex _managedVertex)
	{
		final String sMETHOD = "manageVertex(Vertex, Vertex) --> ";
		logger.debug(sMETHOD + " Add to management index...");
		
		String managerNodeName = _managerVertex.getProperty(MANAGEMENT_NODES_KEY).toString();
		Object managementId = managerNodeName + ":" + _managedVertex.getId().toString();

		logger.debug(sMETHOD + " ... identified by : key '" + MANAGEMENT_NODES_KEY + "' -- value '" + managementId + "'.");
		try {
			
			if (_graph == null) throw new Exception("No graph.");
			
			Index<Vertex> idxMgmnt = ((IndexableGraph)_graph).getIndex(MANAGEMENT_INDEX_NAME, Vertex.class);
			if (idxMgmnt == null) throw new Exception("No index.");
			
			logger.debug(sMETHOD + "Setting management properties ...");
			_managedVertex.setProperty(MANAGEMENT_NODES_KEY, managementId);
			
			logger.debug(sMETHOD + "... adding to index.");
			idxMgmnt.put(MANAGEMENT_NODES_KEY, managementId, _managedVertex);
			
			logger.debug(sMETHOD + "... and connecting to manager node.");
			StructureHelper.connect(_managerVertex, _managedVertex
					, "Reference node : " + managerNodeName, ((IndexableGraph)_graph));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		logger.debug(sMETHOD + " Done.");
		return _managedVertex;
	}
	

}
