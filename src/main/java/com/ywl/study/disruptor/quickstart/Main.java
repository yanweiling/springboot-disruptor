package com.ywl.study.disruptor.quickstart;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        //参数准备工作
        OrderEventFactory orderEventFactory=new OrderEventFactory();
        int ringBufferSize=1024*1024;
        ExecutorService executor=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


        /**
        1.消息工厂对象
        2.容器大小
        3.线程池 实际上线代码，线程池要求自定义，并且重写拒绝策略
        4.生产消费类型 单生产者、多生产者
        5.等待策略
         */
        //1.实例化一个Diruptor对象,类似于ArrayBlockQueue，是一个有界容器
        //可以将Disruptor看成一个组件，用来传递数据的，泛型中定义了要传递对象的类型
        Disruptor<OrderEvent> disruptor=new Disruptor<>(orderEventFactory,ringBufferSize,executor, ProducerType.SINGLE,new BlockingWaitStrategy());

        //2.监听消费者的监听
        disruptor.handleEventsWith(new OrderEventHandler());
        //3. 启动disruptor
        disruptor.start();

        //4.获得实际存储数据的容器RingBuffer
        RingBuffer<OrderEvent> ringBuffer=disruptor.getRingBuffer();

        OrderEventProducer producer=new OrderEventProducer(ringBuffer);

        ByteBuffer bb=ByteBuffer.allocate(8);

        for(long i=0;i<100;i++){
            bb.putLong(0,i);
            producer.sendData(bb);
        }

        disruptor.shutdown();
        executor.shutdown();
    }
}
