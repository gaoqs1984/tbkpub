@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.关闭所有的未关的exe......
%javaDir%/bin/ExecConfig.exe -jar %baseDir%/monitor/intell-biz-execute-1.0.1.jar checkClose" 
if '%1'=='' pause

