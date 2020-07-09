package com.ywl.study.disruptor.quickstart;

import lombok.Data;

/**
 * 订单
 * 由于disruptor走的是纯内存的，不会读取io流，所以对象可以不继承Serializable
 */
@Data
public class OrderEvent {
    /*
    订单价格
     */
    private long value;
}
