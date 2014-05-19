@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.安装ExecConfig系统服务......
%baseDir%/bin/AppToService /install "%javaDir%/bin/ExecConfig.exe" /show:7 /absname:"ExecConfig" /startup:A /Arguments:" -jar %baseDir%/monitor/intell-biz-execute-1.0.1.jar execute" 
echo.
echo.启动ExecConfig系统服务......
echo.
net start ExecConfig
if '%1'=='' pause

