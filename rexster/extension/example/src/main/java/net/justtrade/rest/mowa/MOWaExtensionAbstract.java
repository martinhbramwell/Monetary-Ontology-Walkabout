package net.justtrade.rest.mowa;

import java.util.HashMap;
import java.util.List;

import com.tinkerpop.rexster.extension.AbstractRexsterExtension;
import com.tinkerpop.rexster.extension.ExtensionResponse;

/**
 * Base class for samples.
 */
public abstract class MOWaExtensionAbstract extends AbstractRexsterExtension {
    public static final String EXTENSION_NAMESPACE = "mowa";
    
    abstract public String getExtensionName();
    
    public String getExtensionNameSpace() {
    	return EXTENSION_NAMESPACE;
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
    protected ExtensionResponse toStringIt(Object obj, String path) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("output", obj.toString());
        map.put("workCameFrom", path);
        return ExtensionResponse.ok(map);
    }
    
    
	protected ExtensionResponse toStringSimple(Object obj, String path) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("output", obj.toString());
		map.put("workCameFrom", path);
		return ExtensionResponse.ok(map);
	}

	protected ExtensionResponse toStringIt(Object obj, String path, List<String> hateaos) {

		System.out.println("\n\nAbstractStudyExtension.EXTENSION_NAMESPACE :: " + MOWaRootExtension.EXTENSION_NAMESPACE);
		System.out.println("output :: " + obj.toString());
		System.out.println("workCameFrom :: " + path);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("output", obj.toString());
		map.put("workCameFrom", path);
		map.put("options", hateaos);

		return ExtensionResponse.ok(map);

	}


}
