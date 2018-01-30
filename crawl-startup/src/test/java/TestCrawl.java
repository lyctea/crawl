import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hehe.crawl.service.adapter.BaiduSEOAdapter;
import com.hehe.crawl.service.vo.SeoTask;

@RunWith(SpringJUnit4ClassRunner.class)
@Configuration 
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(value="com.hehe.crawl,com.framework.spring")
@PropertySource("application.properties")
public class TestCrawl {
	@Test
	public void test(){
		SeoTask task = new SeoTask();
		new BaiduSEOAdapter().fetch(task );
	}
}