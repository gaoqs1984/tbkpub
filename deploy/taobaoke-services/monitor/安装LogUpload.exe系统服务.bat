@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.��װLogUploadϵͳ����......
%baseDir%/bin/AppToService /install "%javaDir%/bin/LogUpload.exe" /show:7 /absname:"LogUpload" /startup:A /Arguments:" -jar %baseDir%/monitor/intell-biz-execute-1.0.1.jar logUpload" 
echo.
echo.����LogUploadϵͳ����......
echo.
net start LogUpload
if '%1'=='' pause

