package com.hehe.crawl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(value="com.hehe.crawl,com.framework.spring")
//@EnableTransactionManagement
@EnableScheduling
public class CrawlBootstrap {
	
    public static void main(String[] args) {
        SpringApplication.run(CrawlBootstrap.class, args);
    }
}