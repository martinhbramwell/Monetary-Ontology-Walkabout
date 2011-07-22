:: Windows launcher script for Restarting Rexster
@echo on

set DATABASE_NAME=mowa-graph
cd %CD%
call rexster-restart -configuration myRexster.xml

:DONE
echo We're done restarting 