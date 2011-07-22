package net.justtrade.rest.handlers.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONException;

import com.tinkerpop.blueprints.pgm.TransactionalGraph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.rexster.RexsterApplicationGraph;
import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionConfiguration;
import com.tinkerpop.rexster.extension.ExtensionResponse;

import net.justtrade.rest.handlers.http.UploadHandler;

/**
* This is the work horse for base path actions, on the expectation that most graph-at-a-time operations will have very similar functionality.
* 
*  A thin front-end will handle project specific details and pass the rest of the work back here. 
* 
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class BasePathManager {

	private static final Logger logger = LoggerFactory.getLogger(BasePathManager.class);
	
	private Vertex refVertex = null;
//	private long refVertexId = -1;

	private Map<String, String> configurationProperties;
	
	
    /**
	 * This is the method designated to handle PUT on the root of any Blueprints graph.
	 * 
     * This is the REST style handler for the PUT method, operating on entire graphs at once.
     * It can only handle one file at at a time.
     * It should be IDEMPOTENT, but this has not been tested.
     * @param _context RexsterResourceContext supplied by server
     * @param _extension an abstract class specialized with details of the graph to be worked supplied by the caller method
     * @return ExtensionResponse to be reformatted by caller and then by the server
     * @see BasePathManager
     */
	public ExtensionResponse put(RexsterResourceContext _context, ExtensionAbstract _extension)
	{
		final String sMETHOD = "put(RexsterResourceContext, ExtensionAbstract) :\n";
		
		logger.info(sMETHOD + "ProcessIng Multipart Content!");
		
		configurationProperties = initConfig(_context, _extension);
		if (configurationProperties == null) {
			String message = "Found no configuration to load using : <namespace>" + _extension.getExtensionNameSpace() + "</namespace> and <name>" + _extension.getExtensionName() + "</name>.";
			logger.warn(sMETHOD + message);
			return ExtensionResponse.error(message);
			
		} else {
			
			UploadHandler handler = new UploadHandler();
			return handler.handleUpload(configurationProperties, _context);
		}
	}
		

    /**
	 * This is the method designated to handle DELETE on the root of any Blueprints graph.
	 * 
     * This is the REST style handler for the DELETE method, operating on entire graphs at once.
     * Since multiple graphs may have been supplied, the caller must know the original file identifier.
     * This will not guarantee that the nodes and edges of 
     * the original file all get deleted or that only those get deleted.
     * 
     * @param _context RexsterResourceContext supplied by server
     * @param _extension an abstract class specialized with details of the graph to be worked supplied by the caller method
     * @param _referenceVertex the caller needs to be able to specify the root vertex of the originally loaded file 
     * @return ExtensionResponse to be reformatted by caller and then by the server
     * @see BasePathManager
     */
	public ExtensionResponse delete(RexsterResourceContext _context, ExtensionAbstract _extension, String _referenceVertex)
	throws ReferenceNodeNotFoundException, MultipleReferenceNodesException
	{
		final String sMETHOD = "delete(RexsterResourceContext, ExtensionAbstract, String) :\n";
		
		logger.info(sMETHOD + "Preparing delete : ");
		
		configurationProperties = initConfig(_context, _extension);
		if (configurationProperties == null) {
			String message = "Found no configuration to load using : <namespace>" + _extension.getExtensionNameSpace() + "</namespace> and <name>" + _extension.getExtensionName() + "</name>.";
			logger.error(sMETHOD + message);
			return ExtensionResponse.error(message);
			
		} else {
			
			JSONObject jsonRslt = new JSONObject();
			try {
				
				logger.debug(sMETHOD + "\n\n\n\nReady to delete : " + _referenceVertex);
				
				RexsterApplicationGraph rag = _context.getRexsterApplicationGraph();
				TransactionalGraph graph = (TransactionalGraph) rag.getGraph();

				refVertex = ManagementIndexHelper.getReferenceVertex(graph, true);
				logger.debug(sMETHOD + "\n\n\n\n Found vertex #" + refVertex.getId());
				
				jsonRslt.put("behaviour", "DELETE record");
				
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return ExtensionResponse.ok(jsonRslt);
		}
	}

    /**
	 * This is the method designated to handle POST on the root of any Blueprints graph.
	 * 
     * This is the REST style handler for the POST method, operating on entire graphs at once.
     * 
     * It can handle multiple files in sequence.
     * IDEMPOTENCY should be not be expected.
	 * 
     * @param _context RexsterResourceContext supplied by server
     * @param _extension an abstract class specialized with details of the graph to be worked supplied by the caller method
     * @return ExtensionResponse to be reformatted by caller and then by the server
     * @see BasePathManager
     */
	public ExtensionResponse post(RexsterResourceContext _context, ExtensionAbstract _extension)
	{
		final String sMETHOD = "post(RexsterResourceContext, ExtensionAbstract) :\n";
		
		configurationProperties = initConfig(_context, _extension);
		if (configurationProperties == null) {
			String message = "Found no configuration to load using : <namespace>" + _extension.getExtensionNameSpace() + "</namespace> and <name>" + _extension.getExtensionName() + "</name>.";
			logger.error(sMETHOD + "\n\n\n\n" + message);
			return ExtensionResponse.error(message);
			
		} else {
			
			UploadHandler handler = new UploadHandler();
			return handler.handleUpload(configurationProperties, _context);
		}
	}

    /**
	 * This is the method designated to handle GET on the root of any Blueprints graph * * * NOT IMPLEMENTED * * * .
	 * 
	 * Typically it will not be called for an empty GET.  The caller will need to specify a collection reference node. 
	 * 
     * @param _context RexsterResourceContext supplied by server
     * @param _extension an abstract class specialized with details of the graph to be worked supplied by the caller method
     * @param _referenceVertex the caller needs to be able to specify the root vertex of the originally loaded file 
     * @return ExtensionResponse to be reformatted by the server
     * @see BasePathManager
     */

	public ExtensionResponse get(RexsterResourceContext _context, ExtensionAbstract _extension, String _referenceVertex)
	{
		final String sMETHOD = "get(RexsterResourceContext, ExtensionAbstract) :\n";
		final String REST_PATH = "GET graph's root";
		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("\"title\": \"List available persons.\" \"href\": \"" + _extension.getBasePath() + "/persons\"" + REST_PATH);
		hateaos.add("\"title\": \"List available relationships.\" \"href\": \"" + _extension.getBasePath() + "/relationships\"" + REST_PATH);
		RexsterApplicationGraph rag = _context.getRexsterApplicationGraph();

		logger.info(sMETHOD + "RexsterResourceContext :: getRequestObject " + _context.getRequestObject().toString());
		return _extension.toStringIt(rag.getGraph(), REST_PATH, hateaos);
	}
	
	
	
	private Map<String, String> initConfig (RexsterResourceContext _context, ExtensionAbstract _extension) {
		if (_context == null) return null;
		
		RexsterApplicationGraph rag = _context.getRexsterApplicationGraph();
		if (rag == null) return null;
		
		ExtensionConfiguration configuration = rag.findExtensionConfiguration(_extension.getExtensionNameSpace(), _extension.getExtensionName());
		if (configuration == null) return null;
		
		Map<String, String> cfg = configuration.tryGetMapFromConfiguration();
		if (cfg == null) return null;
		
		Map<String, String> configurationProperties = new HashMap<String, String>();
		configurationProperties.put(UploadHandler.TEMP_FILES, cfg.get(UploadHandler.TEMP_FILES));
		configurationProperties.put(UploadHandler.GRAPH_ARCHIVE, cfg.get(UploadHandler.GRAPH_ARCHIVE));
		configurationProperties.put(UploadHandler.EXTENSION_NAME_SPACE, _extension.getExtensionNameSpace());
		configurationProperties.put(UploadHandler.EXTENSION_NAME, _extension.getExtensionName());
		
		return configurationProperties;
	}

}
