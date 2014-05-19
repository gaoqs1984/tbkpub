@echo off
set baseDir=%AUTO_EXECUTE_HOME%
set javaDir=%JAVA_HOME_JDK6%
echo.安装MonitorService系统服务......
%baseDir%/bin/AppToService /install "%javaDir%/bin/MonitorService.exe" /show:7 /absname:"MonitorService" /startup:A /Arguments:" -jar %baseDir%/monitor/intell-biz-monitor-1.0.1.jar com.gaoqs.intell.biz.monitor.StartSchedulerMain" 
echo.
echo.启动MonitorService系统服务......
echo.
net start MonitorService
if '%1'=='' pause

