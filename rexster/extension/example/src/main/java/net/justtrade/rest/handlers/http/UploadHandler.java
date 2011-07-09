package net.justtrade.rest.handlers.http;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionResponse;

import net.justtrade.rest.util.FileWriteException;


public class UploadHandler {

	public static final String CLASS_NAME = "\n" + "UploadHandler" + ".";
	
	public static final String GRAPH_ARCHIVE = "location-of-graph-archive";
	public static final String TEMP_FILES = "location-of-temp-files";

	public static final String EXTENSION_NAME_SPACE = "extension-name-space";
	public static final String EXTENSION_NAME = "extension-name";
	
	protected JSONObject json = null;

	/*
	 * This method leverages the capabilities of the the Apache project org.apache.commons.fileupload
	 * to load files with very little coding necessary.  They are first streamed directly to the
	 * "location-of-temp-files", and only processed individually, and stored permanently  at 
	 * "location-of-graph-archive".  Both of those locations are user-defined in rexster.xml 
	 * 
	 */
	public ExtensionResponse handleUpload(Map<String, String> _names, RexsterResourceContext _context)
	{
		final String sMETHOD = CLASS_NAME + "handleUpload() --> ";
		
		HttpServletRequest request = _context.getRequest();
		
		json = new JSONObject();
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request); 
		try {
			// Distinguish multi-part from single file upload request
			if (isMultipart)
			{
				return MultiPartUploadHandler.handleUpload(_names, _context, json);
				
			} else {
				
				return FileUploadHandler.handleUpload(_names, _context, json);
				
			}
		} catch (JSONException jsonex) {
			System.out.println(sMETHOD + "* * * Invalid JSON * * * \n" + jsonex.getLocalizedMessage());
		} catch (FileWriteException ioex) {
			System.out.println(sMETHOD + "* * * File Write Failure * * * \n" + ioex.getLocalizedMessage());
			System.out.println(sMETHOD + "Does the directory " + _names.get(TEMP_FILES) + " exist?\n" + ioex.getLocalizedMessage());
			System.out.println(sMETHOD + "Does the directory " + _names.get(GRAPH_ARCHIVE) + " exist?\n" + ioex.getLocalizedMessage());

		} catch (FileUploadException fuex) {
			System.out.println(sMETHOD + "* * * File Upload Failure * * * \n" + fuex.getLocalizedMessage());
		} catch (Exception ex) {
			System.out.println(sMETHOD + "* * * Input/output problem with upload file * * * \n" + ex.getLocalizedMessage());
		}

		
		return ExtensionResponse.ok(json);
		
	}


}
