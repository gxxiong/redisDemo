package com.xgx.web;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xgx.mapper.GoodsMapper;
import com.xgx.mapper.OrderInfoMapper;
import com.xgx.pojo.Goods;
import com.xgx.pojo.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("datasource")
public class DatasourceController {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    //商品详情 参数:商品id
    @GetMapping("/goodsinfo/{goodsId}")
    public Goods goodsInfo(@PathVariable("goodsId") Long goodsId) {
        Goods goods = goodsMapper.selectGoods(goodsId);
        return goods;
    }

    //订单详情 参数：订单id
    @GetMapping("/orderinfo/{orderId}")
    @DS("orderdb")
    public OrderInfo orderInfo(@PathVariable("orderId") Long orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfo(orderId);
        return orderInfo;
    }

}
