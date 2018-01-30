package com.hehe.crawl.service.vo;

import com.gargoylesoftware.htmlunit.fetch.vo.ITask;

public class IpoListTask implements ITask {

/*	public IpoListTask(){
		this.setTaskClass(Ipo123ListAdapter.class);
	}*/
	/**
	 * 
	 */
	private static final long serialVersionUID = 379406169675783233L;
	Integer page = 0;
	
	boolean isPublish = true; 

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public boolean isPublish() {
		return isPublish;
	}

	public void setPublish(boolean isPublish) {
		this.isPublish = isPublish;
	}

}