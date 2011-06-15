package net.justtrade.rest.mowa;

import java.util.HashMap;
import java.util.List;

import com.tinkerpop.rexster.extension.AbstractRexsterExtension;
import com.tinkerpop.rexster.extension.ExtensionResponse;

/**
 * Base class for samples.
 */
public abstract class AbstractSampleExtension extends AbstractRexsterExtension {
	public static final String EXTENSION_NAMESPACE = "mowq";


	abstract public String getExtensionName();

	public String getExtensionNameSpace() {
		return EXTENSION_NAMESPACE;
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
