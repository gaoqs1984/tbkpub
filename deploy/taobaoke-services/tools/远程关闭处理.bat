@echo off
set baseDir=%AUTO_EXECUTE_HOME%
call %baseDir%/wenku_down/ɾ��WenkuDownLoadϵͳ����.bat 1
call %baseDir%/wenku_down/�رմ�Ѹ������.bat 1
call %baseDir%/docin_upload/ɾ��DocinUploadϵͳ����.bat 1
call %baseDir%/docin_upload/ɾ��DocinUploadProxyϵͳ����.bat 1
call %baseDir%/video_down/ɾ��VideoDownLoadϵͳ����.bat 1
call %baseDir%/video_down/ɾ��VideoStoreϵͳ����.bat 1
call %baseDir%/pomoho_upload/ɾ��VideoUploadϵͳ����.bat 1
call %baseDir%/lottery/ɾ��LotteryVote�Զ�Ͷעϵͳ����.bat 1
call %baseDir%/doc88/ɾ��Doc88�ϴ�ϵͳ����.bat 1
call  %baseDir%/tools/EXECUTE_CLOSE.bat
if '%1'=='' pause
