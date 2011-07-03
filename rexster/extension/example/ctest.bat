@echo off
echo GET server root
curl -s http://localhost:8182/neo4jsample | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo GET graph root
curl -s http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo POST graph root
curl -sd "ID=5&Type=Pack&Name=Deck of Cards" http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo GET persons
curl -s http://localhost:8182/neo4jsample/mowa/stevens/persons | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo POST persons
curl -sd "ID=5&Type=Pack&Name=Deck of Cards" http://localhost:8182/neo4jsample/mowa/stevens/persons | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo GET relationships
curl -s http://localhost:8182/neo4jsample/mowa/stevens/relationships | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo POST relationships
curl -sd "ID=5&Type=Pack&Name=Deck of Cards" http://localhost:8182/neo4jsample/mowa/stevens/relationships | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo . . . . . . 
echo.
echo PUT small file
curl -s -T .\src\main\resources\data\theStevens.owl  -H "Content-Type: text/plain"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo . . . . . . 
echo.
echo PUT big file
curl -s -T .\src\main\resources\data\family.swrl.owl  -H "Content-Type: text/plain"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo . . . . . . 
echo.
echo POST two big files
curl -s -F "Family SWRL=@.\src\main\resources\data\family.swrl.owl"  -F "Stevens Family=@.\src\main\resources\data\TheStevens.owl"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more

 