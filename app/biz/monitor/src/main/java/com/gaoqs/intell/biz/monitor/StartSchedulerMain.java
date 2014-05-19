package com.gaoqs.intell.biz.monitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gaoqs.intell.biz.monitor.model.MonitorStartModel;
import com.gaoqs.intell.commons.dal.DaoFactory;
import com.gaoqs.intell.commons.dal.dao.SysConfigDao;
import com.gaoqs.intell.commons.httpclient.HttpClientProcess;
import com.gaoqs.intell.commons.util.ProcessUtil;

/**
 * 监控启动处理类
 * 
 * @author quansong.gao
 * 
 */
public class StartSchedulerMain implements Runnable {

	/**
	 * 日志
	 */
	private static final Log log = LogFactory.getLog(StartSchedulerMain.class);

	/**
	 * 单位等待时间
	 */
	private static final int DEALY_TIME = 1 * 60 * 1000;

	/**
	 * 当前的index，单位时间的倍数
	 */
	private static int currentIndex = -1;

	/**
	 * 需要监控的列表
	 */
	private static List<MonitorStartModel> modelList;

	private static SysConfigDao sysConfigDao;

	private static HttpClientProcess process = new HttpClientProcess();

	/**
	 * 当前机器名
	 */
	private static String groupName;

	/**
	 * 验证目录
	 */
	private static String checkFolder = "E:/webapps/gaoqs-tempfolder/wenku";

	/**
	 * 下一次待检测时间
	 */
	private static int nextCheckWaitTime = -1;

	/**
	 * 启动监控主程序
	 * 
	 * @下午02:13:40
	 * @param args
	 */
	public static void main(String[] args) {
		if (args != null && args.length > 1) {
			log.warn("单独执行close操作");
			executeClose();
		} else {
			log.warn("执行监控操作");
			execute();
		}
	}

	@SuppressWarnings("unchecked")
	private static void executeClose() {
		if (sysConfigDao == null) {
			new DaoFactory();
			sysConfigDao = DaoFactory.getBean(SysConfigDao.class,
					"sysConfigDao");
		}
		List<Map<String, String>> list = sysConfigDao
				.getMapListByHql("select id as id from MonitorStartModel where available='Y' and monitorType='C'");

		if (list == null || list.size() <= 0) {
			log.warn("数据库中没有配置需要监控的记录，程序退出！");
			return;
		}

		for (Map<String, String> map : list) {
			MonitorStartModel model = sysConfigDao.get(MonitorStartModel.class,
					map.get("id"));
			// 处理单个关闭
			try {
				log.warn("关闭程序：" + model.getDescription() + ",exe="
						+ model.getCheckExe());
				ProcessUtil.stopAllProcessByPid(ProcessUtil
						.getAllPidByProcessName(model.getCheckExe()));
			} catch (Exception e) {
				log.error("关闭程序失败，exe=" + model.getCheckExe(), e);
			}
		}
	}

	/**
	 * 
	 * 1.处理过程 检测目录下，已经有多少文件，若长时间进度不正确，连续10分钟没有文件变化，则关闭 检测硬盘空间，若太小了，则报警(>=1GB)
	 * 
	 * 上传完成后，检查目录下是否还有文件，若没有文件了，修改改目录名 关闭所有word进程，ppt进程等 用新的目录下载
	 * 将用过的文件打包，上传到vdisk.cn，上传完成后，删除原来的内容
	 * 
	 * 开启上传，若上传出错，10分钟内，未出现变化，停掉，再开始，10分钟后，还是出现问题，连续3次停止 并报警
	 * 
	 * 上传完成后，关闭，切换目录，再开始上传，若没有可切换的目录，则报警 一般下载到2倍的上传文件数时，开始暂停下载操作
	 * 
	 * 系统连续1个小时没有任何任务调度时报警
	 * 
	 * 调度pomoho下载主程与上传主程处理同上
	 * 
	 * 监控主程序，每隔20分钟向服务器发送httpclient心跳消息(服务器记录最后心跳时间)
	 * 
	 * 监控主程序每隔90分钟向服务器发送处理事件，包括报警操作等
	 * 
	 * 
	 * 
	 */

