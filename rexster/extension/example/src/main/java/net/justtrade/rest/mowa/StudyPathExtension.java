package net.justtrade.rest.mowa;

import com.tinkerpop.rexster.RexsterApplicationGraph;
import com.tinkerpop.rexster.RexsterResourceContext;

import com.tinkerpop.rexster.extension.ExtensionDefinition;
import com.tinkerpop.rexster.extension.ExtensionDescriptor;
import com.tinkerpop.rexster.extension.ExtensionMethod;
import com.tinkerpop.rexster.extension.ExtensionNaming;
import com.tinkerpop.rexster.extension.ExtensionPoint;
import com.tinkerpop.rexster.extension.ExtensionResponse;
import com.tinkerpop.rexster.extension.RexsterContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.justtrade.rest.handlers.http.MultipartUploadHandler;
import net.justtrade.rest.util.HTTPMethod;
import net.justtrade.rest.util.UnsupportedMethodException;

import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONException;

import org.apache.commons.fileupload.servlet.ServletFileUpload;


import javax.ws.rs.core.Response;

/**
 * This is an example of how to load and work with a Web Ontology Language (OWL) file.
 * 
 * It shows the following capabilities :
 *  - how an extension can receive multi-part file uploads.
 *  - how Blueprints permits database work while shielding you from needing to know database specific commands.
 *  - like the SimplePathExtension, it shows various uses of URL paths to achieve various service goals.
 *  
 */

@ExtensionNaming(name = StudyPathExtension.EXTENSION_NAME, namespace = AbstractStudyExtension.EXTENSION_NAMESPACE)
public class StudyPathExtension extends AbstractStudyExtension {

	public static final String EXTENSION_NAME = "stevens";
    public String getExtensionName() {return EXTENSION_NAME;}	
	public static final String basePath = EXTENSION_NAMESPACE + "/" + EXTENSION_NAME;
	public static final String CLASS_NAME = "\nStudyPathExtension.";

