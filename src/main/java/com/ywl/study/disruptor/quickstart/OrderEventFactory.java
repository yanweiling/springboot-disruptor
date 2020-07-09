package com.ywl.study.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;

/**
 * 订单工厂
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {

    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();//返回空的event
    }
}
