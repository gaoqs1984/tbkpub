@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.�ر����е�δ�ص�exe......
%javaDir%/bin/ExecConfig.exe -jar %baseDir%/monitor/intell-biz-execute-1.0.1.jar checkClose" 
if '%1'=='' pause

