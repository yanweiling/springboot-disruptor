package com.ywl.study.disruptor.multi;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //创建RingBuffer
        RingBuffer<Order> ringBuffer=RingBuffer.create(ProducerType.MULTI, new EventFactory<Order>() {
            @Override
            public Order newInstance() {
                return new Order();
            }
        }, 1024*1024,new YieldingWaitStrategy());
        //2.通过RingBuffer创建一个屏障
        SequenceBarrier sequenceBarrier=ringBuffer.newBarrier();

        //3.构建多消费者
        Consumer[] consumers=new Consumer[10];
        for(int i=0;i<consumers.length;i++){
            consumers[i]=new Consumer("C"+i);
        }
        //4.构建多消费者工作池
        WorkerPool<Order> workerPool=new WorkerPool<Order>(ringBuffer,sequenceBarrier,new EventExceptionHandler(),consumers);
        //5.设置多个消费者的sequence序号用于单独统计消费进度，并且设置到RingBuffer中
        //这样RingBuffer才能知道消费者是否消费完某个event事件，如果该事件没有消费完，这生产者无法将新增的event放入到没有消费完的event所在的槽中，更没有办法往后续的槽内新增event
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        //6.启动workpool  实际开发的时候，线程池要自定义
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));//由这个线程池来执行workProcessor中的run方法

        //-----
        final CountDownLatch latch=new CountDownLatch(1);
        for(int i=0;i<100;i++){
          Producer producer=new Producer(ringBuffer);
          new Thread(new Runnable() {
              @Override
              public void run() {
                  try {
                      latch.await();
                  }catch (Exception e){

                  }
                  for(int j=0;j<100;j++){
                      producer.sendData(UUID.randomUUID().toString());
                  }
              }
          }).start();
        }
        Thread.sleep(2000);
        log.info("------线程创建完毕，开始生产数据--------");
        latch.countDown();
        Thread.sleep(10000);
        for(int i=0;i<10;i++){
            log.info("第{}个消费者处理的消费总数:{}",i+1,consumers[i].getCount());
        }



    }

    static class EventExceptionHandler implements ExceptionHandler<Order>{

        /**
         * 消费的时候处理异常
         * @param throwable
         * @param l
         * @param order
         */
        @Override
        public void handleEventException(Throwable throwable, long l, Order order) {

        }

        /**
         * 启动的时候异常
         * @param throwable
         */
        @Override
        public void handleOnStartException(Throwable throwable) {

        }

        /**
         * 停止的时候异常
         * @param throwable
         */
        @Override
        public void handleOnShutdownException(Throwable throwable) {

        }
    }
}
