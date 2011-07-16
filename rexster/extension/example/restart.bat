@echo off
echo .
echo --------------------------------------------------
echo ----------------[ Set Parameters ]----------------
echo --------------------------------------------------
echo .
set VERBOSE=-S
set BASEDIR=%~dp0%APARM%
echo    Base dir : %BASEDIR%
rem set REXSTERDIR=E:\programs\com\tinkerpop\rexster\target\rexster-0.4-SNAPSHOT-standalone\bin
set REXSTERDIR=E:\programs\com\tinkerpop\rexster\
echo Rexster dir : %REXSTERDIR%
echo .
echo --------------------------------------------------
echo ---------------[ Restart Rexster ]---------------
echo --------------------------------------------------
echo .
echo start /I "Restarting Rexster" /D %REXSTERDIR% rexster-restart.bat  -configuration myRexster.xml
start /I "Restarting Rexster" /D %REXSTERDIR% rexster-restart.bat  -configuration myRexster.xml
echo .
echo --------------------------------------------------
echo --------[ Wait 15 secs for full start up ]--------
echo --------------------------------------------------
echo .
PING 1.1.1.1 -n 1 -w 15000 >NUL
echo .
echo .
echo ------------------------
echo --[ GET server root. ]--
echo ------------------------
echo .
curl %VERBOSE% http://localhost:8182/neo4jsample | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo .
echo ------------------------
echo --[ GET graph root. ]---
echo ------------------------
echo .
curl %VERBOSE% http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo .
echo ------------------------
echo ----[ GET persons. ]----
echo ------------------------
echo .
curl %VERBOSE% http://localhost:8182/neo4jsample/mowa/stevens/persons | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo .
echo ------------------------
echo -[ GET relationships. ]-
echo ------------------------
echo .
curl %VERBOSE% http://localhost:8182/neo4jsample/mowa/stevens/relationships | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo .

rem curl %VERBOSE% -T .\src\main\resources\data\theStevens.owl  -H "Content-Type: text/plain"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
