package com.xgx.mapper;

import com.xgx.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper {
    Goods selectGoods(Long goodsId);
}