package net.justtrade.rest.handlers.graph;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.rexster.extension.AbstractRexsterExtension;
import com.tinkerpop.rexster.extension.ExtensionResponse;

/**
 * This class merely adapts AbstractRexsterExtension
 * @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
 */
public abstract class ExtensionAbstract extends AbstractRexsterExtension {

	private static final Logger logger = LoggerFactory.getLogger(ExtensionAbstract.class);
    
	
    /**
     * Enforces provision of the project name space by any subclass
     * @return the root name of URLs accessing this extension
     */
    abstract public String getExtensionNameSpace();
    
    /**
     * Enforces provision of the project name by any subclass
     * @return a secondary root name of URLs accessing this extension
     */
    abstract public String getExtensionName();

    /**
     * Enforces provision of the project name space and name by any subclass
     * @return the root name of URLs accessing this extension merged with the secondary root name.
     */
    abstract public String getBasePath();
    
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
    
    
	public ExtensionResponse toStringIt(Object obj, String path, List<String> hateaos) 
	{
		if (logger.isDebugEnabled()) {
		logger.debug("output :: " + obj.toString());
		logger.debug("workCameFrom :: " + path);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("output", obj.toString());
		map.put("workCameFrom", path);
		map.put("options", hateaos);

		return ExtensionResponse.ok(map);

	}


}