	public static void execute() {
		if (sysConfigDao == null) {
			new DaoFactory();
			sysConfigDao = DaoFactory.getBean(SysConfigDao.class,
					"sysConfigDao");
		}
		loadInitData();
		StartSchedulerMain s = new StartSchedulerMain();
		new Thread(s).start();
	}

	@SuppressWarnings("unchecked")
	private static void loadInitData() {
		if (groupName == null) {
			groupName = sysConfigDao.getSysconfigValue("execute.group.id", "");
		}
		modelList = new ArrayList<MonitorStartModel>();
		List<Map<String, String>> list = sysConfigDao
				.getMapListByHql("select id as id from MonitorStartModel where available='Y'");

		if (list == null || list.size() <= 0) {
			log.warn("数据库中没有配置需要监控的记录，程序退出！");
			return;
		}

		for (Map<String, String> map : list) {
			MonitorStartModel model = sysConfigDao.get(MonitorStartModel.class,
					map.get("id"));
			if (model != null) {
				modelList.add(model);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		String status = "Y";

		while (true) {

			String needCheckEnable = sysConfigDao.getSysconfigValue(
					"monitor.gaoqs.check.enable", "Y");
			if (StringUtils.equals(needCheckEnable, "Y")) {
				try {
					// 当检测到由gaoqs.com设置的N状态后，执行关闭所有的操作，不再开启任何程序
					process.headerMap.put(process.AGENT_NAME,
							process.DEFAULT_AGENT);
					status = process.get(
							"http://www.gaoqs.com/docs/docin_status.php?name="
									+ groupName, null);
					log.warn("验证机器：" + groupName + "，中心返回内容：" + status);
					if (status == null) {
						status = "Y";
					} else {
						status = status.trim();
					}
				} catch (Exception e) {
					log.error("获取处理内容出错，", e);
					status = "Y";
				}
			} else {
				log.warn("不远程检查可用状态");
			}

			try {
				if (currentIndex > 50000) {
					currentIndex = -1;
				}
				currentIndex++;

				// 检查配置，是否允许上传和下载同时开启
				String enableUploadAndDown = sysConfigDao.getSysconfigValue(
						"monitor.upload.and.down.togeter", "true");

				// 检测判断文档数是否足够
				if ((nextCheckWaitTime == -1 || currentIndex
						% nextCheckWaitTime == 0)
						&& StringUtils.equals(enableUploadAndDown, "true")) {
					log.warn("开始检测文档是否足够...");

					String[] files = new File(checkFolder).list();
					int fileCounts = files.length;

					nextCheckWaitTime = fileCounts / 200;

					if (fileCounts <= 20) {
						nextCheckWaitTime = 5;
					}

					if (nextCheckWaitTime < 1) {
						log.error("请注意，使用最低检测等待时间...");
						nextCheckWaitTime = 1;
					} else {
						log.warn("文档数：" + fileCounts + "，下次检测间隔："
								+ nextCheckWaitTime);
					}

					if (fileCounts <= 0) {
						// 关闭上传，开启下载
						log.warn("无文档，开启下载");
						// 更新设置

						sysConfigDao
								.executeHql("update MonitorStartModel set available='N' where checkExe='DocinUpload.exe'");
						sysConfigDao
								.executeHql("update MonitorStartModel set available='Y' where checkExe='WenkuUpdateDownLoad.exe'");

						coloseAllExe();

						loadInitData();
						currentIndex = 0;
					} else if (fileCounts > 5000) {
						boolean needProcess = true;
						if (modelList != null) {
							for (MonitorStartModel model : modelList) {
								if (StringUtils.equals(model.getCheckExe(),
										"DocinUpload.exe")) {
									needProcess = false;
								}
								// 上传下载同时开的情况，不可能出现
								if ((!needProcess)
										&& StringUtils.equals(model
												.getCheckExe(),
												"WenkuUpdateDownLoad.exe")) {
									log.error("存在非法的配置，请检查...");
									// modelList.remove(model);
								}
							}
						}
						// 关闭下载，开启上传
						if (needProcess) {
							log.warn("文档量到达5K，开启上传");
							// 更新设置
							sysConfigDao
									.executeHql("update MonitorStartModel set available='Y' where checkExe='DocinUpload.exe'");
							sysConfigDao
									.executeHql("update MonitorStartModel set available='N' where checkExe='WenkuUpdateDownLoad.exe'");

							coloseAllExe();
							loadInitData();
							currentIndex = 0;
						} else {
							log.warn("超过5K文档，已经开启上传中..." + fileCounts);
						}
					} else {
						log.warn("检测文档数正常：" + fileCounts);
						// ST.GAO 检查是否开启docin上传，若没有，则开启数据库配置，并重新加载
						// 如果豆丁和下载同时开启，则关闭下载
						Map<String, String> map = sysConfigDao
								.getMapByHql("select id as id from MonitorStartModel where checkExe='DocinUpload.exe' and available='Y'");
						if (map != null && map.keySet().size() > 0) {
							Map<String, String> map2 = sysConfigDao
									.getMapByHql("select id as id from MonitorStartModel where checkExe='WenkuUpdateDownLoad.exe' and available='Y'");
							if (map2 != null && map2.keySet().size() > 0) {
								log.warn("同时存在上传和下载，关闭下载...");
								sysConfigDao
										.executeHql("update MonitorStartModel set available='N' where checkExe='WenkuUpdateDownLoad.exe'");
								loadInitData();
							} else {
								log.warn("上传正在进行中...");
							}
						} else {
							// 校验一下下载是否正在开着，否则报警
							map = sysConfigDao
									.getMapByHql("select id as id from MonitorStartModel where checkExe='WenkuUpdateDownLoad.exe' and available='Y'");
							if (map == null || map.keySet().size() <= 0) {
								log.error("上传和下载都没有开启，请检查...");
							}

						}
					}

				}

				if (status.equals("Y")) {
					// TODO 验证是否要重新加载配置，并且，设置currentIndex=0，重新开始
					for (MonitorStartModel model : modelList) {
						String monitorType = model.getMonitorType();
						int waitTime = model.getWaitTime();
						int restartTime = model.getRestartTime();

						// 处理单个重启
						if (monitorType != null
								&& "O".equals(monitorType.trim())) {
							if (waitTime > 0 && currentIndex % waitTime == 0) {
								try {
									restart(model.getCheckExe(), model
											.getStartPath());
								} catch (Exception e) {
									log.error("重启程序失败，exe="
											+ model.getCheckExe(), e);
								}
							}
						}

						// 处理单个关闭
						if (monitorType != null
								&& "C".equals(monitorType.trim())) {
							if (restartTime > 0
									&& currentIndex % restartTime == 0
									&& currentIndex != 0) {
								try {
									log.warn("关闭程序：" + model.getDescription()
											+ ",exe=" + model.getCheckExe());
									ProcessUtil.stopAllProcessByPid(ProcessUtil
											.getAllPidByProcessName(model
													.getCheckExe()));
								} catch (Exception e) {
									log.error("关闭程序失败，exe="
											+ model.getCheckExe(), e);
								}
							}
						}

					}
				} else {
					log.warn("停止状态，关闭所有不需要的内容");
					// Runtime
					// .getRuntime()
					// .exec(
					// "cmd /c start /min "
					// + "E:/webapps/gaoqs-services/tools/远程关闭处理.bat");
					// 只是结束进程，不取消服务
					ProcessUtil.stopSingleProcessByPid(ProcessUtil
							.getSinglePidByProcessName("WINWORD.EXE"));
					ProcessUtil.stopSingleProcessByPid(ProcessUtil
							.getSinglePidByProcessName("cmd.exe"));
					ProcessUtil.stopSingleProcessByPid(ProcessUtil
							.getSinglePidByProcessName("POWERPNT.EXE"));
					ProcessUtil.stopSingleProcessByPid(ProcessUtil
							.getSinglePidByProcessName("Doc88Upload.exe"));
					ProcessUtil.stopSingleProcessByPid(ProcessUtil
							.getSinglePidByProcessName("WenkuDownLoad.exe"));
					ProcessUtil
							.stopSingleProcessByPid(ProcessUtil
									.getSinglePidByProcessName("WenkuUpdateDownLoad.exe"));
					ProcessUtil.stopSingleProcessByPid(ProcessUtil
							.getSinglePidByProcessName("DocinUpload.exe"));
					ProcessUtil.stopSingleProcessByPid(ProcessUtil
							.getSinglePidByProcessName("DocinUploadProxy.exe"));
					log.warn("程序暂停3分钟，后再次检测。。。");
					Thread.sleep(3 * 60 * 1000);
				}
				log.warn("暂停" + (DEALY_TIME / 60000) + "分后再次监控。。。");
				Thread.sleep(DEALY_TIME);
			} catch (Exception e) {
				log.error("监控线程报错", e);
				try {
					currentIndex++;
					Thread.sleep(DEALY_TIME);
				} catch (Exception e1) {
					log.error("出错后线程等待错", e1);
				}
			}

		}

	}

	/**
	 * 关闭所有的客户端exe
	 */
	private void coloseAllExe() {
		ProcessUtil.stopSingleProcessByPid(ProcessUtil
				.getSinglePidByProcessName("WINWORD.EXE"));
		ProcessUtil.stopSingleProcessByPid(ProcessUtil
				.getSinglePidByProcessName("cmd.exe"));
		ProcessUtil.stopSingleProcessByPid(ProcessUtil
				.getSinglePidByProcessName("POWERPNT.EXE"));
		// ProcessUtil.stopSingleProcessByPid(ProcessUtil
		// .getSinglePidByProcessName("Doc88Upload.exe"));
		ProcessUtil.stopSingleProcessByPid(ProcessUtil
				.getSinglePidByProcessName("WenkuDownLoad.exe"));
		ProcessUtil.stopSingleProcessByPid(ProcessUtil
				.getSinglePidByProcessName("WenkuUpdateDownLoad.exe"));
		ProcessUtil.stopSingleProcessByPid(ProcessUtil
				.getSinglePidByProcessName("DocinUpload.exe"));
		// ProcessUtil.stopSingleProcessByPid(ProcessUtil
		// .getSinglePidByProcessName("DocinUploadProxy.exe"));
	}

	/**
	 * 重启程序
	 * 
	 * @param checkExe
	 * @param startExe
	 * @throws IOException
	 */
	private void restart(String checkExe, String startExe) throws IOException {
		log.warn("验证启动程序：" + checkExe + "," + startExe);
		String unClosed[] = ProcessUtil.getAllPidByProcessName(checkExe);
		if (unClosed == null || unClosed.length == 0) {
			log.warn("已经关闭，启动程序：" + checkExe);
			Runtime.getRuntime().exec("cmd /c start /min " + startExe);
		}
	}

	// // 判断执行哪些方法
	// String executeWenkuDownload = sysConfigDao.getSysconfigValue(
	// "execute.wenku.down", "false");
	// // String executeVideoDownload = sysConfigDao.getSysconfigValue(
	// // "execute.video.down", "false");
	// // String executePomohoUpload = sysConfigDao.getSysconfigValue(
	// // "execute.pomoho.upload", "false");
	// String executeDocinUpload = sysConfigDao.getSysconfigValue(
	// "execute.docin.upload", "false");
	// // String executeTotalGetProcesser = sysConfigDao.getSysconfigValue(
	// // "execute.total", "false");
	// // String executeDefaultProcesser = sysConfigDao.getSysconfigValue(
	// // "execute.default", "false");
	// // String executeProxyGetProcesser = sysConfigDao.getSysconfigValue(
	// // "execute.proxy.get", "false");
	//
	// // 设置启动时IP
	// RouterUtil.oldIp = RouterUtil.getCurrentIp();
	//
	// DocinUserDao docinDao = DaoFactory.getBean(DocinUserDao.class,
	// "docinUserDao");
	//
	// // log.warn("启动默认处理。。。。");
	// // new Thread(new DefaultProcesser()).start();
	//
	// if (executeDocinUpload != null
	// && "true".equals(executeDocinUpload.trim())) {
	// log.warn("启动文档上传监控");
	// DcoinUploadProcesser docinUpload = new DcoinUploadProcesser();
	// docinDao.executeHql("update DocinUserUpModel set status='0'");
	// new Thread(docinUpload).start();
	// } else {
	// log.warn("本次未启动文档上传监控");
	// }
	//
	// if (executeWenkuDownload != null
	// && "true".equals(executeWenkuDownload.trim())) {
	// log.warn("启动文档下载监控");
	// WenKuDownloadProcesser wenkuDown = new WenKuDownloadProcesser();
	// new Thread(wenkuDown).start();
	// } else {
	// log.warn("本次未启动文档下载监控");
	// }

	// if (executeVideoDownload != null
	// && "true".equals(executeVideoDownload.trim())) {
	// log.warn("启动视频下载监控");
	// VideoDownloadProcesser videoDown = new VideoDownloadProcesser();
	// new Thread(videoDown).start();
	// } else {
	// log.warn("本次未启动视频下载监控");
	// }
	//
	// if (executePomohoUpload != null
	// && "true".equals(executePomohoUpload.trim())) {
	// log.warn("启动视频上传监控");
	// docinDao.executeHql("update PomohoUserUpModel set status='0'");
	// PomohoUploadProcesser pomoho = new PomohoUploadProcesser();
	// new Thread(pomoho).start();
	// } else {
	// log.warn("本次未启动视频上传监控");
	// }
	//
	// if (executeTotalGetProcesser != null
	// && "true".equals(executeTotalGetProcesser.trim())) {
	// log.warn("启动收益监控");
	// TotalGetProcesser tg = new TotalGetProcesser();
	// new Thread(tg).start();
	// } else {
	// log.warn("本次未启动收益监控");
	// }
	//
	// if (executeDefaultProcesser != null
	// && "true".equals(executeDefaultProcesser.trim())) {
	// log.warn("启动默认监控");
	// DefaultProcesser tg = new DefaultProcesser();
	// new Thread(tg).start();
	// } else {
	// log.warn("本次未启动默认监控");
	// }
	//
	// if (executeProxyGetProcesser != null
	// && "true".equals(executeProxyGetProcesser.trim())) {
	// log.warn("启动代理监控");
	// ProxyGetProcesser tg = new ProxyGetProcesser();
	// new Thread(tg).start();
	// } else {
	// log.warn("本次未启动代理监控");
	// }

	// }

	// /**
	// * 加载配置文件
	// *
	// * @下午01:59:27
	// */
	// private static void loadProperties() {
	// String baseFolder = "";
	// try {
	// URL url = StartSchedulerMain.class.getProtectionDomain()
	// .getCodeSource().getLocation();
	// baseFolder = url.getPath();
	// if (baseFolder != null) {
	// baseFolder = baseFolder.replace("\\", "/");
	// }
	// if (baseFolder.startsWith("/")) {
	// baseFolder = baseFolder.substring(1);
	// }
	// if (baseFolder.endsWith(".jar")) {
	// baseFolder = baseFolder.substring(0, baseFolder
	// .lastIndexOf("/"));
	// } else {
	// baseFolder = baseFolder.substring(0, baseFolder.length() - 1);
	// baseFolder = baseFolder.substring(0, baseFolder
	// .lastIndexOf("/"));
	// }
	// log.warn("读取配置文件：" + baseFolder + "/config.ini");
	// pro = RealPath.loadConfigFile(baseFolder + "/config.ini");
	//
	// } catch (Exception e) {
	// log.error("读取配置文件出错：" + BusinessExceptions.getDetailTrace(e));
	// }
	//
	// }
}
