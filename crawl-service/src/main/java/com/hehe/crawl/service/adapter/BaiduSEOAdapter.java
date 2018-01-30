package com.hehe.crawl.service.adapter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;

import com.framework.utils.RegUtil;
import com.gargoylesoftware.htmlunit.execption.FetchException;
import com.gargoylesoftware.htmlunit.execption.ParseHtmlException;
import com.gargoylesoftware.htmlunit.fetch.FetchWebContext;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.hehe.crawl.service.abs.AbsAdapter;
import com.hehe.crawl.service.vo.FetchSeoVo;
import com.hehe.crawl.service.vo.SeoTask;

public class BaiduSEOAdapter extends AbsAdapter<SeoTask, String, FetchSeoVo>  {
//https://www.baidu.com/s?word=久闻网&oq=久闻网自媒体&sa=tb&ts=764
//https://www.baidu.com/s?word=久闻网&oq=久闻网自媒体&sa=tb&ts=764
//https://www.baidu.com/s?word=久闻网&oq=久闻网自媒体&sa=tb&ts=1945
//https://www.baidu.com/s?word=久闻网&oq=久闻网自媒体&sa=tb&ts=1945
	String url = "https://www.baidu.com/";
	String jqueryUrlPression = "['|\"](https\\://www\\.baidu\\.com.+?cb\\=jQuery.+?)['|\"]";
	String baiduUrlPression = "^(https\\://www\\.baidu\\.com/s\\?((?!&oq=[^\\&]).)*)$";
	String baiduUrlPression2 = "^(https\\://www\\.baidu\\.com/s\\?.*?\\&oq\\=[^\\&].*)$";
//	String 
	 public BaiduSEOAdapter() {
		// TODO Auto-generated constructor stub
		this.init();
	}
	
	public void init(){
		super.setThreadCount(1);
		super.setConnectTimeout(50);
		super.setSocketTimeout(20000);
		super.setIsproxy(true);
		super.setThreadTimeout(10000000);
		super.setIpCount(100);
		super.setThreadExecCount(100);
	}
	
	@Override
	protected String fetchHtml(SeoTask task, FetchWebContext<String> context) throws FetchException {
		String html = null;
		try {
			//context.getFetchWebClient().addFilterUrlExpression(jqueryUrlPression);
			HtmlPage page = context.getFetchWebClient().getPage(url);
//			System.out.println(page.asXml());  
//			FileWriter writer1 = new FileWriter("baidu1.html");  
//			writer1.write(page.asXml());  
			String firstSearchWord = "航班动态查询";
			HtmlPage page2 = this.getPage(context,page,firstSearchWord,baiduUrlPression,"baidu1.html");
//			page2 = this.getPage(context,page2,"机票比价","baidu1.html");
//	        page2 = this.getPage(context,page2,"机票比价航班不用等","baidu2.html");
			final HtmlForm form = page.getFormByName("f");  
	        final HtmlHiddenInput textField = form.getInputByName("oq"); 
	        if(StringUtils.isEmpty(textField.getValueAttribute())){
	        	textField.setValueAttribute(firstSearchWord);
	        }
	        String secondSearchWord = "航班动态查询关注航班不用等";
			page2 = this.getPage(context,page2,secondSearchWord,baiduUrlPression2,"baidu2.html");
	        html = page2.asXml();
	        Map<String, Set<String>> map = context.getFetchWebClient().getFilterUrlMap();
	        for(Set<String> urls : map.values()){
	        	if(urls!=null){
	        		for(String url : urls){
	        			System.out.println("#########"+url);
//	        			context.getFetchWebClient().getPage(url);
	        		}
	        	}
	        }
		} catch (ParseException | IOException e) {
			throw new FetchException(e);
		} 
		return html;
	}

	private void writePage(HtmlPage page,String fileName) throws IOException {
		try {
				Thread.sleep(200L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    String a = page.asXml();
		    FileWriter writer = new FileWriter(fileName);  
		    writer.write(a);
		    writer.close();
	}
	
	
	public HtmlPage getPage( FetchWebContext<String> context,HtmlPage page,String searchWord,String urlPression,String pageName) throws IOException{
		context.getFetchWebClient().addFilterUrlExpression(urlPression);
		
		final HtmlForm form = page.getFormByName("f");  
        final HtmlSubmitInput button = form.getInputByValue("百度一下");  
        final HtmlTextInput textField = form.getInputByName("wd"); 
        textField.focus();
        textField.setValueAttribute(searchWord); 
        textField.blur();
        final HtmlPage page2 = button.click();  
//        context.getFetchWebClient().waitForBackgroundJavaScript(2000);
        context.getFetchWebClient().waitFilterUrlExpression(urlPression, 10000);
        for (int i = 0; i < 20; i++) {
			
			if ((!page2.getByXPath("//div[@id='page']").isEmpty())) {
				break;
			}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
        writePage(page2,pageName);
       // context.getFetchWebClient().waitFilterUrlExpression(this.jqueryUrlPression,10000);
        return page2;
	}

	@Override
	protected boolean isFetchFail(String html, SeoTask task) {
		// TODO Auto-generated method stub
		return StringUtils.isEmpty(html)||(!html.contains("id=\"page\""));
	}

	@Override
	protected FetchSeoVo parseHtml(String html, SeoTask task) throws ParseHtmlException {
		return new FetchSeoVo();
	}
	
	
	
	
	public static void main(String[] args) {
//		String regex = "^(https\\://www\\.baidu\\.com/s\\?((?!&oq=).)*)$";
		String regex = "^(https\\://www\\.baidu\\.com/s\\?((?!&oq=[^\\&]).)*)$";
		String url1 = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&ch=&tn=baidu&bar=&wd=%E8%88%AA%E7%8F%AD%E5%8A%A8%E6%80%81%E6%9F%A5%E8%AF%A2&rn=&rsv_pq=9f65d18400005cbd&rsv_t=2e3dlaNMWUbo7mECemNBlhsmxwk0P9ZJ%2FPoaSlcRuQQ5OFuMhHt%2FnwZRXqo&rqlang=cn";
		System.out.println(RegUtil.regStr(url1, regex, 1));
	}

}