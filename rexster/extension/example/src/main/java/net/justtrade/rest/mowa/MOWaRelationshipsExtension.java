package net.justtrade.rest.mowa;


import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.rexster.extension.ExtensionNaming;
import com.tinkerpop.rexster.extension.ExtensionDefinition;
import com.tinkerpop.rexster.extension.ExtensionPoint;
import com.tinkerpop.rexster.extension.HttpMethod;
import com.tinkerpop.rexster.extension.ExtensionDescriptor;
import com.tinkerpop.rexster.extension.ExtensionResponse;
import com.tinkerpop.rexster.extension.RexsterContext;

/**
 * This is adapted from the Rexster Kibble - SimplePathExtension sample documented here 
 * 
 * {@link <a href="https://github.com/tinkerpop/rexster/wiki/Extension-Points">Extension-Points</a>} 
 * 
 * One of the extension samples that showcase the methods available to those who wish to extend Rexster.
 *
 * This sample focuses on path-level extensions.  Path-level extensions add the extension
 * to the root of the specified ExtensionPoint followed by the value specified in the
 * "path" parameter of the @ExtensionDefinition.  It is important to ensure that paths
 * remain unique.  In the event of a collision, Rexster will serve the request to the
 * first match it finds.  Nevertheless, an extension class can share a name within the 
 * same namespace with another class.  In other words, extensions can span multiple classes
 * within the same namespace and name.
 * 
 * For example, the following two different classes serve "below" the same namespace and name as shown in this table :
 * <TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
		<TH ALIGN="center"><FONT SIZE="+2">
		<B>Class</B></FONT></TH>
		<TH ALIGN="center"><FONT SIZE="+2">
		<B>Path Served</B></FONT></TH>
	</TR>
	<TR BGCOLOR="white" CLASS="TableRowColor">
		<TD ALIGN="right" VALIGN="top">&nbsp;MOWaRootExtension</TD>
		<TD>&nbsp;http://localhost:8182/neo4jsample/mowa/stevens/
		
		<BR>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
	</TR>
	<TR BGCOLOR="white" CLASS="TableRowColor">
		<TD ALIGN="right" VALIGN="top">&nbsp;MOWaRelationshipsExtension</TD>
		<TD>&nbsp;http://localhost:8182/neo4jsample/mowa/stevens/relationships
		
		<BR>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
	</TR>
	<TR BGCOLOR="white" CLASS="TableRowColor">
		<TD ALIGN="right" VALIGN="top">&nbsp;MOWaPersonsExtension</TD>
		<TD>&nbsp;http://localhost:8182/neo4jsample/mowa/stevens/persons
		
		<BR>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
	</TR>
</TABLE>
 * 
 * @author Martin "Hasan" Bramwell (http://hasanbramwell.blogspot.com/2011/03/hello-world.html)
 * @see MOWaRootExtension, MOWaPersonsExtension
 */
@ExtensionNaming(name = MOWaRelationshipsExtension.EXTENSION_NAME, namespace = MOWaExtensionAbstract.EXTENSION_NAMESPACE)
public class MOWaRelationshipsExtension extends MOWaExtensionAbstract {
	
    public static final String EXTENSION_NAME = "stevens";
    
	public static final String basePath = EXTENSION_NAMESPACE + "/" + EXTENSION_NAME;

//	private static final Logger logger = LoggerFactory.getLogger(MOWaRelationshipsExtension.class);
	
	@Override
	public String getExtensionName() {return MOWaRootExtension.EXTENSION_NAME;}

	@Override
	public String getBasePath() {return basePath;}

     /**
     * By adding the @RexsterContext attribute to the "graph" parameter, the graph requested gets
     * automatically injected into the extension.  Therefore, when the following URI is requested:
     *
     * http://localhost:8182/tinkergraph/tp/simple-path/relationships
     *
     * the graph called "graphname" will be pushed into this method.
     */
    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "relationships", method = HttpMethod.GET)
    @ExtensionDescriptor(description = "This gives access to the 'relationships' path, with HttpMethod.GET.")
    public ExtensionResponse doGetOther(@RexsterContext Graph graph){
        return toStringIt(graph, "GET Steven's family relationships");
    }

    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH, path = "relationships", method = HttpMethod.POST)
    @ExtensionDescriptor(description = "This gives access to the 'relationships' path, with HttpMethod.POST.")
    public ExtensionResponse doPostOther(@RexsterContext Graph graph){
        return toStringIt(graph, "POST relationships");
    }

}
