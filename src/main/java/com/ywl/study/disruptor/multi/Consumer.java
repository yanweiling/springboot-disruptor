package com.ywl.study.disruptor.multi;

import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Consumer implements WorkHandler<Order> {
    private String consumerId;

    private  AtomicInteger count=new AtomicInteger(0);

    private Random random=new Random();
    @Override
    public void onEvent(Order event) throws Exception {
        Thread.sleep(1*random.nextInt(5));
        log.info("当前消费者:{},消费信息:{}",this.consumerId,event.getId());
        count.incrementAndGet();

    }

    public Consumer(String consumerId){
        this.consumerId=consumerId;
    }

    public  AtomicInteger getCount(){
        return count;
    }
}
