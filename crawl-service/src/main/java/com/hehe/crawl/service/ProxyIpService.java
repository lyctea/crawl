package com.hehe.crawl.service;

import java.util.List;

import com.gargoylesoftware.htmlunit.fetch.vo.IpAgent;

public interface ProxyIpService {
	
	public List<IpAgent> getProxyip(Integer count);
	
	public List<IpAgent> getAnonymousIp(Integer count);
	
	public void feedback(IpAgent ip);
}