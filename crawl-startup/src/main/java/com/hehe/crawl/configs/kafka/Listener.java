package com.hehe.crawl.configs.kafka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
/*    Ipo123DetailAdapter detailAdpter = new Ipo123DetailAdapter();
    Ipo123ListAdapter listAdpter = new Ipo123ListAdapter();

    @KafkaListener(topics = {"${kafka.listTopic}"})
    public void listenIpoList(ConsumerRecord<String, IpoListTask> record) {
    	listAdpter.fetch(record.value());
    }
    
    
    @KafkaListener(topics = {"${kafka.detailTopic}"})
    public void listenIpo(ConsumerRecord<String, IpoTask> record) {
    	detailAdpter.fetch(record.value());
    }*/
}