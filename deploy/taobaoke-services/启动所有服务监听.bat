@echo off
set baseDir=%AUTO_EXECUTE_HOME%
call %baseDir%/monitor/安装MonitorService系统服务.bat 1
if '%1'=='' pause

