package net.justtrade.rest.mowa;

import java.util.HashMap;

import groovy.json.JsonOutput;

import javax.ws.rs.core.Response;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.justtrade.rest.handlers.graph.BasePathManager;
import net.justtrade.rest.handlers.graph.CollectionIndexNotFoundException;
import net.justtrade.rest.handlers.graph.ReferenceNodeNotFoundException;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

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
//    public ExtensionResponse getBasePath(@RexsterContext RexsterResourceContext context)
	public ExtensionResponse getBasePath
	(
			  @RexsterContext RexsterResourceContext context
			, @ExtensionRequestParameter(name = "req", description = "Indicate the kind of data required") String req
	)
    {
		final String sMETHOD = "getBasePath(RexsterResourceContext) :\n";
		logger.info(sMETHOD + COMMENT);

		try {
			if ((req==null) || req.equals("")) {
				
				JSONObject jsonRslt = new JSONObject();
				JSONObject persons = new JSONObject();
				JSONObject relationships = new JSONObject();

				persons.put("persons", new JSONObject().put("title", "List available persons.").accumulate("href", basePath + "/persons"));
				relationships.put("relationships", new JSONObject().put("title", "List available relationships.").accumulate("href", basePath + "/relationships"));
				
				jsonRslt.put("paths", persons);
				jsonRslt.accumulate("paths", relationships);
				logger.info("\nJSON : " + JsonOutput.prettyPrint(jsonRslt.toString()));
							
				return ExtensionResponse.ok(jsonRslt);
				
			} else {
				
				JSONObject rslt = new JSONObject();
				
				logger.info(sMETHOD + COMMENT + " req = " + req);
				try {
					rslt.put("req", req);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ExtensionResponse.ok(rslt);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Something wrong", "");

		return ExtensionResponse.ok(map);
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
		
		
	    // assume SLF4J is bound to logback in the current environment
	    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	    // print logback's internal status
	    StatusPrinter.print(lc);
		
		
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
		logger.info(sMETHOD + COMMENT + "Are we at DEBUG level?");
		logger.debug(sMETHOD + "We're at DEBUG level.");
		
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
            msg += "could find no such reference vertex.";
		} catch (CollectionIndexNotFoundException e) {
            msg += "could find no index for this collection.";
		}
        return ExtensionResponse.error(msg, null, status, null, generateErrorJson(extMethod.getExtensionApiAsJson()));
	}
    
}
