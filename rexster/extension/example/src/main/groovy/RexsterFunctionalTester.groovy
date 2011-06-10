@Grab("net.sf.json-lib:json-lib:2.4:jdk15")
@Grab(group="org.codehaus.groovy.modules.http-builder",module="http-builder", version="0.5.1" )

import groovyx.net.http.RESTClient
import groovyx.net.http.ParserRegistry
import groovyx.net.http.HTTPBuilder

import groovy.json.JsonOutput
import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC

import java.lang.Character;

public class RexsterFunctionalTest
{
	// Identification of the host and database proxy interface
	def static urlGraphServer = "http://localhost:8182/"
	def static NEO4JDATABASE = "neo4jsample"
	def static urlGraphDatabase = urlGraphServer + NEO4JDATABASE + "/"
	
	
	// Prettification stuff
	def static ruler = "---- ---- ---- ---- ---- ---- ---- ----"
	def static topFrm = "\n\n" + ruler + "\n"
	def static botFrm = "\n" + ruler
	
	public static void main()
	{
		println(topFrm + "---------------[ Begin ]---------------\n\n" + urlGraphDatabase)
		
		// We get this back from each REST command
		def REST_Response
		def neo4jsample = new RESTClient( urlGraphDatabase )
		
		/*
		* This shows that reXster understands the HEAD command of HTTP
		* Obviously it is far from adequate for any serious system
		*/
		println(topFrm + "-----------[ Test connection ]---------" + botFrm)
		try {
			
			REST_Response = neo4jsample.head (path : "indices")
			println "Got request status ${REST_Response.status} from ${urlGraphDatabase}"
			
		} catch( ex ) {
			println "Got request status ${ex.response} from ${urlGraphDatabase}"
			
			// The exception is used for flow control but has access to the response as well:
			assert ex.response.status == 404
			
		}
		
		def spec = [:]
		
		/*
		 * This uses an HTTP GET to verify the database configuration
		 */
		println(topFrm + "-------------[ Neo4j Status ]----------" + botFrm)
		REST_Response = (new RESTClient(urlGraphServer)).get(path : NEO4JDATABASE)
		println ("Response : " + JsonOutput.prettyPrint(REST_Response.data.toString()))
		
		/*
		 * This uses an HTTP GET to view the indices
		 */
		println(topFrm + "-------------[ List indices ]----------" + botFrm)
		REST_Response = neo4jsample.get( path : "indices")
		println ("Response : " + JsonOutput.prettyPrint(REST_Response.data.toString()))
		
		/*
		 * This uses an HTTP GET to view the vertices
		 */
		println(topFrm + "------------[ List vertices ]----------" + botFrm)
		REST_Response = neo4jsample.get( path : "vertices")
		println ("Response : " + JsonOutput.prettyPrint(REST_Response.data.toString()))
		
		
		/*
		 * This uses an HTTP DELETE to drop all the vertices
		println(topFrm + "------------[ Delete vertices ]----------" + botFrm)
		REST_Response = neo4jsample.delete( path : "vertices")
		println ("Response : " + JsonOutput.prettyPrint(REST_Response.data.toString()))
		 */
		
		
		/*
		 * This uses an HTTP GET to view the vertices
		 */
		println(topFrm + "------------[ List vertices ]----------" + botFrm)
		REST_Response = neo4jsample.get( path : "vertices")
		println ("Response : " + JsonOutput.prettyPrint(REST_Response.data.toString()))
		
		
		/*
		 * This uses an HTTP POST to verify that we can create a new vertex
		 */
		println(topFrm + "------------[ Create vertex ]-----------" + botFrm)
		spec = [ID:33, Type:"Suit"]
		REST_Response = neo4jsample.post(
				   path : "vertices"
				, query : spec
			)
		println ("Response : " + JsonOutput.prettyPrint(REST_Response.data.toString()))
		
		/*
		* This uses an HTTP POST to verify that the extension can handle a JSON dataset.
		*/
/*
	   println(topFrm + "------[ Check POST simple-path ]---------" + botFrm)
	   spec = [ID:33, Type:"Suit"]
	   REST_Response = neo4jsample.post(
						  path : "tp-sample/simple-path/other-work"
					   , query : spec
			   )
	   println ("Response : " + JsonOutput.prettyPrint(REST_Response.data.toString()))
*/
	   
	   
		/*
		 * This uses an HTTP POST to verify the extension path is active
		 */
		println(topFrm + "-------[ Check GET cards root path ]---------" + botFrm)
		REST_Response = neo4jsample.get(
				   path : "study/cards"
			)
		println ("Response : " + JsonOutput.prettyPrint(REST_Response.data.toString()))

				

		/*
		 * This uses an HTTP POST to verify that our extension can see the JSON dataset
		 */
		
		spec = [ID:44, Type:"Deck"]
		println(topFrm + "----------[ Post Decks ]---------" + botFrm)
		REST_Response = neo4jsample.post(
			  path : "study/cards/decks"
			, body : spec
			, requestContentType : "application/x-www-form-urlencoded"
		)
		println ("Response : " + JsonOutput.prettyPrint(REST_Response.data.toString()))
		
		
		
		/*
		 * This uses an HTTP POST to verify that our extension can see the JSON dataset
		 */
		spec = [ID:55, Type:"Suit"]
		println(topFrm + "----------[ Post Suits ]---------" + botFrm)
		REST_Response = neo4jsample.post(
			  path : "study/cards/suits"
			, body : spec
			, requestContentType : "application/x-www-form-urlencoded"
		)
		println ("Response : " + JsonOutput.prettyPrint(REST_Response.data.toString()))
		
		

		
		
		println("\n\n\n----------------[ Done ]---------------" + botFrm)
	}
}


/*
 * 

curl -sd "ID=201&Type=Suit&Name=Club" "http://localhost:8182/neo4jsample/vertices/" | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
curl -sVX DELETE "http://localhost:8182/neo4jsample/study/cards/suits/16" | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
curl -svX DELETE "http://localhost:8182/neo4jsample/study/cards/suits/17" | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more

 *
 */
	
def rft = new RexsterFunctionalTest()
rft.main()
	
