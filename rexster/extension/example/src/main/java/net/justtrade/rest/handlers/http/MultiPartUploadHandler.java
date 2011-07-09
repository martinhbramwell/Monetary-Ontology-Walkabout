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

import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionResponse;

import net.justtrade.rest.handlers.rdf.RDF_Loader;
import net.justtrade.rest.util.FileWriteException;


public class MultiPartUploadHandler extends UploadHandler {

	public static final String CLASS_NAME = "\n" + "MultiPartUploadHandler" + ".";
	
	/*
	 * This method leverages the capabilities of the the Apache project org.apache.commons.fileupload
	 * to load files with very little coding necessary.  They are first streamed directly to the
	 * "location-of-temp-files", and only processed individually, and stored permanently  at 
	 * "location-of-graph-archive".  Both of those locations are user-defined in rexster.xml 
	 * 
	 */
	public static ExtensionResponse handleUpload
		(Map<String, String> _names, RexsterResourceContext _context, JSONObject _json) 
			throws JSONException, FileUploadException, FileWriteException
	{
		final String sMETHOD = CLASS_NAME + "handleUpload() --> ";
		
		JSONObject filesDetails = null;
		JSONObject fileDetails = null;
		
		String msg = "";
		
		File dirTmp = new File(_names.get(TEMP_FILES) + "/");
		
		msg = "Detected Multipart Content! Will process as POST of one or more files.";
		System.out.println(sMETHOD + msg);
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

		String name = "";
		iter = items.iterator();
		int filesCount = 0;

		// Parse the request
		while (iter.hasNext()) {
			filesCount++;
			item = iter.next();
			name = item.getFieldName();
			sizeInBytes = item.getSize();
			
			fileDetails = new JSONObject();

			if (item.isFormField()) {

				System.out.println(sMETHOD + "Is Form Field -- Form name '" + name + ".");

			} else {
				
				System.out.println(sMETHOD + "Receiving File Upload.");
				System.out.println
				(
						sMETHOD 
						+       "Item named '" + name
						+            "' is a " + item.getSize()
						+ " byte file named '" + item.getName() 
						+        "'.  Type : " + item.getContentType()
				);

				

				System.out.println(sMETHOD + "Receiving File Upload.");
				uploadedFile = archiveTripleFile(item, _names.get(GRAPH_ARCHIVE) + "/");
				
				System.out.println(sMETHOD + "Receiving File Upload.");
				RDF_Loader loader = new RDF_Loader();
				loader.injectRDF(_names, uploadedFile, name, _context);

			}
			fileDetails.put("name", name);
			fileDetails.put("size", new Long(sizeInBytes));
			filesDetails.append("file_" + filesCount, fileDetails);
		}
		_json.put("filesCount", filesCount);
		_json.put("files", filesDetails);
		
		
		return ExtensionResponse.ok(_json);
		
	}
	
	protected static String archiveTripleFile (FileItem item, String pathArchive) throws FileWriteException
	{
		final String sMETHOD = CLASS_NAME + "archiveTripleFile(FileItem, String) --> ";

		String fileName = item.getName();

		System.out.println(sMETHOD + "Storing locally the file : '" + fileName + "'");

		File dirArchive = new File(pathArchive + fileName);

		try {
			item.write(dirArchive);
		} catch (Exception e) {
			throw new FileWriteException();
		}
		System.out.println(sMETHOD + "Rewritten to '" + pathArchive + fileName + "'.");

		return pathArchive + fileName;

	}

}
