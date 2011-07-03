package net.justtrade.rest.mowa;

import java.util.ArrayList;

import net.justtrade.rest.handlers.context.RexsterResourceGETContext;
import net.justtrade.rest.handlers.context.RexsterResourcePOSTContext;
import net.justtrade.rest.handlers.context.RexsterResourcePUTContext;

import com.tinkerpop.rexster.RexsterApplicationGraph;
import com.tinkerpop.rexster.extension.ExtensionResponse;

public class RelationshipsManager extends MOWaExtension {

	private static final String CLASS_NAME = "\nRelationshipsManager.";
	public static final String REST_PATH = "relationships";

	public ExtensionResponse relationshipsPath (RexsterResourceGETContext context)
	{
		final String sMETHOD = CLASS_NAME + "relationshipsPath(RexsterResourceGETContext) --> ";
		System.out.println(sMETHOD + "Processing GET at " + basePath + "/" + REST_PATH);
		
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();

		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("{\"title\": \"List Relationships.\" \"href\": \"" + basePath + "/" + REST_PATH + "\"}");
		return toStringIt(rag.getGraph(), REST_PATH, hateaos);
	}

	public ExtensionResponse relationshipsPath (RexsterResourcePOSTContext context)
	{
		final String sMETHOD = CLASS_NAME + "relationshipsPath(RexsterResourcePOSTContext) --> ";
		System.out.println(sMETHOD + "Processing POST at " + basePath + "/" + REST_PATH);
		
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();

		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("{\"title\": \"Post Relationships.\" \"href\": \"" + basePath + "/" + REST_PATH + "\"}");
		return toStringIt(rag.getGraph(), REST_PATH, hateaos);
	}

	public ExtensionResponse relationshipsPath (RexsterResourcePUTContext context)
	{
		final String sMETHOD = CLASS_NAME + "relationshipsPath(RexsterResourcePUTContext) --> ";
		
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();

		ArrayList<String> hateaos = new ArrayList<String>();
		System.out.println(sMETHOD + "Detected Multipart Content! Can be processed only at root level.");

		hateaos.add("\"title\": \"Multipart content received at -- \" \"href\": \"/" + basePath);
		return toStringIt(rag.getGraph(), REST_PATH, hateaos);
	}

	
	
}
