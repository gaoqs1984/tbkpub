@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.
echo.ֹͣMonitorServiceϵͳ����......
echo.
net stop MonitorService
echo.
echo.ɾ��MonitorServiceϵͳ����......
echo.
%baseDir%/bin/AppToService /Remove "MonitorService"
if '%1'=='' pause

