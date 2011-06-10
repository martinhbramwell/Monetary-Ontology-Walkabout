@echo off
echo .
echo --------------------------------------------------
echo ----------------[ Set Parameters ]----------------
echo --------------------------------------------------
echo .
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
echo --------[ Wait 8 secs for full start up ]--------
echo --------------------------------------------------
echo .
PING 1.1.1.1 -n 1 -w 8000 >NUL
echo .
echo --------------------------------------------------
echo ------------[ Load Some Owl Files ]------------
echo --------------------------------------------------
echo .
echo curl -svF "CarAds=@.\src\main\resources\data\family.swrl.owl"  http://localhost:8182/neo4jsample/mowa/stevens/suits
curl -sv -F "CarAds=@.\src\main\resources\data\family.swrl.owl"  -F "CarAds=@.\src\main\resources\data\TheStevens.owl"  http://localhost:8182/neo4jsample/mowa/stevens | groovy -e "println(groovy.json.JsonOutput.prettyPrint(System.in.text))" | more
