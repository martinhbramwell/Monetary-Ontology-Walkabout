:: Windows launcher script for Restarting Rexster
@echo off

cd %CD%\target\

set TARGET=

for /f "tokens=*" %%a in ('dir /b /ad') do (
if exist "%%a\bin\rexster-stop.bat" set TARGET=%%a
)

cd %TARGET%\bin\
rem call rexster-stop.bat %*
call rexster-stop.bat

set NO_LOCK_FILE=.\data\\lock
set DATABASE_LOCK_FILE=.\data\%DATABASE_NAME%\lock
echo Wait for %DATABASE_LOCK_FILE% to disappear

if %DATABASE_LOCK_FILE%==%NO_LOCK_FILE% goto :TIMED_WAIT
:EXISTS_WAIT
echo Waiting
PING 1.1.1.1 -n 1 -w 2000 >NUL

if exist %DATABASE_LOCK_FILE% goto :EXISTS_WAIT
goto :START_IT_BACK_UP_AGAIN


:TIMED_WAIT
echo Wait 4 seconds for it to completely disappear ...
PING 1.1.1.1 -n 1 -w 4000 >NUL


:START_IT_BACK_UP_AGAIN
echo Rexster should be gone now.  Now restart it.
echo call rexster-start.bat -webroot target/%TARGET%/bin/public %*
call rexster-start.bat -webroot target/%TARGET%/bin/public %*

:DONE
echo We're done