package com.xgx.mapper;

import com.xgx.pojo.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper {
    OrderInfo selectOrderInfo(Long orderId);
}