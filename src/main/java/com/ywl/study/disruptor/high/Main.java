package com.ywl.study.disruptor.high;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //构建一个线程池用来提交任务
        ExecutorService es1=Executors.newFixedThreadPool(4);
        //线程池数量应该>=handler个数，有多少个线程数，表示最多支持多少个handler执行，如果线程数4，handler个数是5，则只会有4个handler执行
        //1.构建disruptor
        ExecutorService es2=Executors.newFixedThreadPool(5);
        Disruptor<Trade> disruptor=new Disruptor<Trade>(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        },1024*1024,es2 , ProducerType.SINGLE,new BusySpinWaitStrategy());

        //2.把消费者设置到disruptor中
        //2.1.串行操作
        /**
        disruptor.handleEventsWith(new Handler1())
                .handleEventsWith(new Handler2())
                .handleEventsWith(new Handler3());
         */
        //2.2并行操作
//        disruptor.handleEventsWith(new Handler1());
//        disruptor.handleEventsWith(new Handler2());
//        disruptor.handleEventsWith(new Handler3());
        //2.3并行操作
       // disruptor.handleEventsWith(new Handler1(),new Handler2(),new Handler3());
        //2.4 菱形操作(1)
//        disruptor.handleEventsWith(new Handler1(),new Handler2()).handleEventsWith(new Handler3());
        //2.4 菱形操作(2)
//        EventHandlerGroup<Trade> handlerGroup=disruptor.handleEventsWith(new Handler1(),new Handler2());
//        handlerGroup.then(new Handler3());

         Handler1 h1=new Handler1();
         Handler2 h2=new Handler2();
         Handler3 h3=new Handler3();
         Handler4 h4=new Handler4();
         Handler5 h5=new Handler5();

         //h1->h2
         //h4->h5
        //都执行完，汇总到h3
        disruptor.handleEventsWith(h1,h4);
        disruptor.after(h1).handleEventsWith(h2);
        disruptor.after(h4).handleEventsWith(h5);
        disruptor.after(h2,h5).handleEventsWith(h3);


        //3.启动disruptor
        RingBuffer<Trade> ringBuffer=disruptor.start();

        CountDownLatch latch=new CountDownLatch(1);
        long beginTime=System.currentTimeMillis();
        es1.submit(new TradePublisher(latch,disruptor));
        latch.await();
        es1.shutdown();
        es2.shutdown();
        disruptor.shutdown();
        log.info("总耗时:"+Long.valueOf(System.currentTimeMillis()-beginTime));

    }
}
