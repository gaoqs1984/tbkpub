@echo off
set baseDir=%AUTO_EXECUTE_HOME%
call %baseDir%/monitor/删除MonitorService系统服务.bat 1
call %baseDir%/wenku_down/删除WenkuDownLoad系统服务.bat 1
call %baseDir%/wenku_down/关闭代迅雷下载.bat 1
call %baseDir%/docin_upload/删除DocinUpload系统服务.bat 1
call %baseDir%/docin_upload/删除DocinUploadProxy系统服务.bat 1
call %baseDir%/video_down/删除VideoDownLoad系统服务.bat 1
call %baseDir%/video_down/删除VideoStore系统服务.bat 1
call %baseDir%/pomoho_upload/删除VideoUpload系统服务.bat 1
call %baseDir%/lottery/删除LotteryVote自动投注系统服务.bat 1
call %baseDir%/lottery/删除LotteryDown自动更新系统服务.bat 1
call %baseDir%/lottery/删除LotteryBuy自动购买系统服务.bat 1
call %baseDir%/doc88/删除Doc88上传系统服务.bat 1
call %baseDir%/wenku_down/关闭处理文档目录.bat 1
call %baseDir%/wenku_down/删除UpdateDownLoad系统服务.bat 1
call %baseDir%/wenku_down/删除WenkuCreate系统服务.bat 1
call %baseDir%/search/删除SearchDocsDown系统服务.bat 1
call %baseDir%/search/删除SearchInfoDown系统服务.bat 1
call %baseDir%/search/删除WenkuFileProcess统服务.bat 1
call %baseDir%/monitor/删除ExecConfig.exe系统服务.bat 1
call %baseDir%/click/删除EachDown系统服务.bat 1
call %baseDir%/click/删除RealDown系统服务.bat 1
call %baseDir%/click/删除ListDocs系统服务.bat 1
call %baseDir%/monitor/close_all.bat 1
call %baseDir%/monitor/删除LogUpload.exe系统服务.bat 1
call %baseDir%/docin_upload/删除DocinBatchUpload系统服务.bat 1

if '%1'=='' pause
