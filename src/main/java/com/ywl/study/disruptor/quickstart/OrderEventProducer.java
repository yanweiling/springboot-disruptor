package com.ywl.study.disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class OrderEventProducer {
    private RingBuffer<OrderEvent> ringBuffer;
    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer){
      this.ringBuffer=ringBuffer;
    }

    //投递数据
    public void sendData(ByteBuffer data){
        //1.生产者在发送消息的时候，首先需要从ringBuffer中获得一个可用的序号
        long sequence=ringBuffer.next();
        try{
            //2.根据这个序号找到具体的"OrderEvent"元素:此时获取的orderEvent的元素是没有被填充的对象
            OrderEvent event=ringBuffer.get(sequence);
            //3.进行实际的赋值
            event.setValue(data.getLong(0));
        }finally {
            //提交发布操作
            ringBuffer.publish(sequence);
        }

    }
}
