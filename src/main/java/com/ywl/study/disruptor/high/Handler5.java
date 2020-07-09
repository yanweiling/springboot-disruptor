package com.ywl.study.disruptor.high;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Handler5 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        log.info("handler 5:GET PRICE:{}",event.getPrice());
        event.setPrice(event.getPrice()+3.0);
    }
}
