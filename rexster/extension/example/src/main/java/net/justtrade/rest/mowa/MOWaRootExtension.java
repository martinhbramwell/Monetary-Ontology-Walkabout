package net.justtrade.rest.mowa;

import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionNaming;
import com.tinkerpop.rexster.extension.ExtensionDefinition;
import com.tinkerpop.rexster.extension.ExtensionDescriptor;
import com.tinkerpop.rexster.extension.HttpMethod;

import com.tinkerpop.rexster.extension.ExtensionPoint;
import com.tinkerpop.rexster.extension.RexsterContext;
import com.tinkerpop.rexster.extension.ExtensionResponse;


/**
 * An extension that showcases the methods available to those who wish to extend Rexster.
 *
 * This sample focuses on path-level extensions.  Path-level extensions add the extension
 * to the root of the specified ExtensionPoint followed by the value specified in the
 * "path" parameter of the @ExtensionDefinition.  It is important to ensure that paths
 * remain unique.  In the event of a collision, Rexster will serve the request to the
 * first match it finds.
 */
@ExtensionNaming(name = MOWaRootExtension.EXTENSION_NAME, namespace = MOWaExtensionAbstract.EXTENSION_NAMESPACE)
public class MOWaRootExtension extends MOWaExtensionAbstract {
	
    public static final String EXTENSION_NAME = "stevens";
    public String getExtensionName() {return EXTENSION_NAME;}	
	public static final String basePath = EXTENSION_NAMESPACE + "/" + EXTENSION_NAME;
	private static final String CLASS_NAME = "\n" + "MOWaRootExtension" + ".";


	/**
	 * This is the method designated to handle the root of the Ray Stevens Family Ontology.
	 * 
	 * It returns a list of available options.
	 * It is also able to receive one or more OWL files as HTTP Multipart Content.
	 * 
 	 * curl -svF "CarAds=@.\src\main\resources\data\family.swrl.owl"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
	 * curl -svF "CarAds=@.\src\main\resources\data\TheStevens.owl"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
	 * , method = HttpMethod.GET
	 */

	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.GET)
	@ExtensionDescriptor(description = "Returns the options available for the 'Stevens' family ontology, with HttpMethod.GET.")
	public ExtensionResponse getBasePath (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "getBasePath() --> ";
		System.out.println(sMETHOD + "Base path :: " + basePath);
		
		return this.toStringSimple(this, basePath);

//		HttpServletRequest httpRequest = context.getRequest();
//		String httpMethod = httpRequest.getMethod();
//		
//		BasePathManager manager = new BasePathManager(); 
//		if (ServletFileUpload.isMultipartContent(httpRequest)) {
//			return manager.basePath(ContextTransformer.transformContextPUT(context));
//		} else {
//			switch (HTTPMethod.convert(httpMethod)) {
//			case HTTPMethod.POST :
//				return manager.basePath(ContextTransformer.transformContextPOST(context));
//			case HTTPMethod.GET :
//				return manager.basePath(ContextTransformer.transformContextGET(context));
//			default:
//				return ExtensionResponse.error(new UnsupportedMethodException(httpMethod));
//			}
//		}
	}
    
    
	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.PUT)
	@ExtensionDescriptor(description = "PUT a single file towards the 'Stevens' family ontology.")
	public ExtensionResponse putBasePath (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "putBasePath() --> ";
		System.out.println(sMETHOD + "Base path :: " + basePath);
		
		BasePathManager manager = new BasePathManager(); 
		return manager.put(context);

	}

    
	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.POST)
	@ExtensionDescriptor(description = "POST a multipart file containg the 'Stevens' family ontology.")
	public ExtensionResponse postBasePath (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "postBasePath() --> ";
		System.out.println(sMETHOD + "Base path :: " + basePath);
		
		BasePathManager manager = new BasePathManager(); 
		return manager.post(context);

	}
    
    
}