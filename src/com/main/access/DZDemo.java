package com.main.access;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.main.parseHTML.Process;
import com.utils.FormatUtil;

public class DZDemo {
	private static Logger log =Logger.getLogger(DZDemo.class);
	// 获得httpclient
	private static HttpClient httpclient = new HttpClient();

	// 定义一个url，url表示最终所搜的域名
	private String url;

	// 定义i为1，i表示页码编号，作用是循环获得所有页码
	private static int i = 1;
	// Scanner s = new Scanner(System.in);
	// 定义str，str表示输入的地名
	private String str = null;

	// 定义keywordstr，keywordstr表示输入的要搜索的字词
	private static String keywordstr = null;

	// 定义shop，shop表示你搜索到的商铺信息（包括商铺名称、星级、点评数）
	private String shop = null;

	// 定义shoplist，为了存放搜索到的所有商铺信息
	List<String> shoplist = new ArrayList<String>();

	private static LinkedBlockingQueue qp = null;

	// 有参构造，作用是获得在页面上输入的参数
	public DZDemo() {
		super();
	}

	// 获得区域Url
	public List<Map<String, String>> getAreaUrl(String select) {
		// 定义区域URL
		List<Map<String, String>> areaUrl = new ArrayList<Map<String, String>>();
		GetMethod getmethod = new GetMethod(
				"http://www.dianping.com/search/category/5/10");
		getmethod.setRequestHeader("Accept", "text/html");
		getmethod
				.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		getmethod
				.setRequestHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
		try {
			int statucode = httpclient.executeMethod(getmethod);
			if (statucode == HttpStatus.SC_OK) {
				String html = getmethod.getResponseBodyAsString();
				Document doc = getDocument(html);
				// 获得所有区域所在的块
				Elements elements = doc.select("[id=" + select + "]").select(
						"a");
				for (Element element : elements) {
					// 获得区域名称
					Map<String, String> map = new HashMap<String, String>();
					map.put("AreaStr", element.text());
					map.put("href", element.attr("href"));
					areaUrl.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getmethod.releaseConnection();
		}
		return areaUrl;
	}

	private void getArrayUrlList(List<String> searchRegUrl, List<String> list,
			String keyword) {
		for (String url : searchRegUrl) {
			GetMethod get = new GetMethod(url);
			get.setRequestHeader("Accept", "text/html");
			get.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
			get.setRequestHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
			try {
				int statuscode = httpclient.executeMethod(get);
				if (statuscode == HttpStatus.SC_OK) {
					// 获得string类型的响应，该响应表示你所输入的区域对应的返回的响应
					String html = get.getResponseBodyAsString();
					Document doc = getDocument(html);
					// 获得大众点评搜索栏块
					Elements elements = doc
							.select("input[class=J-search-input]");
					// 获得最终搜索的域名
					url = "http://www.dianping.com/search/category/"
							+ elements.attr("data-s-cityid") + "/"
							+ elements.attr("data-s-cateid") + "_" + keyword;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				get.releaseConnection();
			}
			list.add(url);
		}
	}

	// 执行搜索并输出结果
	public void SearchKeyword(LinkedBlockingQueue queue) {
		// 获得最终搜索的所有域名（p表示页码，i表示页码编号）
		qp = queue;
		List<Map<String, String>> rurlL = getAreaUrl("region-nav");
		List<Map<String, String>> curlL = getAreaUrl("classfy");
		String url = "http://www.dianping.com/search/category/5/10/";
		long sum = rurlL.size() * curlL.size(); 
		if (sum > 0) {
			ExecutorService service = Executors.newFixedThreadPool(2*rurlL.size());
		    log.info("   |||----时间：   "+FormatUtil.dateFormatYMDHMS(new Date())+"--类别： /search/category/5/10/ 有 "+sum+" 条爬取目录----启动线程池大小："+2*rurlL.size());
			for (Map<String, String> rl : rurlL) {
				String aText = rl.get("AreaStr") + "";
				String href = rl.get("href") + "";
				String reg = href.substring(href.lastIndexOf("/") + 1);
				for (Map<String, String> cl : curlL) {
					String aText2 = cl.get("AreaStr") + "";
					String href2 = cl.get("href") + "";
					String reg2 = href2.substring(href2.lastIndexOf("/") + 1);
					url += reg2 + reg;
					Map<String, String> map = new HashMap<String, String>();
					map.put("area", aText);
					map.put("small_cate", aText2);
					map.put("small_cate_id", reg2);
					// Process instance = Process.getSingleInstance(map, queue,reg2+reg);
					 Process process = new Process(map, queue, reg2 + reg,url,i);
					 service.execute(process);
					// instance.getResult(url, i);
				}
			}
		}
	}

	public List<String> getShoplist() {// shoplist的get方法，方便后面获得shoplist
		return shoplist;
	}

	public static Document getDocument(String str) {// 创建Jsoup将String类型解析成Document类型的方法
		return Jsoup.parse(str);
	}

	public static void main(String[] args) {
		mainb(0);
	}

	private static void mainb(int i) {
		i++;
		System.out.println("----------" + i);
		if (i < 10) {
			mainb(i);
		} else {
			return;
		}

	}
}
