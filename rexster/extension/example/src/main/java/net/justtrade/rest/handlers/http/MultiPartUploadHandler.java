package net.justtrade.rest.handlers.http;

import java.io.File;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionResponse;

import net.justtrade.rest.handlers.rdf.RDF_Loader;
import net.justtrade.rest.util.FileWriteException;


/**
* 
* Exploits 
* 
* @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
*/
public class MultiPartUploadHandler extends UploadHandler {

	private static final Logger logger = LoggerFactory.getLogger(MultiPartUploadHandler.class);
	
	/**
	 * This method leverages the capabilities of the the Apache project org.apache.commons.fileupload
	 * to load files with very little coding necessary.  They are first streamed directly to the
	 * "location-of-temp-files", and only processed individually, and stored permanently  at 
	 * "location-of-graph-archive".  Both of those locations are user-defined in rexster.xml 
	 * 
	 */
	public static ExtensionResponse handleUpload
		(Map<String, String> _configurationProperties, RexsterResourceContext _context, JSONObject _json) 
			throws JSONException, FileUploadException, FileWriteException
	{
		final String sMETHOD = "handleUpload() --> ";
		
		JSONObject filesDetails = null;
		JSONObject fileDetails = null;
		
		String msg = "";
		
		File dirTmp = new File(_configurationProperties.get(TEMP_FILES) + "/");
		
		msg = "Detected Multipart Content! Will process as POST of one or more files.";
		logger.info(sMETHOD + msg);
		_json.put("PUT or POSTbehaviour", msg);
		
		filesDetails = new JSONObject();

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory(0x0ffffff, dirTmp);

		Iterator<FileItem> iter = null;
		FileItem item = null;
		long sizeInBytes = -999;
		
		String uploadedFile = null;

		ServletFileUpload upload = new ServletFileUpload(factory);
		@SuppressWarnings("unchecked")
		List<FileItem> /* FileItem */ items = upload.parseRequest(_context.getRequest());

		String subRefNodeName = "";
		iter = items.iterator();
		int filesCount = 0;

		// Parse the request
		while (iter.hasNext()) {
			filesCount++;
			item = iter.next();
			subRefNodeName = item.getFieldName();
			sizeInBytes = item.getSize();
			
			fileDetails = new JSONObject();

			if (item.isFormField()) {

				logger.debug(sMETHOD + "Is Form Field -- Form name '" + subRefNodeName + ".");

			} else {
				
				logger.debug(sMETHOD + "Receiving File Upload.");
				logger.debug
				(
						sMETHOD 
						+       "Item named '" + subRefNodeName
						+            "' is a " + item.getSize()
						+ " byte file named '" + item.getName() 
						+        "'.  Type : " + item.getContentType()
				);

				logger.debug(sMETHOD + "Receiving File Upload.");
				uploadedFile = archiveTripleFile(item, _configurationProperties.get(GRAPH_ARCHIVE) + "/");
				
				logger.debug(sMETHOD + "Receiving File Upload.");
				RDF_Loader loader = new RDF_Loader();
//				loader.injectRDF(_configurationProperties, uploadedFile, subRefNodeName, _context);
				loader.injectRDF(uploadedFile, subRefNodeName, _context);

			}
			fileDetails.put("name", subRefNodeName);
			fileDetails.put("size", new Long(sizeInBytes));
			filesDetails.append("file_" + filesCount, fileDetails);
		}
		_json.put("filesCount", filesCount);
		_json.put("files", filesDetails);
		
		
		return ExtensionResponse.ok(_json);
		
	}
	
	/**
	 * "Information hiding" method for saving an archive copy of a file 
	 * 
	 * @param item a file item created by org.apache.commons.fileupload 
	 * @param pathArchive The place to stick it
	 * @return the full path definition of the saved file
	 * @throws FileWriteException
	 */
	protected static String archiveTripleFile (FileItem item, String pathArchive) 
		throws FileWriteException
	{
		final String sMETHOD = "archiveTripleFile(FileItem, String) --> ";

		String fileName = item.getName();

		logger.debug(sMETHOD + "Storing the file : '" + fileName + "' locally.");

		File dirArchive = new File(pathArchive + fileName);

		try {
			item.write(dirArchive);
		} catch (Exception e) {
			throw new FileWriteException();
		}
		logger.debug(sMETHOD + "Rewritten to '" + pathArchive + fileName + "'.");

		return pathArchive + fileName;

	}

}
