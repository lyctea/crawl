package com.hehe.crawl.configs.mybatis;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.framework.spring.BootPropertyUtil;

//@Configuration
public class DataSourceLoader  {
	@Autowired
	BootPropertyUtil propertyUtil ;
	@Bean
	@Primary
//	@ConfigurationProperties(prefix="shard0.datasource")
    public DataSource datashard0() {
		DataSource datasource = new  com.alibaba.druid.pool.DruidDataSource();
		Map<String,String> props = propertyUtil.keyValue("parent.datasource");
		initDataSource(datasource, props);
		Map<String,String> shard0props = propertyUtil.keyValue("shard0.datasource");
		initDataSource(datasource, shard0props);
		return datasource;
    }


	private void initDataSource(DataSource datasource, Map<String, String> props) {
		Set<String>keys = props.keySet();
		for(String key:keys){
//			datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
			try {
				org.apache.commons.beanutils.BeanUtils.setProperty(datasource, key, props.get(key));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

    
    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("datashard0")DataSource ds) throws Exception {
      SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
      fb.setDataSource(ds);// 指定数据源(这个必须有，否则报错)
      // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
      fb.setTypeAliasesPackage(propertyUtil.getProperty("batis.typeAliasesPackage"));// 指定基包
      fb.setMapperLocations(
          new PathMatchingResourcePatternResolver()
          .getResources(propertyUtil.getProperty("batis.mapperLocations")));//
      return fb.getObject();
    }
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate2(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("datashard0")DataSource dataSource) throws Exception {
      return new DataSourceTransactionManager(dataSource);
    }
    
}