package net.justtrade.rest.mowa;

import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.*;

import javax.servlet.http.HttpServletRequest;

import net.justtrade.rest.handlers.context.ContextTransformer;
import net.justtrade.rest.util.HTTPMethod;
import net.justtrade.rest.util.UnsupportedMethodException;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * An extension that showcases the methods available to those who wish to extend Rexster.
 *
 * This sample focuses on path-level extensions.  Path-level extensions add the extension
 * to the root of the specified ExtensionPoint followed by the value specified in the
 * "path" parameter of the @ExtensionDefinition.  It is important to ensure that paths
 * remain unique.  In the event of a collision, Rexster will serve the request to the
 * first match it finds.
 */
@ExtensionNaming(name = MOWaExtensionX.EXTENSION_NAME, namespace = MOWaRootExtension.EXTENSION_NAMESPACE)
public class MOWaExtensionX extends MOWaExtensionAbstract {
	
    public static final String EXTENSION_NAME = "stevens";
    public String getExtensionName() {return EXTENSION_NAME;}	
	public static final String basePath = EXTENSION_NAMESPACE + "/" + EXTENSION_NAME;
	private static final String CLASS_NAME = "\n" + "MOWaExtensionX" + ".";


	/**
	 * By adding the @RexsterContext attribute to the "graph" parameter, the graph requested gets
	 * automatically injected into the extension.  Therefore, when the following URI is requested:
	 * 
	 * the graph called "graphname" will be pushed into this method.
	 */

	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "persons")
	@ExtensionDescriptor(description = "CRUD for persons.")
	public ExtensionResponse getPersons (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "getPersons(RexsterResourceContext) --> ";
		
		System.out.println(sMETHOD + "Starting ");

		HttpServletRequest httpRequest = context.getRequest();
		String httpMethod = httpRequest.getMethod();
		
		PersonsManager manager = new PersonsManager(); 
		if (ServletFileUpload.isMultipartContent(httpRequest)) {
			return manager.personsPath(ContextTransformer.transformContextPUT(context));
		} else {
			switch (HTTPMethod.convert(httpMethod)) {
			case HTTPMethod.POST :
				return manager.personsPath(ContextTransformer.transformContextPOST(context));
			case HTTPMethod.GET :
				return manager.personsPath(ContextTransformer.transformContextGET(context));
			default:
				return ExtensionResponse.error(new UnsupportedMethodException(httpMethod));
			}
		}
	}

	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "relationships")
	@ExtensionDescriptor(description = "CRUD for relationships.")
	public ExtensionResponse getRelationships
	(@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "getRelationships() --> ";
		System.out.println(sMETHOD + "RexsterResourceContext :: getRequestObject " + context.getRequestObject().toString());


		HttpServletRequest httpRequest = context.getRequest();
		String httpMethod = httpRequest.getMethod();
		
		RelationshipsManager manager = new RelationshipsManager(); 
		if (ServletFileUpload.isMultipartContent(httpRequest)) {
			return manager.relationshipsPath(ContextTransformer.transformContextPUT(context));
		} else {
			switch (HTTPMethod.convert(httpMethod)) {
			case HTTPMethod.POST :
				return manager.relationshipsPath(ContextTransformer.transformContextPOST(context));
			case HTTPMethod.GET :
				return manager.relationshipsPath(ContextTransformer.transformContextGET(context));
			default:
				return ExtensionResponse.error(new UnsupportedMethodException(httpMethod));
			}
		}
		
	}
    /**
     * This method helps the root methods by wrapping the output of the toString of the graph element
     * in JSON to be returned in the ExtensionResponse.  ExtensionResponse has numerous helper methods
     * to make it easy to build the response object.
     *
     * Outputted JSON (if the object is a graph) will look like this:
     *
     * {"output":"tinkergraph[vertices:6 edges:6]","version":"0.3-SNAPSHOT","queryTime":38.02189}
     *
     * Note the "version" and "queryTime" properties within the JSON.  Rexster will attempt to automatically
     * add these items when it understands the output to be JSON.  It is possible to override this default
     * behavior by setting the tryIncludeRexsterAttributes on the @Extension definition to false.
     */
    
}
