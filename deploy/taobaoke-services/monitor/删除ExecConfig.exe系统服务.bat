@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.
echo.ֹͣExecConfigϵͳ����......
echo.
net stop ExecConfig
echo.
echo.ɾ��ExecConfigϵͳ����......
echo.
%baseDir%/bin/AppToService /Remove "ExecConfig"
if '%1'=='' pause

