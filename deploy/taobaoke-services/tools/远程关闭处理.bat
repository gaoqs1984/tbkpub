@echo off
set baseDir=%AUTO_EXECUTE_HOME%
call %baseDir%/wenku_down/删除WenkuDownLoad系统服务.bat 1
call %baseDir%/wenku_down/关闭代迅雷下载.bat 1
call %baseDir%/docin_upload/删除DocinUpload系统服务.bat 1
call %baseDir%/docin_upload/删除DocinUploadProxy系统服务.bat 1
call %baseDir%/video_down/删除VideoDownLoad系统服务.bat 1
call %baseDir%/video_down/删除VideoStore系统服务.bat 1
call %baseDir%/pomoho_upload/删除VideoUpload系统服务.bat 1
call %baseDir%/lottery/删除LotteryVote自动投注系统服务.bat 1
call %baseDir%/doc88/删除Doc88上传系统服务.bat 1
call  %baseDir%/tools/EXECUTE_CLOSE.bat
if '%1'=='' pause
