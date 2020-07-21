package com.xgx.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EasyExcelConsumerListener<T> extends AnalysisEventListener<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EasyExcelConsumerListener.class);
    private int pageSize;
    private List<T> list;
    private Map map=new HashMap();
    private Consumer<Map> consumer;

    public EasyExcelConsumerListener(int pageSize, Consumer<Map> consumer) {
        this.pageSize = pageSize;
        this.consumer = consumer;
        list = new ArrayList<>();
    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        map.put("headMap",headMap);
        consumer.accept(map);
        map.remove("headMap");
        LOGGER.info("解析到一条头数据:{}", JSON.toJSONString(headMap));
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        list.add(data);
        map.put("content",list);
        if (list.size() >= pageSize) {
            consumer.accept(map);
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        map.put("content",list);
        consumer.accept(map);
    }
}