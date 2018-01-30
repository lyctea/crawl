package com.hehe.crawl.service.zk;

import com.framework.spring.PropertiesUtil;

public class ZKConfig {

	public final static String CREATE_PATH = "CREATE_PATH";
	public final static String APP_PATH = PropertiesUtil.getProperty("zookeeper.appPath");
	
}