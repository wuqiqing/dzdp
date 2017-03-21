package test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class exRunable implements Runnable {
	String url="";
	public exRunable(String url) {
      this.url=url;
	}
	public Document getDocument(String str){//创建Jsoup将String类型解析成Document类型的方法
		return Jsoup.parse(str);
	}
	@Override
	public void run() {
		this.getResult(url);
	}
	private void getResult(String url) {
		
		//定义shop，shop表示你搜索到的商铺信息（包括商铺名称、星级、点评数）
		 String shop=null;
		//定义shoplist，为了存放搜索到的所有商铺信息
		List<String> shoplist=new ArrayList<String>();
		HttpClient httpclient=new HttpClient();
		String content="";
		String mark="";
		GetMethod getMethod=new GetMethod(url);
		getMethod.setRequestHeader("Accept", "text/html");
		getMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		getMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
		try {
			Thread.currentThread().sleep(2000);
			int status=httpclient.executeMethod(getMethod);
			if(status==HttpStatus.SC_OK){
				String html=getMethod.getResponseBodyAsString();
				Document doc=getDocument(html);
				System.out.println("--------------------"+Thread.currentThread().getName()+"--"+status);
				//获得商铺信息所在的块
				Elements elements1=doc.select("[id=shop-all-list]").select("ul").select("li");
				if(elements1.size()!=0){//判断商铺数量是否为零
					for(Element element1:elements1){//遍历获得该页面所有商铺
						//获得店铺名
						Elements shopnamele=element1.select("h4");
						String shopname=shopnamele.text();
						if(shopname==null||shopname==""||shopname.equals("")){
							continue;
						}
						//获得商铺星级
						Elements xingji_ele=element1.select("[class=comment]");
						Elements xingji_el=xingji_ele.select("span");
						String xingji=xingji_el.attr("title");
						
						//获得价格
						Elements priceE = xingji_ele.select("[class=mean-price]").select("b");
						String price=priceE.text();
						//获得点评数
						Elements pinglunNUM=element1.select("[class=review-num][target=_blank]");
						Elements dianpingNum=pinglunNUM.select("b");
						String dianingNO=dianpingNum.text();
						//判断是否有点评
						if(dianingNO==""||dianingNO.equals("")||dianingNO.equals(null)||dianingNO==null){
							dianingNO="0"+"条点评";
						}else{
							dianingNO=dianpingNum.get(0).text()+"条点评";
						}
						//获得商铺Url
						Elements shopUrl=element1.select("[class=mean-price]");
						String shopurl="http://www.dianping.com"+shopUrl.attr("href");
						
						//将商铺信息组合成一个新的字符串，为了后面获得所有的商铺信息
						shop=shopname+"#"+xingji+"#"+dianingNO+"#"+shopurl;
						shoplist.add(shop);
						content += shop+" @ " + shopname+" @ " + xingji+" @ " + price+" @ "+ dianingNO+" @ " + shopurl+"\r\n";
						Out.WriteFileExample(content, "E:\\content");
					}
				}
				//获得点评数所在的class块
				/*Elements elements2=doc.select("[class=\"\"]");
				if(elements2.size()!=0){
					for(Element element2:elements2){
						Elements shopnamele=element2.select("h4");
						String shopname=shopnamele.text();
						if(shopname==null||shopname==""||shopname.equals("")){
							continue;
						}
						
						Elements xingji_ele=element2.select("[class=comment]");
						Elements xingji_el=xingji_ele.select("span");
						String xingji=xingji_el.attr("title");
						
						//获得点评数
						Elements pinglunNUM=element2.select("[class=review-num][target=_blank]");
						Elements dianpingNum=pinglunNUM.select("b");
						String dianingNO=dianpingNum.text();
						if(dianingNO==""||dianingNO.equals("")||dianingNO.equals(null)||dianingNO==null){
							dianingNO="0"+"条点评";
						}else{
							dianingNO=dianpingNum.get(0).text()+"条点评";
						}
						
						Elements shopUrl=element2.select("[class=mean-price]");
						String shopurl="http://www.dianping.com"+shopUrl.attr("href");
						
						shop=shopname+"#"+xingji+"#"+dianingNO+"#"+shopurl;
						shoplist.add(shop);
						mark += shop+" @ " + shopname+" @ " + xingji+" @ " + dianingNO+" @ " + shopurl;
						System.out.println(shop);
						System.out.println(shop.split("#").length);
						System.out.println(shopname);
						System.out.println(xingji);
						System.out.println(dianingNO);
						System.out.println(shopurl);
						System.out.println("----------------------------------------------------------");
					}
				}*/
			
				Out.WriteFileExample(mark, "E:\\mark");
				
			/*	Elements elements3=doc.select("[class=PageLink]");
				String maxPageNum=elements3.get(elements3.size()-1).attr("title");
				Integer maxpageNum=new Integer(maxPageNum);
				//System.out.println(maxpageNum);
				//判断是否已经是最后一个页码，若是则跳出结束方法；若否，则继续执行方法
				if(i<=maxpageNum){
					i++;
					System.out.println();
					System.out.println("**********************p"+i+"*************************");
					System.out.println();
					SearchKeyword();
					
				}else{
					return;
				}*/
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			getMethod.releaseConnection();
		}
 }
}
