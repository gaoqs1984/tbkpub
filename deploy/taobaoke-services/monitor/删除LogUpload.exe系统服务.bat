@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.
echo.ֹͣLogUploadϵͳ����......
echo.
net stop LogUpload
echo.
echo.ɾ��LogUploadϵͳ����......
echo.
%baseDir%/bin/AppToService /Remove "LogUpload"
if '%1'=='' pause

