package com.xgx.web;

import com.xgx.pojo.User;
import com.xgx.service.IThreadPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ThreadPoolController {

    @Autowired
    private IThreadPoolService iThreadPoolService;


    @RequestMapping(value = "test")
    public void test() throws Exception {
        User user = new User();
        iThreadPoolService.add();

    }


}
