package net.justtrade.rest.mowa;

import com.tinkerpop.rexster.extension.AbstractRexsterExtension;

/**
 * Base class for samples.
 */
public abstract class AbstractStudyExtension extends AbstractRexsterExtension {
    public static final String EXTENSION_NAMESPACE = "mowa";
    
    abstract public String getExtensionName();
    
    public String getExtensionNameSpace() {
    	return EXTENSION_NAMESPACE;
    }
}
