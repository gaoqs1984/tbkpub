@echo off

echo -------------------------------------------------------------------------
echo Gaoqs Services Build/Config/Deploy Script for Win32
echo -------------------------------------------------------------------------

setlocal
set CURRENT_DIR=%~dp0%

if "%1" == "build" goto BUILD
if "%1" == "deploy" goto DEPLOY
goto CONFIG

:BUILD

mvn -Dmaven.test.skip=true -o install
 
goto END

:CONFIG

echo .no need to config

goto END

:DEPLOY

ant -f build.xml

:END