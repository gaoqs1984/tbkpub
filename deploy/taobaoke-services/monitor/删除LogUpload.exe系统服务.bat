@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.
echo.停止LogUpload系统服务......
echo.
net stop LogUpload
echo.
echo.删除LogUpload系统服务......
echo.
%baseDir%/bin/AppToService /Remove "LogUpload"
if '%1'=='' pause

