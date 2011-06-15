package net.justtrade.rest.handlers.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import net.justtrade.rest.handlers.rdf.RDF_Loader;

import org.apache.commons.fileupload.FileUploadException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionResponse;


public class FileUploadHandler {

	public static final String CLASS_NAME = "\n" + "UploadHandler" + ".";

	/*
	 * This method leverages the capabilities of the the Apache project org.apache.commons.fileupload
	 * to load files with very little coding necessary.  They are first streamed directly to the
	 * "location-of-temp-files", and only processed individually, and stored permanently  at 
	 * "location-of-graph-archive".  Both of those locations are user-defined in rexster.xml 
	 * 
	 */
	public static ExtensionResponse handleUpload
		(Map<String, String> _names, RexsterResourceContext _context, JSONObject _json)
			throws JSONException, FileUploadException, IOException, FileNotFoundException
	{
		final String sMETHOD = CLASS_NAME + "handleUpload(Map, RexsterResourceContext, JSONObject) --> ";

		JSONObject json = new JSONObject();
		JSONObject filesDetails = null;
		JSONObject fileDetails = null;

		String msg = "";
		HttpServletRequest request = _context.getRequest();

		msg = "Not 'Multipart Content'! Will process as PUT of a single file.";
		System.out.println(sMETHOD + msg);

		json.append("PUT behaviour", msg);
		filesDetails = new JSONObject();
		fileDetails = new JSONObject();

		File dirDestination = new File(_names.get(UploadHandler.TEMP_FILES));
		StringBuffer oFileName = new StringBuffer();
		oFileName.append(_names.get(UploadHandler.EXTENSION_NAME_SPACE));
		oFileName.append(".");
		oFileName.append(_names.get(UploadHandler.EXTENSION_NAME));
		oFileName.append(".");
		oFileName.append((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date(System.currentTimeMillis())).toString());
		String to_name = oFileName.toString();

		File fileDestination = new File(dirDestination, to_name);

		// If the destination exists, make sure it is a writeable file
		// and ask before overwriting it. If the destination doesn't
		// exist, make sure the directory exists and is writeable.
		if (fileDestination.exists()) {
			if (!fileDestination.canWrite()) throw new FileUploadException("Destination file [" + to_name + "] is unwriteable!");
		} else {
			// If file doesn't exist, check if directory exists and is
			// writeable. If getParent() returns null, then the directory is
			// the current dir. so look up the user.dir system property to
			// find out what that is.
			if (!   dirDestination.exists())  throw new FileUploadException("Destination directory [" + dirDestination + "] does not exist!");
			if (    dirDestination.isFile())  throw new FileUploadException("Destination directory [" + dirDestination + "] is a file, not a directory!");
			if (! dirDestination.canWrite())  throw new FileUploadException("Destination directory [" + dirDestination + "] is not writeable!");
		}

		// If we've gotten this far, then everything is okay.
		// So we copy the file, a buffer of bytes at a time.
		FileOutputStream destination = null; // Stream to write to destination
		ServletInputStream inStr = request.getInputStream();

		try {
			destination = new FileOutputStream(fileDestination); // Create output stream
			byte[] buffer = new byte[4096]; // To hold file contents
			int bytes_read; // How many bytes in buffer

			// Read a chunk of bytes into the buffer, then write them out,
			// looping until we reach the end of the file (when read() returns
			// -1). Note the combination of assignment and comparison in this
			// while loop. This is a common I/O programming idiom.
			
			while ((bytes_read = inStr.read(buffer)) != -1)
			{
				// Read until EOF
				destination.write(buffer, 0, bytes_read); // write
			}
			
			System.out.println(sMETHOD + "Receiving file to : " + fileDestination.getCanonicalPath());
			RDF_Loader loader = new RDF_Loader();
			loader.injectRDF(_names, fileDestination.getCanonicalPath(), to_name, _context);


		// Always close the streams, even if exceptions were thrown
		} catch (FileNotFoundException fnfex) {
			throw fnfex;
		} catch (IOException ioex) {
			throw ioex;
		} finally {
			
			if (inStr != null)
				{try {inStr.close();} catch (IOException e) {;}}
			
			if (destination != null)
				{try {destination.close();} catch (IOException e) {;}}
		}

		json.append("File count", 1);
		fileDetails.append("File size", new Long(request.getContentLength()));
		filesDetails.append(to_name, fileDetails);
		json.append("Uploaded file", filesDetails);


		return ExtensionResponse.ok(json);

	}		

}
