package net.justtrade.rest.handlers.http;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionResponse;

import net.justtrade.rest.util.FileWriteException;
import net.justtrade.rest.util.JSONHelper;


/**
* This a simple upload manager that delegates calls to the correct handler, while keeping some global data needed by any upload type.
* 
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class UploadHandler {

	private static final Logger logger = LoggerFactory.getLogger(UploadHandler.class);
	
	public static final String GRAPH_ARCHIVE = "location-of-graph-archive";
	public static final String TEMP_FILES = "location-of-temp-files";

	public static final String EXTENSION_NAME_SPACE = "extension-name-space";
	public static final String EXTENSION_NAME = "extension-name";
	
	protected JSONObject json = null;

	/**
	 * This method multiplexes an incoming file PUT or POST onto the correct handler.
	 *  
	 * @param _names
	 * @param _context
	 */
	public ExtensionResponse handleUpload(Map<String, String> _names, RexsterResourceContext _context)
	{
		final String sMETHOD = "handleUpload() --> ";
		String msg = "";
		
		HttpServletRequest request = _context.getRequest();
		
		json = new JSONObject();
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request); 
		try {
			// Distinguish multi-part from single file upload request
			if (isMultipart)
			{
				json.put("uploadType", "multipart");
				return MultiPartUploadHandler.handleUpload(_names, _context, json);
				
			} else {
				
				json.put("uploadType", "single");
				return FileUploadHandler.handleUpload(_names, _context, json);
				
			}
		} catch (JSONException jsonex) {
			msg = "* * * Invalid JSON * * * \n" + jsonex.getLocalizedMessage();
			logger.error(sMETHOD + msg);
			return ExtensionResponse.error(jsonex, JSONHelper.put(json, "failure", msg));
			
		} catch (FileWriteException ioex) {
			msg = "* * * File Write Failure * * * \n" + ioex.getLocalizedMessage();
			logger.error(sMETHOD + msg);
			logger.error(sMETHOD + "Does the directory " + _names.get(TEMP_FILES) + " exist?\n" + ioex.getLocalizedMessage());
			logger.error(sMETHOD + "Does the directory " + _names.get(GRAPH_ARCHIVE) + " exist?\n" + ioex.getLocalizedMessage());
			logger.error(sMETHOD + msg);
			return ExtensionResponse.error(ioex, JSONHelper.put(json, "failure", msg));

		} catch (FileUploadException fuex) {
			msg = "* * * File Upload Failure * * * \n" + fuex.getLocalizedMessage();
			logger.error(sMETHOD + msg);
			return ExtensionResponse.error(fuex, JSONHelper.put(json, "failure", msg));
			
		} catch (Exception ex) {
			ex.printStackTrace();
			msg = "* * * Input/output problem with upload file * * * \n" + ex.getLocalizedMessage();
			logger.error(sMETHOD + msg);
			return ExtensionResponse.error(ex, JSONHelper.put(json, "failure", msg));
		}
		
	}


}
