package net.justtrade.rest.mowa;


import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionNaming;
import com.tinkerpop.rexster.extension.ExtensionDefinition;
import com.tinkerpop.rexster.extension.ExtensionPoint;
import com.tinkerpop.rexster.extension.HttpMethod;
import com.tinkerpop.rexster.extension.ExtensionDescriptor;
import com.tinkerpop.rexster.extension.ExtensionResponse;
import com.tinkerpop.rexster.extension.RexsterContext;

/**
 * An extension that showcases the methods available to those who wish to extend Rexster.
 *
 * This sample focuses on path-level extensions.  Path-level extensions add the extension
 * to the root of the specified ExtensionPoint followed by the value specified in the
 * "path" parameter of the @ExtensionDefinition.  It is important to ensure that paths
 * remain unique.  In the event of a collision, Rexster will serve the request to the
 * first match it finds.
 */
@ExtensionNaming(name = MOWaExtension.EXTENSION_NAME, namespace = MOWaExtensionAbstract.EXTENSION_NAMESPACE)
public class MOWaExtension extends MOWaExtensionAbstract {
    public static final String EXTENSION_NAME = "stevens";
    
	public static final String basePath = EXTENSION_NAMESPACE + "/" + EXTENSION_NAME;
	private static final String CLASS_NAME = "\n" + "MOWaRootExtension" + ".";

	@Override
	public String getExtensionName() {return MOWaRootExtension.EXTENSION_NAME;}

    /**
     * By adding the @RexsterContext attribute to the "graph" parameter, the graph requested gets
     * automatically injected into the extension.  Therefore, when the following URI is requested:
     *
     * http://localhost:8182/tinkergraph/tp/simple-path/persons
     *
     * the graph called "graphname" will be pushed into this method.
     */
    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "persons", method = HttpMethod.GET )
    @ExtensionDescriptor(description = "This gives access to the 'persons' path, with HttpMethod.GET.")
    public ExtensionResponse doGetSome(@RexsterContext Graph graph) {
        return toStringIt(graph, "GET persons");
    }

    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "persons", method = HttpMethod.POST )
    @ExtensionDescriptor(description = "This gives access to the 'persons' path, with HttpMethod.POST.")
    public ExtensionResponse doPostSome(@RexsterContext Graph graph) {
        return toStringIt(graph, "POST persons");
    }

     /**
     * By adding the @RexsterContext attribute to the "graph" parameter, the graph requested gets
     * automatically injected into the extension.  Therefore, when the following URI is requested:
     *
     * http://localhost:8182/tinkergraph/tp/simple-path/relationships
     *
     * the graph called "graphname" will be pushed into this method.
     */
    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "relationships", method = HttpMethod.GET)
    @ExtensionDescriptor(description = "This gives access to the 'relationships' path, with HttpMethod.GET.")
    public ExtensionResponse doGetOther(@RexsterContext Graph graph){
        return toStringIt(graph, "GET relationships");
    }

    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "relationships", method = HttpMethod.POST)
    @ExtensionDescriptor(description = "This gives access to the 'relationships' path, with HttpMethod.POST.")
    public ExtensionResponse doPostOther(@RexsterContext Graph graph){
        return toStringIt(graph, "POST relationships");
    }

    /**
     * By adding the @RexsterContext attribute to the "graph" parameter, the graph requested gets
     * automatically injected into the extension.  Therefore, when the following URI is requested:
     *
     * http://localhost:8182/tinkergraph/tp/simple-path/persons
     *
     * the graph called "graphname" will be pushed into this method.
     */
/*    
    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.GET)
	@ExtensionDescriptor(description = "Returns the options available for the 'Stevens' family ontology, with HttpMethod.GET.")
    public ExtensionResponse getBasePath(@RexsterContext Graph graph) {
		final String sMETHOD = CLASS_NAME + "getBasePath(Graph) --> ";
		System.out.println(sMETHOD + "Base path :: " + basePath);
		
        return toStringIt(graph, "GET graph's root");
    }
*/

//    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.POST)
//	@ExtensionDescriptor(description = "Returns the options available for the 'Stevens' family ontology, with HttpMethod.POST.")
//    public ExtensionResponse doPostRootWork(@RexsterContext Graph graph) {
//        return toStringIt(graph, "POST root work");
//    }
/*
	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.POST)
	@ExtensionDescriptor(description = "POST a multipart file containg the 'Stevens' family ontology.")
	public ExtensionResponse postBasePath (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "postBasePath(RexsterResourceContext) --> ";
		System.out.println(sMETHOD + "Base path :: " + basePath);
		
		BasePathManager manager = new BasePathManager(); 
		return manager.post(context);

	}
*/    
    
//  @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.PUT)
//	@ExtensionDescriptor(description = "Returns the options available for the 'Stevens' family ontology, with HttpMethod.PUT.")
//  public ExtensionResponse doPutRootWork(@RexsterContext Graph graph) {
//      return toStringIt(graph, "PUT root work");
//  }
/*
	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.PUT)
	@ExtensionDescriptor(description = "PUT a single file towards the 'Stevens' family ontology.")
	public ExtensionResponse putBasePath (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "putBasePath(RexsterResourceContext) --> ";
		System.out.println(sMETHOD + "Base path :: " + basePath);
		
		BasePathManager manager = new BasePathManager(); 
		return manager.put(context);

	}
*/
    


    
}
