package com.xgx.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.xgx.core.restful.Result;
import com.xgx.core.restful.ResultGenerator;
import com.xgx.pojo.DownloadData;
import com.xgx.pojo.User;
import com.xgx.service.IThreadPoolService;
import com.xgx.util.EasyExcelConsumerListener;
import com.xgx.websocket.WebSocketServer;
import com.xgx.websocket.WebsocketPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WebsocketPublish websocketPublish;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private IThreadPoolService threadPoolService;

    @GetMapping("set")
    public void setOcr() {
        User user = new User();
//        user.setId(1);
        user.setName("熊高祥");
        redisTemplate.opsForValue().set("1",user,1,TimeUnit.DAYS);
    }

    @GetMapping("get")
    public void getOcr() throws Exception {
        User user = (User) redisTemplate.opsForValue().get("1");
        System.out.println(user);
//        throw new BusinessException(ResultCode.CODE_402,"11111");
    }

    @GetMapping("template")
    public void getTemplate(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(data());
    }

    @GetMapping("downloadFailedUsingJson")
    public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), DownloadData.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(data());
            int i =1/0;
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    @PostMapping("upload")
    @ResponseBody
    public String upload(MultipartFile file) throws IOException {
        Consumer consumer =new Consumer() {
            @Override
            public void accept(Object o) {
                Map map = (Map) o;
                if (map.get("headMap") != null) {
                    System.out.println(map.get("headMap"));
                }
                List<DownloadData> a = (List<DownloadData>) map.get("content");
                if (a != null) {
                    for (DownloadData downloadData : (List<DownloadData>) map.get("content")) {
                        System.out.println(downloadData);
                    }
                }
            }
        };
        EasyExcel.read(file.getInputStream(), DownloadData.class, new EasyExcelConsumerListener<>(5 ,consumer)).sheet().doRead();
        return "success";
    }

    private List<DownloadData> data() {
        List<DownloadData> list = new ArrayList<DownloadData>();
        for (int i = 0; i < 10; i++) {
            DownloadData data = new DownloadData();
            data.setString("字符串" + 0);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    @GetMapping("test")
    public Result test(String userId) throws Exception {
        User user = threadPoolService.selectUserById(userId);
        return ResultGenerator.genSuccessResult(user);
    }


}
