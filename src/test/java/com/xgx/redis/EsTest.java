package com.xgx.redis;

import com.alibaba.fastjson.JSON;
import com.xgx.pojo.FileManagement;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class EsTest {

    private RestHighLevelClient client;


    @Before
    public void init() {
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.8.227", 9200, "http"))
        );
    }


    @Test
    public void addDoc() {
        for (int i = 0; i < 3; i++) {
            FileManagement fileManagement = new FileManagement();
            fileManagement.setFileName("熊高祥");
            fileManagement.setId((long) i);
            fileManagement.setUserId(123L);
            String json = JSON.toJSONString(fileManagement);
            IndexRequest indexRequest = new IndexRequest("xgx", "_doc", i + "").source(json, XContentType.JSON);
            try {
                IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                System.out.println("响应结果" + indexResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void createIndex() throws IOException {
        Settings.Builder builder = Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 1);
        CreateIndexRequest request = new CreateIndexRequest("pitp-file-management-index").settings(builder);
        request.mapping(generateBuilder());
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
    }

    private XContentBuilder generateBuilder() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject("completeDoc");
        {
            builder.startObject("properties");
            {
                builder.startObject("id");
                {
                    builder.field("type", "long");
                }
                builder.endObject();
                builder.startObject("pId");
                {
                    builder.field("type", "long");
                }
                builder.endObject();
                builder.startObject("fileName");
                {
                    builder.field("type", "text");
                    builder.field("analyzer", "index_ansj");
                    builder.field("search_analyzer", "query_ansj");
                }
                builder.endObject();
                builder.startObject("createUser");
                {
                    builder.field("type", "long");
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        return builder;
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
