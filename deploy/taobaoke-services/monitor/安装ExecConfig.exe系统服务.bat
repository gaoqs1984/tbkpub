@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.��װExecConfigϵͳ����......
%baseDir%/bin/AppToService /install "%javaDir%/bin/ExecConfig.exe" /show:7 /absname:"ExecConfig" /startup:A /Arguments:" -jar %baseDir%/monitor/intell-biz-execute-1.0.1.jar execute" 
echo.
echo.����ExecConfigϵͳ����......
echo.
net start ExecConfig
if '%1'=='' pause

