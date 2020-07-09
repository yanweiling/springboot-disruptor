package com.ywl.study.disruptor.high;


import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Handler1 implements EventHandler<Trade>, WorkHandler<Trade> {

    //重写EventHandler
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);

    }

    //重写WorkHandler
    @Override
    public void onEvent(Trade event) throws Exception {
        log.info("handler 1: SET NAME");
        event.setName("H1");
        Thread.sleep(1000);

    }
}
