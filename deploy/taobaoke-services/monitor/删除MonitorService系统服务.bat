@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.
echo.停止MonitorService系统服务......
echo.
net stop MonitorService
echo.
echo.删除MonitorService系统服务......
echo.
%baseDir%/bin/AppToService /Remove "MonitorService"
if '%1'=='' pause

