@echo off
set VERBOSE=-S
echo GET server root
curl %VERBOSE% http://localhost:8182/neo4jsample | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo GET graph root
curl %VERBOSE% http://localhost:8182/neo4jsample/mowa/stevens-family | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo POST graph root
curl %VERBOSE% -d "ID=5&Type=Pack&Name=Deck of Cards" http://localhost:8182/neo4jsample/mowa/stevens-family | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo GET persons
curl %VERBOSE% http://localhost:8182/neo4jsample/mowa/stevens/persons | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo POST persons
curl %VERBOSE% -d "ID=5&Type=Pack&Name=Deck of Cards" http://localhost:8182/neo4jsample/mowa/stevens/persons | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo GET relationships
curl %VERBOSE% http://localhost:8182/neo4jsample/mowa/stevens/relationships | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo POST relationships
curl %VERBOSE%d "ID=5&Type=Pack&Name=Deck of Cards" http://localhost:8182/neo4jsample/mowa/stevens/relationships | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo . . . . . . 
echo.
echo PUT small file
curl %VERBOSE% -T .\src\main\resources\data\theStevens.owl  -H "Content-Type: text/plain"  http://localhost:8182/neo4jsample/mowa/stevens-family | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo . . . . . . 
echo.
echo PUT big file
curl %VERBOSE% -T .\src\main\resources\data\family.swrl.owl  -H "Content-Type: text/plain"  http://localhost:8182/neo4jsample/mowa/stevens-family | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo . . . . . . 
echo.
echo POST two big files
curl %VERBOSE% -F "Family SWRL=@.\src\main\resources\data\family.swrl.owl"  -F "Stevens Family=@.\src\main\resources\data\TheStevens.owl"  http://localhost:8182/neo4jsample/mowa/stevens-family | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more

 