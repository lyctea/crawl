package com.hehe.crawl.configs.kafka;

import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

import com.gargoylesoftware.htmlunit.fetch.vo.ITask;

public class TaskSerializer implements Serializer<ITask> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] serialize(String topic, ITask data) {
		return SerializationUtils.serialize(data);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}