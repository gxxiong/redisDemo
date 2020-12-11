package com.xgx.core.restful;

public enum ResultCode {
    CODE_200(200, "success"),
    CODE_401(401, "illegal access"),
    CODE_402(402, "your token has expired"),
    CODE_403(403, "you have no authority to access"),
    CODE_405(405, "data is null"),
    CODE_500(500, "runtime exception");

    private final Integer code;
    private final String describe;

    ResultCode(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }
}
