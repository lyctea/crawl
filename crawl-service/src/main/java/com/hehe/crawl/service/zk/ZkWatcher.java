package com.hehe.crawl.service.zk;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.springframework.stereotype.Component;

import com.framework.spring.PropertiesUtil;
import com.framework.utils.NumUtil;

//@Component
/*@PropertySource("${locations}/zookeeper.properties")
@ConfigurationProperties(prefix="zookeeper")  */
public class ZkWatcher implements Watcher{
	private CountDownLatch connectedLatch = null;
	private String path = ZKConfig.APP_PATH;
	private final static String uuid = UUID.randomUUID().toString();
	ZooKeeper zk = null;
	
	
	public  ZkWatcher(){
		init ();
	}
	
	private ZooKeeper init (){
		if(zk==null){
			try {
				getZk();
				if(zk.exists(path, false)==null){
					zk.create(path, uuid.getBytes(),  Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				createAction();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return zk;
	}
	

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected&&connectedLatch!=null ) { 
			 //zookeeper链接成功，释放 ：waitUntilConnected
              connectedLatch.countDown();  
        } 
		if(StringUtils.isNotEmpty(path)){
			try {
				List<String> list = zk.getChildren(path, true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

//		System.out.println("process:"+event.getPath()+";"+event.getState()+";"+event.getType());
	}
	
	public void createAction(){
		String ip;
		try {
			ip = InetAddress.getLocalHost().getLocalHost().getHostAddress();
			String appPath = path+"/"+ip+"_";
			zk.getChildren(path, true);
			zk.create(appPath, ip.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/** 
	 * @Title:        waitUntilConnected 
	 * @Description:  等待zooKeeper连接成功 
	 * @param:        zooKeeper    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午5:08:10 
	 */
	private  void waitUntilConnected() {  
	        if (States.CONNECTING == zk.getState()) {  
	            try {  
	                connectedLatch.await();  
	            } catch (InterruptedException e) {  
	                throw new IllegalStateException(e);  
	            }  
	        }  
	}  
	 /** 
	 * @Title:        newZk 
	 * @Description:  新建zookeeper链接对象 
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午5:08:30 
	 */
	public ZooKeeper getZk() throws IOException{
		if(zk==null){
			 connectedLatch = new CountDownLatch(1);
			 int sessionTimeout = NumUtil.intValue(PropertiesUtil.getProperty("zookeeper.sessionTimeout"));
			 String connect = PropertiesUtil.getProperty("zookeeper.connect");
			 zk = new ZooKeeper(connect , sessionTimeout, this);
			 waitUntilConnected();
		}
		return zk;
	 }
}