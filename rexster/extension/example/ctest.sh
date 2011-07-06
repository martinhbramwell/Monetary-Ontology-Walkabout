echo GET server root
curl -v http://www.mowa.justtrade.net:8182/neo4jsample | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo .
echo GET graph root
curl -v http://www.mowa.justtrade.net:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo .
echo POST graph root
curl -vd "ID=5&Type=Pack&Name=Deck of Cards" http://www.mowa.justtrade.net:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo .
echo GET persons
curl -v http://www.mowa.justtrade.net:8182/neo4jsample/mowa/stevens/persons | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo .
echo POST persons
curl -vd "ID=5&Type=Pack&Name=Deck of Cards" http://www.mowa.justtrade.net:8182/neo4jsample/mowa/stevens/persons | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo .
echo GET relationships
curl -v http://www.mowa.justtrade.net:8182/neo4jsample/mowa/stevens/relationships | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo .
echo POST relationships
curl -vd "ID=5&Type=Pack&Name=Deck of Cards" http://www.mowa.justtrade.net:8182/neo4jsample/mowa/stevens/relationships | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo . . . . . . 
echo .
echo PUT small file
curl -v -T ./src/main/resources/data/TheStevens.owl  -H "Content-Type: text/plain"  http://www.mowa.justtrade.net:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo . . . . . . 
echo .
echo PUT big file
curl -v -T ./src/main/resources/data/family.swrl.owl  -H "Content-Type: text/plain"  http://www.mowa.justtrade.net:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo . . . . . . 
echo .
echo POST two big files
curl -v -F "Family SWRL=@./src/main/resources/data/family.swrl.owl"  -F "Stevens Family=@./src/main/resources/data/TheStevens.owl"  http://www.mowa.justtrade.net:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo = = = = = = = = = = = =  
echo .

curl -v  -F "silliness=@./src/main/resources/data/silly.json"  http://www.mowa.justtrade.net:8182/neo4jsample/mowa/stevens  | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more

