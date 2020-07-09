package com.ywl.study.disruptor.high;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import sun.java2d.SurfaceDataProxy;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TradePublisher implements Runnable {
    private CountDownLatch latch;
    private Disruptor<Trade> disruptor;
    private static final int PUBLISH_COUNT=1;
    public TradePublisher(CountDownLatch latch, Disruptor<Trade> disruptor) {
        this.latch=latch;
        this.disruptor=disruptor;
    }

    @Override
    public void run() {
        TradeEventTranslator translator=new TradeEventTranslator();
        //新的提交任务的方式
        for(int i=0;i<PUBLISH_COUNT;i++){
            disruptor.publishEvent(translator);
        }
        latch.countDown();

    }
}

class TradeEventTranslator implements EventTranslator<Trade>{
    private Random random=new Random();


    //将RingBuffer中的空的event，和该event的sequence作为参数返回
    @Override
    public void translateTo(Trade event, long sequence) {
        this.generateTrade(event);

    }

    private void generateTrade(Trade event) {
        event.setPrice(random.nextDouble()*9999);
    }
}