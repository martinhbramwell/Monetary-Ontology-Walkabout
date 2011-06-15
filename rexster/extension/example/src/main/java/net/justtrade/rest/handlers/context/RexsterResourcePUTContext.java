package net.justtrade.rest.handlers.context;

import com.tinkerpop.rexster.RexsterApplicationGraph;
import com.tinkerpop.rexster.RexsterResourceContext;

import com.tinkerpop.rexster.extension.ExtensionMethod;
import org.codehaus.jettison.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;

public class RexsterResourcePUTContext extends RexsterResourceContext {

	public RexsterResourcePUTContext(RexsterApplicationGraph rag,
			UriInfo uriInfo, HttpServletRequest request,
			JSONObject requestObject, ExtensionMethod extensionMethod) {
		super(rag, uriInfo, request, requestObject, extensionMethod);
	}

}
