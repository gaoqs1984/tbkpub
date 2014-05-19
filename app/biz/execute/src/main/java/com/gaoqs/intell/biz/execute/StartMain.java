package com.gaoqs.intell.biz.execute;

import com.gaoqs.intell.biz.execute.logs.LogUploadMain;

public class StartMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length < 1 || args[0].trim().equals("")) {
			System.out.println("未配置启动参数，无法开启程序！");
			return;
		}
		// 启动类型
		String type = args[0];

		// 处理剩下的参数传递
		String leftArgs[] = null;
		if (args.length > 1) {
			leftArgs = new String[args.length - 1];
			for (int i = 1; i < args.length; i++) {
				leftArgs[i - 1] = args[i];
			}
		}

		// 分批启动
		if (type.trim().equals("execute")) {
			System.out.println("启动类型：" + type);
			StartMainExecute.main(leftArgs);
		} else if (type.trim().equals("checkClose")) {
			SpecialCheckCloseAllMain.main(leftArgs);
		} else if (type.trim().equals("logUpload")) {
			LogUploadMain.main(leftArgs);
		}else {
			System.out.println("未指定参数，退出...");
		}
	}

}
