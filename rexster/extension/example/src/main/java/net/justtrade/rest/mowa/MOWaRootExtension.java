package net.justtrade.rest.mowa;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.justtrade.rest.handlers.graph.BasePathManager;
import net.justtrade.rest.handlers.graph.MultipleReferenceNodesException;
import net.justtrade.rest.handlers.graph.ReferenceNodeNotFoundException;

import com.tinkerpop.rexster.RexsterApplicationGraph;
import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionMethod;
import com.tinkerpop.rexster.extension.ExtensionNaming;
import com.tinkerpop.rexster.extension.ExtensionDefinition;
import com.tinkerpop.rexster.extension.ExtensionDescriptor;
import com.tinkerpop.rexster.extension.ExtensionRequestParameter;
import com.tinkerpop.rexster.extension.HttpMethod;

import com.tinkerpop.rexster.extension.ExtensionPoint;
import com.tinkerpop.rexster.extension.RexsterContext;
import com.tinkerpop.rexster.extension.ExtensionResponse;


/**
 * 
 * This is adapted from the Rexster Kibble - SimpleRootExtension sample documented here 
 * 
 * 
 * {@link <a href="https://github.com/tinkerpop/rexster/wiki/Extension-Points">Extension-Points</a>} 
 * 
 * One of the extension samples that showcase the methods available to those who wish to extend Rexster.
 *
 * This sample focuses on root-level extensions.  Root-level extensions add the extension
 * to the root of the specified ExtensionPoint.  No additional pathing is taken into consideration
 * when routing to the service method, therefore, if more than one root-level extension is specified
 * then Rexster may appear to misbehave.  Rexster will choose the first extension method match that
 * it can find when processing a request.
 * See {@link MOWaRelationshipsExtension, MOWaPersonsExtension} for details about multiple classes serving one tree of possible URL paths. 
 *
 * @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
 * @see MOWaRelationshipsExtension, MOWaPersonsExtension
 */
@ExtensionNaming(name = MOWaRootExtension.EXTENSION_NAME, namespace = MOWaExtensionAbstract.EXTENSION_NAMESPACE)
public class MOWaRootExtension extends MOWaExtensionAbstract {
	
	private static final Logger logger = LoggerFactory.getLogger(MOWaRootExtension.class);
	
    public static final String EXTENSION_NAME = "stevens";
    public String getExtensionName() {return EXTENSION_NAME;}
    
	public static final String basePath = EXTENSION_NAMESPACE + "/" + EXTENSION_NAME;
    public String getBasePath() {return basePath;}
    
    private static final String COMMENT = "Base path :: " + basePath;


	/*
 	 * curl -svF "CarAds=@.\src\main\resources\data\family.swrl.owl"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
	 * curl -svF "CarAds=@.\src\main\resources\data\TheStevens.owl"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
	 * , method = HttpMethod.GET
	 */
    
    /**
	 * This is the method designated to handle GET on the root of the Ray Stevens Family Ontology, so it should act as an informative front door.
	 * 
	 * It returns a list of available options.
     * It delegates almost all functionality to do with getting sub-segments of the graph to BasePathManager.
     * <br/>* * * hardly implemented at all yet * * * <br/>
     * @param context RexsterResourceContext supplied by server
     * @return ExtensionResponse to be reformatted by the server
     * @see BasePathManager
     */

    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.GET)
	@ExtensionDescriptor(description = "GETs the options available for the 'Stevens' family ontology, with HttpMethod.GET.")
    public ExtensionResponse getBasePath(@RexsterContext RexsterResourceContext context) {
		final String sMETHOD = "getBasePath(RexsterResourceContext) :\n";
		final String REST_PATH = "GET graph's root";

		logger.info(sMETHOD + COMMENT);		
		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("\"title\": \"List available persons.\" \"href\": \"" + basePath + "/persons\"" + REST_PATH);
		hateaos.add("\"title\": \"List available relationships.\" \"href\": \"" + basePath + "/relationships\"" + REST_PATH);
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();

		logger.info(sMETHOD + "RexsterResourceContext :: getRequestObject " + context.getRequestObject().toString());
		return toStringIt(rag.getGraph(), REST_PATH, hateaos);
		
//		BasePathManager manager = new BasePathManager(); 
//		return manager.get(context, this);

    }

    
    /**
     * This is the REST style handler for PUT method, operating on entire graphs at once.
     * It can only handle one file at at a time.
     * It should be IDEMPOTENT, but this has not been tested.
     * It delegates almost all functionality to BasePathManager 
     * @param context RexsterResourceContext supplied by server
     * @return ExtensionResponse to be reformatted by the server
     * @see BasePathManager
     */
	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.PUT)
	@ExtensionDescriptor(description = "PUT a single file towards the 'Stevens' family ontology, with HttpMethod.PUT")
	public ExtensionResponse putBasePath (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = "putBasePath(RexsterResourceContext) :\n";
		logger.info(sMETHOD + COMMENT);
		
		long startTime = System.currentTimeMillis();
		logger.info(sMETHOD + "Time now (start) : " + startTime);
		
		BasePathManager manager = new BasePathManager(); 
		
		ExtensionResponse resp = manager.put(context, this); 
		
		long endTime = System.currentTimeMillis();
		logger.info(sMETHOD + "Time  : " + endTime + " - " + startTime + " = " + (endTime-startTime));
		return resp;

	}

    
    /**
     * This is the REST style handler for POST method, operating on the entire graph at once.
     * It delegates almost all functionality to BasePathManager 
     * @param context RexsterResourceContext supplied by server
     * @return ExtensionResponse to be reformatted by the server
     * @see BasePathManager
     */
	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.POST)
	@ExtensionDescriptor(description = "POST a multipart file containg the 'Stevens' family ontology, with HttpMethod.POST")
	public ExtensionResponse postBasePath (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = "postBasePath(RexsterResourceContext) :\n";
		logger.info(sMETHOD + COMMENT);
		
		BasePathManager manager = new BasePathManager(); 
		return manager.post(context, this);

	}
    
    
    /**
     * This is the REST style handler for DELETE method, operating on the entire graph at once.
     * It delegates almost all functionality to BasePathManager 
     * @param context RexsterResourceContext supplied by server
     * @param refVertex supplied by user on the URL address line
     * @return ExtensionResponse to be reformatted by the server
     * @see BasePathManager
     */
	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.DELETE)
	@ExtensionDescriptor(description = "DELETE all the 'Stevens' family ontology, with HttpMethod.DELETE")
	public ExtensionResponse deleteBasePath (@RexsterContext RexsterResourceContext context
				, @ExtensionRequestParameter(name = "refVertex", description = "Delete all nodes connected to refVertex") String refVertex
			)
	{
		final String sMETHOD = "deleteBasePath(RexsterResourceContext) :\n";
		logger.info(sMETHOD + COMMENT);
		
        ExtensionMethod extMethod = context.getExtensionMethod();
        String msg = "internal error -- ";
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

		
        if (refVertex == null || refVertex.isEmpty()) {
            msg = "the refVertex parameter cannot be empty";
            status = Response.Status.BAD_REQUEST.getStatusCode();
        }

		BasePathManager manager = new BasePathManager(); 
		try {
			return manager.delete(context, this, refVertex);
			
		} catch (ReferenceNodeNotFoundException e) {
            msg += "could not create vertex";
		} catch (MultipleReferenceNodesException e) {
            msg += "can't distinguish unique reference index.";
		}
        return ExtensionResponse.error(msg, null, status, null, generateErrorJson(extMethod.getExtensionApiAsJson()));
	}

    
}
