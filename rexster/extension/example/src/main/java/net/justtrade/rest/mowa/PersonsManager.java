package net.justtrade.rest.mowa;

import java.util.ArrayList;

import net.justtrade.rest.handlers.context.RexsterResourceGETContext;
import net.justtrade.rest.handlers.context.RexsterResourcePOSTContext;
import net.justtrade.rest.handlers.context.RexsterResourcePUTContext;

import com.tinkerpop.rexster.RexsterApplicationGraph;
import com.tinkerpop.rexster.extension.ExtensionResponse;

public class PersonsManager extends MOWaExtension {


	private static final String CLASS_NAME = "\nPersonsManager.";
	public static final String REST_PATH = "persons";

	public ExtensionResponse personsPath (RexsterResourceGETContext context)
	{
		final String sMETHOD = CLASS_NAME + "personsPath(RexsterResourceGETContext) --> ";
		System.out.println(sMETHOD + "Processing GET at " + basePath + "/" + REST_PATH);
		
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();

		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("{\"title\": \"List Persons.\" \"href\": \"" + basePath + "/" + REST_PATH + "\"}");
		return toStringIt(rag.getGraph(), "persons", hateaos);
	}


	public ExtensionResponse personsPath (RexsterResourcePOSTContext context)
	{
		final String sMETHOD = CLASS_NAME + "personsPath(RexsterResourcePOSTContext) --> ";
		System.out.println(sMETHOD + "Processing POST at " + basePath + "/" + REST_PATH);
		
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();

		ArrayList<String> hateaos = new ArrayList<String>();
		hateaos.add("{\"title\": \"Post Persons.\" \"href\": \"" + basePath + "/" + REST_PATH + "\"}");
		return toStringIt(rag.getGraph(), "persons", hateaos);
	}

	public ExtensionResponse personsPath (RexsterResourcePUTContext context)
	{
		final String sMETHOD = CLASS_NAME + "personsPath(RexsterResourcePUTContext) --> ";
		
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();

		ArrayList<String> hateaos = new ArrayList<String>();
		System.out.println(sMETHOD + "Detected Multipart Content! Can be processed only at root level.");

		hateaos.add("\"title\": \"Multipart content received at -- \" \"href\": \"/" + basePath);
		return toStringIt(rag.getGraph(), "persons", hateaos);
	}
}
