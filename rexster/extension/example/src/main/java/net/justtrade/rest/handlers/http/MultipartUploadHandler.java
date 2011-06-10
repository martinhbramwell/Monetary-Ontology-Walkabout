package net.justtrade.rest.handlers.http;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.tinkerpop.rexster.RexsterApplicationGraph;
import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionConfiguration;
import com.tinkerpop.rexster.extension.ExtensionResponse;

import net.justtrade.rest.handlers.rdf.RDF_Loader;
import net.justtrade.rest.mowa.AbstractStudyExtension;


public class MultipartUploadHandler {

	public static final String CLASS_NAME = "\nRDF_Loader.";
	

	private static final String GRAPH_ARCHIVE = "location-of-graph-archive";
	private static final String TEMP_FILES = "location-of-temp-files";

	public ExtensionResponse handleUpload(AbstractStudyExtension caller, HttpServletRequest httpRequest, RexsterResourceContext context)
	{
		final String sMETHOD = CLASS_NAME + "injectRDF() --> ";
		
		JSONObject json = new JSONObject();
		
		
		RexsterApplicationGraph rag = context.getRexsterApplicationGraph();
		ExtensionConfiguration configuration = rag.findExtensionConfiguration(caller.getExtensionNameSpace(), caller.getExtensionName());
		
		
		System.out.println(sMETHOD + "Detected 'Multipart Content'! Will process as PUT of one or more files.");

		Map<String, String> cfg = configuration.tryGetMapFromConfiguration();
		String pathTempFiles = cfg.get(TEMP_FILES) + "/";
		File dirTmp = new File(pathTempFiles);
		String pathArchive = cfg.get(GRAPH_ARCHIVE) + "/";
		System.out.println(sMETHOD + "File archive path : " + pathArchive);



		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory(0x0ffffff, dirTmp);

		Iterator<FileItem> iter = null;
		FileItem item = null;
		long sizeInBytes = -999;

		try {

			ServletFileUpload upload = new ServletFileUpload(factory);
			@SuppressWarnings("unchecked")
			List<FileItem> /* FileItem */ items = upload.parseRequest(httpRequest);

			String name = "";
			iter = items.iterator();

			// Parse the request
			while (iter.hasNext()) {
				item = iter.next();
				name = item.getFieldName();

				if (item.isFormField()) {

					System.out.println(sMETHOD + "Is Form Field -- Form name '" + name + ".");

				} else {

					System.out.println(sMETHOD + "Receiving File Upload.");
					RDF_Loader loader = new RDF_Loader();
					loader.injectRDF(caller, item, context);

				}
			}
			json.append("Byte Count", sizeInBytes);

		} catch (FileUploadException fuex) {
			System.out.println(sMETHOD + "* * * File Upload Failure * * * \n" + fuex.getLocalizedMessage());

		} catch (JSONException jsonex) {
			System.out.println(sMETHOD + "* * * Invalid JSON * * * \n" + jsonex.getLocalizedMessage());

		} catch (Exception ex) {
			System.out.println(sMETHOD + "* * * Input/output problem with upload file * * * \n" + ex.getLocalizedMessage());

		} finally {
			factory = null;
			item = null;
			iter = null;
		}
		
		return ExtensionResponse.ok(json);
		
	}		

}
