package com.hehe.crawl.configs.kafka;

import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import com.gargoylesoftware.htmlunit.fetch.vo.ITask;

public class TaskDeserializer implements Deserializer<ITask> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ITask deserialize(String topic, byte[] data) {
		return SerializationUtils.deserialize(data);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}


}