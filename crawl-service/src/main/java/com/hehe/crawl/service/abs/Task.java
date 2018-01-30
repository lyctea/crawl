package com.hehe.crawl.service.abs;

import com.gargoylesoftware.htmlunit.fetch.vo.ITask;

public class Task implements ITask {

	Class taskClass = null;

	public Class getTaskClass() {
		return taskClass;
	}

	public void setTaskClass(Class taskClass) {
		this.taskClass = taskClass;
	}
	
}