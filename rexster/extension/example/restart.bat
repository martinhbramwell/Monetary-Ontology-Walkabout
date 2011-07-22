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
echo start /I "Restarting Rexster" /D %REXSTERDIR% mowa-restart.bat  -configuration myRexster.xml
start /I "Restarting Rexster" /D %REXSTERDIR% mowa-restart.bat  -configuration myRexster.xml
echo .
echo --------------------------------------------------
echo ----------[ Wait for Rexster to wake up ]---------
echo --------------------------------------------------
echo .

echo Waiting for %REXSTERDIR%\data\mowa-graph\lock to reappear
rem dir %REXSTERDIR%\data\mowa-graph\lock

:WAIT_FOR_LOCK

echo .
PING 1.1.1.1 -n 1 -w 1000 >NUL
if not exist %REXSTERDIR%\data\mowa-graph\lock goto :WAIT_FOR_LOCK


:TIMED_WAIT
set SECS=2
echo Wait %SECS% seconds for it to become completely active ...
PING 1.1.1.1 -n 1 -w %SECS%000 >NUL

:START_IT_BACK_UP_AGAIN

echo .
echo .
echo --------------------------------------------------
echo ---------------[ GET server root. ]---------------
echo --------------------------------------------------
echo .
curl %VERBOSE% http://localhost:8182/neo4jsample | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo .
echo --------------------------------------------------
echo ----------------[ GET graph root. ]---------------
echo --------------------------------------------------
echo .
curl %VERBOSE% http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo .
echo --------------------------------------------------
echo -----------------[ GET persons. ]-----------------
echo --------------------------------------------------
echo .
curl %VERBOSE% http://localhost:8182/neo4jsample/mowa/stevens/persons | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo . 
echo --------------------------------------------------
echo --------------[ GET relationships. ]--------------
echo --------------------------------------------------
echo .
curl %VERBOSE% http://localhost:8182/neo4jsample/mowa/stevens/relationships | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo .
echo --------------------------------------------------
echo --------------[ PUT Stevens Family. ]-------------
echo --------------------------------------------------
echo .
curl %VERBOSE% -T .\src\main\resources\data\theStevens.owl  -H "Content-Type: text/plain"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo .
echo --------------------------------------------------
echo -------------[ DELETE Stevens Family. ]-----------
echo --------------------------------------------------
echo .
curl %VERBOSE% -d "refVertex=ABCDEFG" -SX DELETE "http://localhost:8182/neo4jsample/mowa/stevens/" | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
echo.
echo .

goto :DONE
:DONE
echo Finished restart of testing ...