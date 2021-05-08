package com.xgx.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Goods {
    //商品id
    Long goodsId;

    //商品名称
    private String goodsName;

    //商品标题
    private String subject;

    //商品价格
    private BigDecimal price;

    //库存
    int stock;

}
