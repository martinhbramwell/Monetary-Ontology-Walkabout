package net.justtrade.rest.handlers.context;

import com.tinkerpop.rexster.RexsterResourceContext;

public class ContextTransformer {
	
	public static RexsterResourceGETContext transformContextGET(RexsterResourceContext ctxt) {
		return new RexsterResourceGETContext
			(
					  ctxt.getRexsterApplicationGraph()
					, ctxt.getUriInfo()
					, ctxt.getRequest()
					, ctxt.getRequestObject()
					, ctxt.getExtensionMethod()
			);
	}
	
	public static RexsterResourcePUTContext transformContextPUT(RexsterResourceContext ctxt) {
		return new RexsterResourcePUTContext
			(
					  ctxt.getRexsterApplicationGraph()
					, ctxt.getUriInfo()
					, ctxt.getRequest()
					, ctxt.getRequestObject()
					, ctxt.getExtensionMethod()
			);
	}
	
	public static RexsterResourcePOSTContext transformContextPOST(RexsterResourceContext ctxt) {
		return new RexsterResourcePOSTContext
			(
					  ctxt.getRexsterApplicationGraph()
					, ctxt.getUriInfo()
					, ctxt.getRequest()
					, ctxt.getRequestObject()
					, ctxt.getExtensionMethod()
			);
	}
	
	public static RexsterResourceDELETEContext transformContextDELETE(RexsterResourceContext ctxt) {
		return new RexsterResourceDELETEContext
			(
					  ctxt.getRexsterApplicationGraph()
					, ctxt.getUriInfo()
					, ctxt.getRequest()
					, ctxt.getRequestObject()
					, ctxt.getExtensionMethod()
			);
	}

}
