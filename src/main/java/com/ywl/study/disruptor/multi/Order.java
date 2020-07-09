package com.ywl.study.disruptor.multi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {
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

}
