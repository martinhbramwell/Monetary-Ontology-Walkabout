package net.justtrade.rest.mowa;

import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionDefinition;
import com.tinkerpop.rexster.extension.ExtensionDescriptor;
import com.tinkerpop.rexster.extension.ExtensionNaming;
import com.tinkerpop.rexster.extension.ExtensionPoint;
import com.tinkerpop.rexster.extension.ExtensionResponse;
import com.tinkerpop.rexster.extension.HttpMethod;
import com.tinkerpop.rexster.extension.RexsterContext;

@ExtensionNaming(name = MOWaRootExtension.EXTENSION_NAME, namespace = MOWaExtensionAbstract.EXTENSION_NAMESPACE)
public class MOWaExtensionY extends MOWaExtensionAbstract {
	
	public static final String basePath = EXTENSION_NAMESPACE + "/" + MOWaRootExtension.EXTENSION_NAME;
	private static final String CLASS_NAME = "\n" + "MOWaExtensionY" + ".";

	@Override
	public String getExtensionName() {return MOWaRootExtension.EXTENSION_NAME;}

	
	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "persons", method = HttpMethod.GET)
	@ExtensionDescriptor(description = "CRUD for persons.")
	public ExtensionResponse getPersons (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "getPersons(RexsterResourceContext) --> ";
		final String METHOD_PATH = "persons";
		System.out.println(sMETHOD + "Starting ...");
		
		return this.toStringSimple(this, basePath + "/" + METHOD_PATH);
	}
    
	
	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "relationships", method = HttpMethod.GET)
	@ExtensionDescriptor(description = "CRUD for relationships.")
	public ExtensionResponse getRelationships (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "getRelationships(RexsterResourceContext) --> ";
		final String METHOD_PATH = "relationships";
		System.out.println(sMETHOD + "Starting ...");
		
		return this.toStringSimple(this, basePath + "/" + METHOD_PATH);
	}
	
	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, method = HttpMethod.GET)
	@ExtensionDescriptor(description = "Returns the options available for the 'Stevens' family ontology, with HttpMethod.GET.")
	public ExtensionResponse getBasePath (@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "getBasePath() --> ";
		System.out.println(sMETHOD + "Base path :: " + basePath);
		
		return this.toStringSimple(this, "GET from " + basePath);

	}
    
    
	
    
}
