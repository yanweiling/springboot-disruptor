package com.ywl.study.disruptor.multi;

import com.lmax.disruptor.RingBuffer;

public class Producer {
    private RingBuffer<Order> ringBuffer;
    public Producer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer=ringBuffer;
    }

    public void sendData(String id){
        long sequence=ringBuffer.next();
        try{
            Order order=ringBuffer.get(sequence);
            order.setId(id);
        }finally {
            ringBuffer.publish(sequence);
        }
    }
}
