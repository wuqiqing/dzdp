package com.main.parseHTML;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.main.access.DZDemo;
import com.main.out.excelUtils;
import com.utils.FormatUtil;

public class Process implements Runnable {
	private static Logger log = Logger.getLogger(Process.class);
	private Map<String, String> map = null;
	private LinkedBlockingQueue<Map<String, String>> queue = null;
	private String tag = null;
	private static Process instance = null;
	private String url = "";
	private int i = 0;

	public Process(Map<String, String> map, LinkedBlockingQueue queue,String tag, String url, int i) {
		super();
		this.map = map;
		this.queue = queue;
		this.tag = tag;
		this.url = url;
		this.i = i;
	}

	@Override
	public void run() {
		this.getResult(url, i);
	}

	public static Process getSingleInstance(Map<String, String> map,
			LinkedBlockingQueue queue, String tag, String url, int i) {
		if (instance == null) {
			instance = new Process(map, queue, tag, url, i);
		}
		return instance;
	}

	public void getResult(String url, int i) {
		// 定义shop，shop表示你搜索到的商铺信息（包括商铺名称、星级、点评数）
		Map<String, String> shopMap = new HashMap<String, String>();
		String shop = null;
		// 定义shoplist，为了存放搜索到的所有商铺信息
		List<String> shoplist = new ArrayList<String>();
		HttpClient httpclient = new HttpClient();
		String content = "";
		String mark = "";

		GetMethod getMethod = new GetMethod(url);
		getMethod.setRequestHeader("Accept", "text/html");
		getMethod
				.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		getMethod
				.setRequestHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
		try {
			Thread.currentThread().sleep(5);
			int status = httpclient.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				String html = getMethod.getResponseBodyAsString();
				Document doc = getDocument(html);
				Elements elements1 = doc.select("[id=shop-all-list]").select("ul").select("li");
				if (elements1.size() != 0) {// 判断商铺数量是否为零
					for (Element element1 : elements1) {// 遍历获得该页面所有商铺
						Element divTit = element1.select("[class=tit]").get(0);
						Element divCom = element1.select("[class=comment]").get(0);
						Element divAdd = element1.select("[class=tag-addr]").get(0);
						Elements spans = element1.select("div").select("[class=comment-list]").select("span");
						Element a = divTit.select("a").get(0);
						// 商户ID(shop_id)
						String shopUrl = a.attr("href");
						shopMap.put("shop_id",	shopUrl.substring(shopUrl.lastIndexOf("/") + 1));
						// 商户名(shop_name)
						String shopName = a.attr("title");
						shopMap.put("shop_name", shopName);
						// 是否可外卖(can_takeout)
						try {
							String attr = divTit.select("div").select("a").attr("class");
							String can_takeout = "iout".equals(attr) ? "y": "n";
							shopMap.put("can_takeout", can_takeout);
						} catch (Exception e) {
							exceptionManager(e, shopName, url);
						}
						
						
						// 商户级别(shop_rank)
						try {
							Elements tems = divTit.select("a").select("[class=shop-branch]");
							String shop_rank = tems != null ? tems.text() : "";
							shopMap.put("shop_rank", shop_rank);
						} catch (Exception e) {
							exceptionManager(e, shopName, url);
						}
						// 其他分店(shop-branches)
						try {
							String shop_branches = divTit.select("[class=shop-branch]").attr("href");
							shopMap.put("shop_branches", shop_branches);
						} catch (Exception e) {
							exceptionManager(e, shopName, url);
						}
						// 商户地址(shop_address)
						try {
							String shop_address = divAdd.select("[class=addr]").text();
							shopMap.put("shop_address", shop_address);
						} catch (Exception e) {
							exceptionManager(e, shopName, url);
						}
						// 人均消费()
						try {
							String shop_spend = divCom.select("a").get(1).select("b").text();
							shopMap.put("shop_spend", shop_spend);
						} catch (Exception e) {
							exceptionManager(e, shopName, url);
						}
						// 评论数(shop_comment_num)
						try {
							String shop_comment_num = divCom.select("a").get(0).select("b").text();
							shopMap.put("shop_comment_num", shop_comment_num);
						} catch (Exception e) {
							exceptionManager(e, shopName, url);
						}
						// 评分(shop_scores)
						try {
							String shop_scores = "";
							shop_scores = "口味："
									+ spans.get(1).select("b").text() + " 环境："
									+ spans.get(2).select("b").text() + " 服务："
									+ spans.get(3).select("b").text();
							shopMap.put("shop_scores", shop_scores);
						} catch (Exception e) {
							exceptionManager(e, shopName, url);
						}
						mapCopy(map, shopMap);
						getShopPage("https://www.dianping.com" + shopUrl,shopMap);
						queue.put(shopMap);
						// 页码

						System.out.println("--"+ excelUtils.sequenceNumber+++ "----商店编号："	+ shopUrl+ "  ------完成~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					   excelUtils.tempS++;
					}
				}
				Elements elements3 = doc.select("[class=PageLink]");
				String maxPageNum = elements3.get(elements3.size() - 1).attr("title");
				Integer maxpageNum = new Integer(maxPageNum);
				// 判断是否已经是最后一个页码，若是则跳出结束方法；若否，则继续执行方法
				if (i <= maxpageNum) {
					log.info("---分类：" + tag + "--------p" + i+ "----------maxNum=" + maxpageNum+" 开始执爬取下一页。。。。");
					url += "p"+ i+ "?aid=69139982%2C4733611%2C73418365%2C14721437%2C2124043%2C8551590";
					i++;
					getResult(url, i);

				} else {
					log.info("---分类：" + tag + "--------p" + i+ "----------" + "总共爬取:"+excelUtils.tempS++);
					excelUtils.tempS=1;
					return;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
	}

	private void exceptionManager(Exception e, String shopName, String url) {
		StackTraceElement[] trace = e.getStackTrace();
		String msg = "";
		for (StackTraceElement ele : trace) {
			msg += ele.toString() + "\n";
			e.printStackTrace();
		}
		log.error("------时间：  " + FormatUtil.dateFormatYMDHMS(new Date())+ " url:" + url + " 商品名：" + shopName + "出现异常" + msg);
	}

	private GetMethod getShopPage(String url, Map<String, String> rsMap) {
		GetMethod getMethod = new GetMethod(url);
		getMethod.setRequestHeader("Accept", "text/html");
		getMethod
				.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		getMethod
				.setRequestHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
		HttpClient httpclient = new HttpClient();
		try {
			Thread.currentThread().sleep(5);
			int status = httpclient.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				String html = getMethod.getResponseBodyAsString();
				Document doc = getDocument(html);
				Elements bread = doc.select("[class=breadcrumb]").select("a");
				Elements src = doc.select("script:not([src],[type])");
				Element element = src.get(src.size() - 3);
				String data = element.data().substring(element.data().indexOf("=") + 1);
				// 商户创建时间(shop_create_time)
				try{
					// 纬度（腾讯地图）(latitude)
					String latitude = "";
					// 经度（腾讯地图）(longitude)
					String longitude = "";
					if (src != null) {
						latitude = getValue("shopGlat", data, 3);
						longitude = getValue("shopGlng", data, 3);
						rsMap.put("latitude", latitude);
						rsMap.put("longitude", longitude);
					}
				}catch (Exception e) {
					exceptionManager(e, rsMap.get("shop_name"), url);
				}
				
				try{
				// 商户分类(shop_categories)
					String shop_categories = "";
					for (Element ele : bread) {
						shop_categories += ele.text() + " > ";
					}
					shop_categories = shop_categories.substring(0,shop_categories.length() - 2);
					rsMap.put("shop_categories", shop_categories);
				}catch (Exception e) {
					exceptionManager(e, rsMap.get("shop_name"), url);
				}
				
				Elements div = doc.select("[class=main]").select("div");
				// 商户电话(shop_telephone)
				try{
			   	    Elements select = div.select("[class=expand-info tel]").select("[class=item]");
				    String shop_telephone = select != null &&  select.toString().length()>0  && select.get(0) != null ? select.get(0).text() : "";
				    rsMap.put("shop_telephone", shop_telephone);
				}catch (Exception e) {
					exceptionManager(e, rsMap.get("shop_name"), url);
				}
				
				try{
				   // 营业时间(open_time)
				   String open_time = div.select("[class=info info-indent]").select("[class=item]").get(0).text();
				   rsMap.put("open_time", open_time);
				}catch (Exception e) {
					exceptionManager(e, rsMap.get("shop_name"), url);
				}
				try{
					// 商户详情(shop_views)
					Elements sel2 = div.select("[class=shop-tab-brandstory J-panel clearfix]");
					String shop_views = sel2 != null&& sel2.select("[class=J_short]") != null ? sel2.select("[class=J_short]").text() : "";
					rsMap.put("shop_views", shop_views);
				}catch (Exception e) {
					exceptionManager(e, rsMap.get("shop_name"), url);
				}
				try{
					// 评论标签(comment_tags)
					String comment_tags = "";
					Document ajaxDoc = getAjaxStr(data);
					Elements as = ajaxDoc.select("a[date-type]");
					for (Element sp : as) {
						String text = sp.text();
						int indexOf = text.indexOf("<");
						if (indexOf > 0) {
							comment_tags += text.substring(0, indexOf) + " | ";
						}
					}
					rsMap.put("comment_tags", comment_tags);
				}catch (Exception e) {
					exceptionManager(e, rsMap.get("shop_name"), url);
				}
				
				// 评论(comments)
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getMethod;
	}

	private Document getAjaxStr(String data) {
		String shopId = getValue("shopId", data, 2);
		String cityId = getValue("cityId", data, 2);
		String power = getValue("power", data, 2);
		String shopType = getValue("shopType", data, 2);
		String mainCategoryId = getValue("mainCategoryId", data, 2);
		String ajaxStr = "https://www.dianping.com/ajax/json/shopfood/wizard/reviewAllFPAjax?shopId="
				+ shopId
				+ "&cityId="
				+ cityId
				+ "&power="
				+ power
				+ "&shopType="
				+ shopType
				+ "&mainCategoryId="
				+ mainCategoryId
				+ "";
		GetMethod getMethod = new GetMethod(ajaxStr);
		getMethod.setRequestHeader("Accept", "text/html");
		getMethod
				.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		getMethod
				.setRequestHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
		HttpClient httpclient = new HttpClient();
		Document doc = null;
		try {
			Thread.currentThread().sleep(5);
			int status = httpclient.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				String html = getMethod.getResponseBodyAsString();
				doc = getDocument(html);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	public Document getDocument(String str) {// 创建Jsoup将String类型解析成Document类型的方法
		return Jsoup.parse(str);
	}

	private String getValue(String attr, String data, int s) {
		int sIndex = data.indexOf(attr);
		String str = data.substring(sIndex + s + attr.length() - 1);
		int eIndex = str.indexOf(",");
		String value = str.substring(0, eIndex + 2 - s);
		value.replace(":", "");
		if (value.startsWith("\"")) {
			value = value.substring(1);
		}

		return StringUtils.trim(value);
	}

	private void mapCopy(Map mapfrom, Map mapto) {
		for (Iterator iterator = mapto.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Integer> next = (Entry<String, Integer>) iterator.next();
			mapto.put(next.getKey(), next.getValue());
		}
	}

	public static void main(String[] args) {
		String value = "sdfgsgsfgsfgs";
		try {
			value.substring(80);
		} catch (Exception e) {
			e.printStackTrace();
			StackTraceElement[] trace = e.getStackTrace();
			for (StackTraceElement t : trace) {
				System.out.println(t.toString());
			}
		}

	}

}
