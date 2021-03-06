package com.xgx.service;

import com.xgx.pojo.User;

public interface IThreadPoolService {

    void add() throws Exception;

    void check();

    User selectUserById(String userId);
}
