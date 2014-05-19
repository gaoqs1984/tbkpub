@echo off
set baseDir=%AUTO_EXECUTE_HOME%
java -jar %baseDir%/monitor/intell-biz-monitor-1.0.1.jar com.gaoqs.intell.biz.monitor.StartSchedulerMain close all
if '%1'=='' pause
