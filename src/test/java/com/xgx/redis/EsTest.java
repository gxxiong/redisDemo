package com.xgx.redis;

import com.alibaba.fastjson.JSON;
import com.xgx.pojo.User;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class EsTest {

    private RestHighLevelClient client;


    @Before
    public void init() {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
        );
    }


    @Test
    public void addDoc() {
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setName("熊高祥");
            user.setSex("男");
            user.setUserId(123);
            String json = JSON.toJSONString(user);
            IndexRequest indexRequest = new IndexRequest("xgx", "test", i + "").source(json, XContentType.JSON);
            try {
                IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                System.out.println("响应结果" + indexResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void delDoc() {
        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest("xgx", "test", "0"));
        request.add(new DeleteRequest("xgx", "test", "1"));
        try {
            BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
            System.out.println("响应结果" + bulk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
