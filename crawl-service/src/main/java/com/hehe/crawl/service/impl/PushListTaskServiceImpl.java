package com.hehe.crawl.service.impl;

import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.framework.spring.PropertiesUtil;
import com.framework.spring.SpringContextUtil;
import com.hehe.crawl.service.PushTaskService;
import com.hehe.crawl.service.vo.IpoListTask;
import com.hehe.crawl.service.vo.SeoTask;
import com.hehe.crawl.service.zk.ZKConfig;
import com.hehe.crawl.service.zk.ZkWatcher;

@Service("listTaskService")
public class PushListTaskServiceImpl implements PushTaskService {

	@Autowired
	KafkaTemplate kafkaTemplate ;

	
	@Override
	public void push(IpoListTask task) {
		String topic = PropertiesUtil.getProperty("kafka.listTopic");
		kafkaTemplate.send(topic, task);
	}
	

	@Override
	public void push(SeoTask task) {
		String topic = PropertiesUtil.getProperty("kafka.detailTopic");
		kafkaTemplate.send(topic, task);
		
	}
	
	
	
	public void cronRun(){
		IpoListTask publishHask  = new IpoListTask();
		IpoListTask noPublishHask  = new IpoListTask();
		noPublishHask.setPublish(false);
		String topic = PropertiesUtil.getProperty("kafka.listTopic");
		
		ZkWatcher watcher = SpringContextUtil.getBean(ZkWatcher.class);
		 try {
			List<String> list = watcher.getZk().getChildren(ZKConfig.APP_PATH, true);
			if(CollectionUtils.isEmpty(list)){
				throw new Exception ("没有  app");
			}
			int i = new Random().nextInt(list.size());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		kafkaTemplate.send(topic, publishHask);
		kafkaTemplate.send(topic, noPublishHask);
	}



}