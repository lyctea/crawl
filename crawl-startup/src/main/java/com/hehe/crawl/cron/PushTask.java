package com.hehe.crawl.cron;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.framework.utils.DateUtil;
import com.framework.utils.NumUtil;
import com.gargoylesoftware.htmlunit.fetch.ICrawlWorker;
import com.hehe.crawl.service.adapter.BaiduSEOAdapter;
import com.hehe.crawl.service.vo.FetchSeoVo;
import com.hehe.crawl.service.vo.SeoTask;

@Component
public class PushTask {

	ExecutorService es = Executors.newFixedThreadPool(20, new ThreadFactory() {
		AtomicInteger atomic = new AtomicInteger();
		@Override
		public Thread newThread(Runnable r) {
			Thread th = new Thread(r, "Task:"+ this.atomic.getAndIncrement());
			th.setDaemon(true);
			return th;
		}
	});
   BaiduSEOAdapter adapter = new BaiduSEOAdapter();
  int task = 0;
  int totalTask = 0;
    @Scheduled(fixedRate = 1000*60)   
    public void run(){
    	
    	Integer nowHour = NumUtil.intValue(DateUtil.getNowHour());
    	Integer initTaskCount = 100;
    	Random random = new Random();
    	int threshold = 100;
    	
    	if(nowHour<7){
    		initTaskCount = 50;
    		threshold = 50;
    	}
    	Integer taskCount = initTaskCount + random.nextInt(threshold);
    	totalTask = totalTask + taskCount;
    	System.out.println("任务："+(++task)+";任务数"+taskCount+";执行数："+taskCount);
    	SEORunnable command = new SEORunnable(adapter,new SeoTask());
    	for(int i=0;i<taskCount;i++){
    		es.execute(command );
    	}
		
    }
    
  /*  public static void main(String[] args) {
    	ExecutorService es = Executors.newFixedThreadPool(20, new ThreadFactory() {
    		AtomicInteger atomic = new AtomicInteger();
    		@Override
    		public Thread newThread(Runnable r) {
    			Thread th = new Thread(r, "Task:"+ this.atomic.getAndIncrement());
    			th.setDaemon(true);
    			return th;
    		}
    	});
       BaiduSEOAdapter adapter = new BaiduSEOAdapter();
       SEORunnable command = new SEORunnable(adapter,new SeoTask());
    	for(int i=0;i<100;i++){
    		 es.execute(command);
    	}
    	
    	try {  
            // 向学生传达“问题解答完毕后请举手示意！”  
          //  es.shutdown();  
       
            // 向学生传达“XX分之内解答不完的问题全部带回去作为课后作业！”后老师等待学生答题  
            // (所有的任务都结束的时候，返回TRUE)  
            if(!es.awaitTermination(2000, TimeUnit.MILLISECONDS)){  
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。  
                //es.shutdownNow();  
            }  
        } catch (InterruptedException e) {  
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。  
            System.out.println("awaitTermination interrupted: " + e);  
            es.shutdownNow();  
        }
    	for(int i=0;i<100;i++){
   		 es.execute(command);
    	}
    	try {
			new CountDownLatch(1).await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}

class SEORunnable implements Runnable{
	AtomicInteger  a = new AtomicInteger(1);
	SeoTask task ; 
	ICrawlWorker<SeoTask, FetchSeoVo> worker;
	SEORunnable(ICrawlWorker<SeoTask, FetchSeoVo> worker,SeoTask task){
		this.task = task;
		this.worker = worker;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		/*System.out.println("thread:"+a.getAndIncrement());
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try{
		this.worker.fetch(task);
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
}