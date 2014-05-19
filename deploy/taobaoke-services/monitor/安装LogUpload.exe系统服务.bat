@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.安装LogUpload系统服务......
%baseDir%/bin/AppToService /install "%javaDir%/bin/LogUpload.exe" /show:7 /absname:"LogUpload" /startup:A /Arguments:" -jar %baseDir%/monitor/intell-biz-execute-1.0.1.jar logUpload" 
echo.
echo.启动LogUpload系统服务......
echo.
net start LogUpload
if '%1'=='' pause

