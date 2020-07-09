package com.ywl.study.disruptor.high;


import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class Handler2 implements EventHandler<Trade> {

    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        log.info("handler 2: SET ID");
        event.setId(UUID.randomUUID().toString());
        Thread.sleep(2000);

    }
}
