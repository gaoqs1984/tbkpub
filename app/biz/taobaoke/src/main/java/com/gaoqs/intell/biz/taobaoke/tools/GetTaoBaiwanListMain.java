package com.gaoqs.intell.biz.taobaoke.tools;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gaoqs.intell.biz.taobaoke.model.site.SiteWordModel;
import com.gaoqs.intell.commons.dal.DaoFactory;
import com.gaoqs.intell.commons.dal.dao.BaseDao;
import com.gaoqs.intell.commons.httpclient.HttpClientProcess;
import com.gaoqs.intell.commons.string.StringProcess;

/**
 * 获取指定网站的列表信息
 * 
 * @author Administrator
 * 
 */
public class GetTaoBaiwanListMain {

	HttpClientProcess process;
	BaseDao dao;
	private static final Log log = LogFactory
			.getLog(GetTaoBaiwanListMain.class);
	int count = 0;

	public static void main(String[] args) {
		new GetTaoBaiwanListMain().execute();
	}

	private void execute() {
		process = new HttpClientProcess();
		new DaoFactory();
		dao = DaoFactory.getBaseDao();

		String baseurl = "http://www.zhongyin.org/";

		try {
			getSubPage(baseurl);
		} catch (Exception e) {
			log.error("处理单页出错：" + baseurl, e);
		}
	}

	/**
	 * 获取页面数据
	 * 
	 * @param url
	 */
	private void getSubPage(String url) {
		log.warn("处理url：" + url);
		String result = process.get(url, null);
		String startFlag = "http://www.zhongyin.org/procat/";
		String endFlag = "</a>";

		List<String> list = StringProcess.processAllDetail(result, startFlag,
				endFlag, startFlag.length(), 0);
		for (String string : list) {
			String id = StringProcess.processDetail(string, null, "/\">", 0, 0);
			// 查询id是否存在
			SiteWordModel model = dao.get(SiteWordModel.class, id);
			if (model != null) {
				log.warn("对象已经存在：" + id);
				continue;
			}

			String name = StringProcess.processDetail(string, "/\">", null,
					"/\">".length(), 0);
			model = new SiteWordModel();
			model.setId(id);
			model.setName(name);
			model.setCategory_id(1000);
			model.setCreate_at(new Date().getTime());
			count++;
			log.warn("存入对象：" + id + "@" + name + "@" + count);
			dao.saveSimple(model);

			// 处理子页面
			try {
				getSubPage("http://www.zhongyin.org/procat/" + id + "/");
			} catch (Exception e) {
				log.error("处理单页出错：" + id, e);
			}

		}
	}

}