	/**
	 * This is the method designated to handle the root of the Ray Stevens Family Ontology.
	 * 
	 * It returns a list of available options.
	 * It is also able to receive one or more OWL files as HTTP Multipart Content.
	 *  
	 */

	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "")
	@ExtensionDescriptor(description = "returns the options available for the 'Stevens' family ontology.")
	public ExtensionResponse manageBasePath (
			@RexsterContext RexsterResourceContext context,
			@RexsterContext RexsterApplicationGraph graph
	)
	{
		final String sMETHOD = CLASS_NAME + "manageBasePath() --> ";
		System.out.println(sMETHOD + "Base path :: " + basePath);

		HttpServletRequest httpRequest = context.getRequest();
		String httpMethod = httpRequest.getMethod();

		if (ServletFileUpload.isMultipartContent(httpRequest)) {
			return basePath_PUT(context, graph);
		} else {
			switch (HTTPMethod.convert(httpMethod)) {
			case HTTPMethod.POST :
				return basePath_POST(context, graph);
			case HTTPMethod.GET :
				return basePath_GET(context, graph);
			default:
				return ExtensionResponse.error(new UnsupportedMethodException(httpMethod));
			}
		}
	}

	public ExtensionResponse basePath_PUT (
			RexsterResourceContext context,
			RexsterApplicationGraph graph
	)
	{
		final String sMETHOD = CLASS_NAME + "basePath_PUT(RexsterResourceContext, RexsterApplicationGraph) --> ";
		
		System.out.println(sMETHOD + "ProcessIng 'Multipart Content'!");

		HttpServletRequest httpRequest = context.getRequest();
		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("\"title\": \"List available persons.\" \"href\": \"" + basePath + "/persons\"");
		hateaos.add("\"title\": \"List available relationships.\" \"href\": \"" + basePath + "/relationships\"");
		hateaos.add("\"title\": \"Multipart content received at -- \" \"href\": \"/" + basePath);

		MultipartUploadHandler handler = new MultipartUploadHandler();
		return handler.handleUpload(this, httpRequest, context);
	}

	public ExtensionResponse basePath_POST (
			RexsterResourceContext context,
			RexsterApplicationGraph graph
	)
	{
		final String sMETHOD = CLASS_NAME + "basePath_POST(RexsterResourceContext, RexsterApplicationGraph) --> ";
		final String REST_PATH = "";
		
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();
		
		System.out.println(sMETHOD + "RexsterResourceContext :: getRequestObject " + context.getRequestObject().toString());
		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("{\"Added Person :\" \"href\": \"" + basePath + "/" + REST_PATH + "\"}");
		return toStringIt(rag.getGraph(), "persons", hateaos);
		
	}

	public ExtensionResponse basePath_GET (
			RexsterResourceContext context,
			RexsterApplicationGraph graph
	)
	{
		final String sMETHOD = CLASS_NAME + "basePath_GET(RexsterResourceContext, RexsterApplicationGraph) --> ";
		final String REST_PATH = "";
		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("\"title\": \"List available persons.\" \"href\": \"" + basePath + "/persons\"");
		hateaos.add("\"title\": \"List available relationships.\" \"href\": \"" + basePath + "/relationships\"");
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();

		System.out.println(sMETHOD + "RexsterResourceContext :: getRequestObject " + context.getRequestObject().toString());
		return toStringIt(rag.getGraph(), "persons", hateaos);
		
	}
	
	
	
	
	/**
	 * By adding the @RexsterContext attribute to the "graph" parameter, the graph requested gets
	 * automatically injected into the extension.  Therefore, when the following URI is requested:
	 * 
	 * curl -svF "CarAds=@.\src\main\resources\data\family.swrl.owl"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
	 * curl -svF "CarAds=@.\src\main\resources\data\TheStevens.owl"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
	 *
	 * the graph called "graphname" will be pushed into this method.
	 */

	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "persons")
	@ExtensionDescriptor(description = "CRUD for persons.")
	public ExtensionResponse managePersons
	(
			@RexsterContext RexsterResourceContext context
	)
	{
		final String sMETHOD = CLASS_NAME + "managePersons(RexsterResourceContext) --> ";
		final String REST_PATH = "persons";
		
		System.out.println(sMETHOD + "Starting ");

		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();

		ArrayList<String> hateaos = new ArrayList<String>();
		HttpServletRequest httpRequest = context.getRequest();

		if (ServletFileUpload.isMultipartContent(httpRequest)) {

			System.out.println(sMETHOD + "Detected 'Multipart Content'! Can be processed only at root level.");

			hateaos.add("\"title\": \"Multipart content received at -- \" \"href\": \"/" + basePath);

			MultipartUploadHandler handler = new MultipartUploadHandler();
			return handler.handleUpload(this, httpRequest, context);


		} else {
			String httpMethod = httpRequest.getMethod();

			switch (HTTPMethod.convert(httpMethod)) {

			case HTTPMethod.POST :
				System.out.println(sMETHOD + "RexsterResourceContext :: getRequestObject " + context.getRequestObject().toString());
				hateaos.add("{\"Added Person :\" \"href\": \"" + basePath + "/" + REST_PATH + "\"}");
				return toStringIt(rag.getGraph(), "persons", hateaos);

			case HTTPMethod.GET :
				hateaos.add("{\"title\": \"List Persons.\" \"href\": \"" + basePath + "/" + REST_PATH + "\"}");
				return toStringIt(rag.getGraph(), "persons", hateaos);

			default:

				return ExtensionResponse.error(new UnsupportedMethodException(httpMethod));
			}
		}
	}



	@ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "relationships")
	@ExtensionDescriptor(description = "CRUD for relationships.")
	public ExtensionResponse doOtherWorkOnGraph
	(@RexsterContext RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "doOtherWorkOnGraph() --> ";
		final String REST_PATH = "relationships";

		HttpServletRequest rqst = context.getRequest();

		System.out.println(sMETHOD + "RexsterResourceContext :: getRequestObject " + context.getRequestObject().toString());


		String strMthd = rqst.getMethod().toString();
		int iMthd = HTTPMethod.convert(strMthd);
		System.out.println("\n\nHttpServletRequest ::  (" + iMthd + ") " + strMthd);


		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("{\"title\": \"List " + REST_PATH + ".\" \"href\": \"" + basePath + "/" + REST_PATH + "\"}");

		return toStringIt(context.getRexsterApplicationGraph().getGraph(), REST_PATH, hateaos);

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
	private ExtensionResponse toStringIt(Object obj, String path, List<String> hateaos) {

		System.out.println("\n\nAbstractStudyExtension.EXTENSION_NAMESPACE :: " + AbstractStudyExtension.EXTENSION_NAMESPACE);
		System.out.println("output :: " + obj.toString());
		System.out.println("work-came-from :: " + path);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("output", obj.toString());
		map.put("work-came-from", path);
		map.put("options", hateaos);

		return ExtensionResponse.ok(map);

	}


}