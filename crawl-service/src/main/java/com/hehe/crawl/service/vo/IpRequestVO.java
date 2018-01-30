package com.hehe.crawl.service.vo;

public class IpRequestVO {
	
	//高匿HA
	String type;
	int count=500;
	boolean pool=true;
	public IpRequestVO(){}
	public IpRequestVO(String type){
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public boolean isPool() {
		return pool;
	}
	public void setPool(boolean pool) {
		this.pool = pool;
	}
	
}