package com.xgx.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderInfo {

    //订单id
    Long orderId;

    //订单编号
    private String orderSn;

    //下单时间
    private String orderTime;

    //订单状态
    int orderStatus;

    //订单状态
    int userId;

    //订单价格
    private BigDecimal price;

}
