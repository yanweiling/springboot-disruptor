package com.ywl.study.disruptor.quickstart;


import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 这里写消费者逻辑
 * 事件驱动模型
 */
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
        log.info("消费者:{}",orderEvent.getValue());

    }
}
