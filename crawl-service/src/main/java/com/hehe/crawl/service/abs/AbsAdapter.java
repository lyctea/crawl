package com.hehe.crawl.service.abs;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.framework.spring.SpringContextUtil;
import com.gargoylesoftware.htmlunit.fetch.AbsProxyIpFetcher;
import com.gargoylesoftware.htmlunit.fetch.FetchWebContext;
import com.gargoylesoftware.htmlunit.fetch.vo.FetchBaseVo;
import com.gargoylesoftware.htmlunit.fetch.vo.ITask;
import com.gargoylesoftware.htmlunit.fetch.vo.IpAgent;
import com.hehe.crawl.service.ProxyIpService;


public abstract class AbsAdapter<Task extends ITask, HtmlClass, T extends FetchBaseVo> extends AbsProxyIpFetcher<Task,HtmlClass, T> {

	protected Log logger = LogFactory.getLog(getClass());
	//默认线程数为3个线程，所以ip数最好是3的倍数
	private Integer ipCount = DEFAULT_IP_COUNT;
	/**
	 * 
	 */

	/**
	 * 外部调用方法,按照就系统逻辑实现。
	 */
	@Override
	public T fetch(Task task) {
		T data = null;
		
		HtmlClass htmlData = null;
		try {
			htmlData = fetchStrategyHtml(task);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e);
		}
		if (isFetchFail(htmlData, task)) {
			logger.info("============================最终本机抓取失败！！===============================");
			data = getTclass();
			data.setState(false);
			data.setCode("00002");
			return data;
		}
		// System.out.println("---------------------原始页面-------------------------");
		// System.out.println(htmlData);
		// System.out.println("---------------------原始页面-------------------------");
		data = parseHtml(htmlData, task);
		if (data != null && null != data.getState() && data.getState().booleanValue()) {
			// data.setMessage(task.message.toString());
			if (htmlData instanceof String) {
				data.setByteLength(((String) htmlData).length());
			} else {
				if (htmlData != null) {
					data.setByteLength(htmlData.toString().length());
				}
			}
		}
//			data.setMessage("ok");
		return data;
	}

	private T getTclass() {
		try {
			ParameterizedType pt = null;
			// 通过反射获取model的真实类型
			if(this.getClass().getName().toLowerCase().contains("imme")) {
				pt = (ParameterizedType)this.getClass().getSuperclass().getGenericSuperclass();
			} else {
				pt = (ParameterizedType) this.getClass().getGenericSuperclass();
			}
			Class<T> clazz = (Class<T>) pt.getActualTypeArguments()[2];
			// 通过反射创建model的实例
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用策略加强抓取
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 */
	private HtmlClass fetchStrategyHtml(Task task) throws Exception {
		HtmlClass resultStr = null;
		if (!this.isIsproxy()) {
			FetchWebContext<HtmlClass> context = newFetchContext();
			resultStr = this.fetchHtml(task, context);
			
			if (isFetchFail(resultStr, task)) {
				// 最终抓取失败！
				logger.info("==========================================最终本机抓取失败！！==========================================");
				return null;
			} else {
				return resultStr;
			}
		}
		
		resultStr = doThreadsCrawl(task);
	
		if (isFetchFail(resultStr, task) ) {
			// 页面错误代码30*,40*,50*,都是错误——lyq
			for (int i = 0; i < 5; i++) {// 连续抓5次，尽可能保证成功。
				resultStr = doThreadsCrawl(task);
				if (!isFetchFail(resultStr, task)) {
					break;
				}
			}
		}
		if (this.isIsproxy() && isFetchFail(resultStr, task)) {
			logger.warn("------------并发代理抓取失败---------------");
			FetchWebContext<HtmlClass> context = newFetchContext();
			resultStr = this.fetchHtml(task, context);
			if (isFetchFail(resultStr, task)) {
				// 最终抓取失败！
				logger.info(
						"==========================================最终本机抓取失败！！==========================================");
				return null;
			}
			System.out.println("本地抓取========================================================================");
			System.out.println(resultStr);
		}
//			if (isCrawFail(resultStr, task)) {
//				// 最终抓取失败！
//				logger.info(
//						"==========================================最终抓取失败！！==========================================");
//				return null;
//			}
//		}
		return resultStr;
	}

	
	
	/**
	 * 从代理ip网站抓到ip后，分二十个线程去抓取网页targetUrl
	 * 
	 * @param targetUrl
	 * @param flightCode
	 * @param maxExecCount
	 *            每个线程抓取页面的最大次数
	 */
	private HtmlClass doThreadsCrawl(Task task) {
		List<IpAgent> ips = null;
		ips = SpringContextUtil.getBean(ProxyIpService.class).getProxyip(this.getIpCount());
		try {
			//
			final String threadName = getThreadName(task, this.getThreadTimeout());
			Long start = System.currentTimeMillis();
//			System.out.println("ip size:"+(data==null?null:data.size()));
			if(ips == null || ips.size() == 0) {
				return null;
			}
			List<HtmlClass> result = startFetch(task, ips, threadName);
//			List<HtmlClass> result =.openThreads(threadName,data, task, threads, threadTimeout);
			crawlCount = crawlCount + result.size();
			System.out.println( "crawlCount:"+ crawlCount+";threadTimeout:" + this.getThreadTimeout() + ";proxyip:" + ips.size() + ";spend:"
					+ (System.currentTimeMillis() - start) + "ms;result:" + result.size() + ";isproxy:" + this.isIsproxy()
					+ "");	

			if (result != null && result.size() != 0) {
				return result.get(0);
			}
		} catch (Exception e) {
			logger.error("最终抓取失败:" + e);
		}
		return null;
	}


	
	private String getThreadName(final Task task, Long threadTimeout) {
		String infoName = null;
		final String app = this.getClass().getSimpleName() + "_" + threadTimeout + "_" + infoName;
		return app;
	}
	
	public Integer getIpCount() {
		return ipCount;
	}

	public void setIpCount(Integer ipCount) {
		this.ipCount = ipCount;
	}

}