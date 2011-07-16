package net.justtrade.rest.mowa;

import net.justtrade.rest.handlers.graph.ExtensionAbstract;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * This class simple exposes the project namespace for now, but it's available to 
 * hold global functionality for the entire tree of possible URL paths.
 * 
 * @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
 */
public abstract class MOWaExtensionAbstract extends ExtensionAbstract {

//	private static final Logger logger = LoggerFactory.getLogger(MOWaExtensionAbstract.class);
	
    public static final String EXTENSION_NAMESPACE = "mowa";
    
    public String getExtensionNameSpace() {
    	return EXTENSION_NAMESPACE;
    }


}
