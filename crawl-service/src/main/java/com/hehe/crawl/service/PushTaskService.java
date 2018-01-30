package com.hehe.crawl.service;

import com.hehe.crawl.service.vo.IpoListTask;
import com.hehe.crawl.service.vo.SeoTask;

public interface PushTaskService {

	
	public void push(IpoListTask task);

	public void push(SeoTask task);
	
	public void cronRun();
}