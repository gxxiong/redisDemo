package com.xgx.core.exception;

import com.xgx.core.restful.ResultCode;

public class BusinessException extends Exception {

    private Integer code;
    private String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.message = message;
        this.code = resultCode.getCode();
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
