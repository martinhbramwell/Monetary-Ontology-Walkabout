package net.justtrade.rest.mowa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.tinkerpop.rexster.RexsterApplicationGraph;
import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionConfiguration;
import com.tinkerpop.rexster.extension.ExtensionResponse;

import net.justtrade.rest.handlers.http.UploadHandler;;

public class BasePathManager extends MOWaExtension {

	public static final String CLASS_NAME = "\nBasePathManager.";
	
	private static final String basePath = MOWaExtension.basePath;
	
	private Map<String, String> names;
	
	public ExtensionResponse put (RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "basePath_PUT(RexsterResourcePUTContext) --> ";
		
		System.out.println(sMETHOD + "ProcessIng Multipart Content!");
		
		names = initConfig(context);
		if (names == null) {
			String message = "Found no configuration to load using : <namespace>" + getExtensionNameSpace() + "</namespace> and <name>" + getExtensionName() + "</name>.";
			System.out.println(sMETHOD + message);
			return ExtensionResponse.error(message);
			
		} else {
			
			UploadHandler handler = new UploadHandler();
			return handler.handleUpload(names, context);
		}
	}
		

	public ExtensionResponse delete(RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "post(RexsterResourceContext) --> ";
		
		names = initConfig(context);
		if (names == null) {
			String message = "Found no configuration to load using : <namespace>" + getExtensionNameSpace() + "</namespace> and <name>" + getExtensionName() + "</name>.";
			System.out.println(sMETHOD + message);
			return ExtensionResponse.error(message);
			
		} else {
			
			JSONObject jsonRslt = new JSONObject();
			try {
				jsonRslt.put("behaviour", "DELETE");
				System.out.println(sMETHOD + "\n\n\n\nReady to delete : ");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return ExtensionResponse.ok(jsonRslt);
		}
	}

	public ExtensionResponse post(RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "post(RexsterResourceContext) --> ";
		
		names = initConfig(context);
		if (names == null) {
			String message = "Found no configuration to load using : <namespace>" + getExtensionNameSpace() + "</namespace> and <name>" + getExtensionName() + "</name>.";
			System.out.println(sMETHOD + message);
			return ExtensionResponse.error(message);
			
		} else {
			
			UploadHandler handler = new UploadHandler();
			return handler.handleUpload(names, context);
		}
	}

	public ExtensionResponse basePath (RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "basePath(RexsterResourceGETContext) --> ";
		final String REST_PATH = "";
		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("\"title\": \"List available persons.\" \"href\": \"" + basePath + "/persons\"" + REST_PATH);
		hateaos.add("\"title\": \"List available relationships.\" \"href\": \"" + basePath + "/relationships\"" + REST_PATH);
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();

		System.out.println(sMETHOD + "RexsterResourceContext :: getRequestObject " + context.getRequestObject().toString());
		return toStringIt(rag.getGraph(), REST_PATH, hateaos);
	}
	
	private Map<String, String> initConfig (RexsterResourceContext _context) {
		if (_context == null) return null;
		
		RexsterApplicationGraph rag = _context.getRexsterApplicationGraph();
		if (rag == null) return null;
		
		ExtensionConfiguration configuration = rag.findExtensionConfiguration(getExtensionNameSpace(), getExtensionName());
		if (configuration == null) return null;
		
		Map<String, String> cfg = configuration.tryGetMapFromConfiguration();
		if (cfg == null) return null;
		
		Map<String, String> names = new HashMap<String, String>();
		names.put(UploadHandler.TEMP_FILES, cfg.get(UploadHandler.TEMP_FILES));
		names.put(UploadHandler.GRAPH_ARCHIVE, cfg.get(UploadHandler.GRAPH_ARCHIVE));
		names.put(UploadHandler.EXTENSION_NAME_SPACE, getExtensionNameSpace());
		names.put(UploadHandler.EXTENSION_NAME, getExtensionName());
		
		return names;
	}

}
