package com.hehe.crawl.service.job;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.framework.queue.AbsBlockQueueJob;
import com.hehe.crawl.service.adapter.BaiduSEOAdapter;
import com.hehe.crawl.service.vo.SeoTask;

@Component
public class SeoQueueJob extends AbsBlockQueueJob<SeoTask> {
	BaiduSEOAdapter adapter = new BaiduSEOAdapter();
	public SeoQueueJob(@Value(value="20") int maxThreadCount,@Value("300000")int timeout){
		super(maxThreadCount,timeout);
	}
	@Override
	public void doQueue(SeoTask task) {
		adapter.fetch(task);
	}

}