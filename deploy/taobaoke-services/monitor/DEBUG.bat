@echo off
java -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=8803,server=y,suspend=n -jar intell-biz-monitor-1.0.1.jar
pause
