package com.hehe.crawl.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.fetch.vo.IpAgent;
import com.hehe.crawl.service.ProxyIpService;
import com.hehe.crawl.service.vo.IpRequestVO;

@Service

public class ProxyIpServiceImpl implements ProxyIpService {

	RestTemplate restTemplate = null;
	
//	String url = "http://112.74.17.159:8081/proxyip-web/proxyipProvide/getProxyip";	
	String url = "http://112.74.17.159:8081/proxyip-web/proxyipProvide/getProxyip";
	
	
	
	
	public ProxyIpServiceImpl(){
		initTemplate();
	}
	
	private synchronized void initTemplate() {
		if (this.restTemplate == null) {
			RestTemplate restTemplate = new RestTemplate();

			StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
			MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.setDateFormat(sdf);
			jacksonConverter.setObjectMapper(objectMapper);
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(jacksonConverter);
			messageConverters.add(stringConverter);
			restTemplate.setMessageConverters(messageConverters);
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//			requestFactory.setConnectTimeout(timeout);
//			requestFactory.setReadTimeout(timeout);
			restTemplate.setRequestFactory(requestFactory);
			// 拦截器
//			restTemplate.getInterceptors().add(new ClientHttpRequestLogInterceptor());
			this.restTemplate = restTemplate;
		}
	}
	
	@Override
	public List<IpAgent> getProxyip(Integer count) {
		
		LocalIpEntity entity = null;
		try {
			IpRequestVO ipvo = new IpRequestVO("GN");
			ipvo.setCount(500);
			entity = restTemplate.getForObject(url+this.getURLParam(ipvo), LocalIpEntity.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			System.out.println("!-------------------------------------------------------");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("2-------------------------------------------------------");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		 ResponseEntity<LocalIpEntity<IpAgent>> entity = restTemplate.exchange(url, HttpMethod.GET, null, typeRef,ipvo);
//		 LocalIpEntity<IpAgent> ipvo = entity.getBody();
		 return entity==null?null:entity.getData();
	}

	@Override
	public List<IpAgent> getAnonymousIp(Integer count) {
		
		IpRequestVO anonymousvo = new IpRequestVO("HA");
		anonymousvo.setCount(500);
		LocalIpEntity entity = restTemplate.getForObject(url+this.getURLParam(anonymousvo), LocalIpEntity.class);
		return entity.getData();
	}

	@Override
	public void feedback(IpAgent ip) {
		// TODO Auto-generated method stub
		
	}
	
	private String getURLParam(Object obj){
		StringBuffer params = new StringBuffer("");
		try {
			Map<String, String> map = BeanUtils.describe(obj);
			if(map==null||map.size()==0){
				return params.toString();
			}
			params.append("?");
			Set<String> set =map.keySet();
			boolean bool = false;
			for(String key : set){
				if(StringUtils.isEmpty(map.get(key))||StringUtils.equalsIgnoreCase(key, "class")){
					continue;
				}
				bool = true;
				params.append(key).append("=").append(map.get(key)).append("&");
			}
			if(!bool){
				return "";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return StringUtils.substringBeforeLast(params.toString(), "&");
	}
	

	
	public static void main(String[] args) {
		List list = new ProxyIpServiceImpl().getProxyip(10);
		System.out.println(list.size());
	}

}
class LocalIpEntity{
	List<IpAgent> data = null;

	public List<IpAgent> getData() {
		return data;
	}

	public void setData(List<IpAgent> data) {
		this.data = data;
	}
	
}