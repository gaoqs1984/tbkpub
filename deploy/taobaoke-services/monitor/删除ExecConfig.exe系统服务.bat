@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.
echo.停止ExecConfig系统服务......
echo.
net stop ExecConfig
echo.
echo.删除ExecConfig系统服务......
echo.
%baseDir%/bin/AppToService /Remove "ExecConfig"
if '%1'=='' pause

