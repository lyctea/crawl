import java.io.FileWriter;
import java.io.IOException;

import com.framework.utils.RegUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.fetch.FetchWebContext;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Test {

	public static void main(String[] args) throws IOException, InterruptedException {
//		String a = " null: XMLHttpRequest GET 'https://www.baidu.com/his?wd=&from=pc_web&rf=3&hisdata=&json=1&p=3&sid=1440_12897_21088_18560_20930&req=2&csor=0&cb=jQuery11020005011185666535467_1516807119790&_=1516807119791'";
//		String g = RegUtil.regStr(a, "https\\://www\\.baidu\\.com.+?cb\\=jQuery.+?['|\"]",0);
		
//		String a = "https://www.baidu.com/s?ie=utf-8&mod=11&isbd=1&isid=FCECEA6D37978419&ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%9C%BA%E7%A5%A8%E6%AF%94%E4%BB%B7&rsv_pq=e05472a90001cce2&rsv_t=8173KoVWQ4PFZrxsr2SQU5fzdEfzPx4l2gofuKxXXHnDBfjEmxnE1OmXCnY&rqlang=cn&rsv_enter=0&rsv_sid=1430_21099_17001_20719&_ss=1&clist=&hsug=&csor=4&pstg=5&_cr1=23994";
//		
//		String g = RegUtil.regStr(a, "(https\\://www\\.baidu\\.com/s\\?.*)");
//		System.out.println(g);
//		String jqueryUrlPression = "(https\\://www\\.baidu\\.com/s\\?.*)";
		FetchWebContext context = new FetchWebContext<>();
//		context.getFetchWebClient().addFilterUrlExpression(jqueryUrlPression);
		context.setConnectTimeout(1000);
		context.setSocketTimeout(5000);
		WebClient webclient = context.getFetchWebClient();

	    // 这里是配置一下不加载css和javaScript，因为httpunit对javascript兼容性不太好

	    // 做的第一件事，去拿到这个网页，只需要调用getPage这个方法即可  
	    HtmlPage htmlpage = webclient.getPage("https://baidu.com/");  
	    System.out.println(htmlpage.asXml());
	    FileWriter writer1 = new FileWriter("baidu1.html");  
		writer1.write(htmlpage.asXml());  
	    // 根据名字得到一个表单，查看上面这个网页的源代码可以发现表单的名字叫“f”  
	    final HtmlForm form = htmlpage.getFormByName("f");  
	    // 同样道理，获取”百度一下“这个按钮  
	    final HtmlSubmitInput button = form.getInputByValue("百度一下");  
	    // 得到搜索框  
	    final HtmlTextInput textField = form.getInputByName("q1");  
	    //搜索我的id
	    textField.setValueAttribute("航班不用等");  
	    // 输入好了，我们点一下这个按钮  
	    final HtmlPage nextPage = button.click();  
	    // 我把结果转成String  
	    String result = nextPage.asXml();  
	    FileWriter writer = new FileWriter("baidu2.html");  
        writer.write(result);  
	    System.out.println(result);  //得到的是点击后的网页

	}
}
