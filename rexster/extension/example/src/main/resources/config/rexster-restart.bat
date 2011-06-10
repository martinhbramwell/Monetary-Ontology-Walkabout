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

echo Wait 3 seconds for it to completely disappear ...
PING 1.1.1.1 -n 1 -w 3000 >NUL
echo Should be gone now.  Now restart it.

call rexster-start.bat -webroot target/%TARGET%/bin/public %*
