package com.ywl.study.disruptor.high;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: yanwlb
 * @Date: 2020/7/8 15:44
 * @Description: 交易event
 * @History: //修改记录
 */
@Data
@NoArgsConstructor
public class Trade {
    /**
     * 交易id
     */
    private String id;
    /**
     * 交易名称
     */
    private String name;
    /**
     * 交易价格
     */
    private double price;
    private AtomicInteger count=new AtomicInteger(0);
}
