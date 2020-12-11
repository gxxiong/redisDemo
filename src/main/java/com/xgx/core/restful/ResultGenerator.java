package com.xgx.core.restful;

public class ResultGenerator {

    /**
     * 无参数返回成功状态
     *
     * @return
     */
    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.CODE_200.getCode())
                .setMessage(ResultCode.CODE_200.getDescribe());
    }

    /**
     * 有参数返回成功状态
     *
     * @param data 计算结果对象
     * @return
     */
    public static Result genSuccessResult(Object data) {
        return new Result()
                .setCode(ResultCode.CODE_200.getCode())
                .setMessage(ResultCode.CODE_200.getDescribe())
                .setData(data);
    }

    /**
     * 有参数返回失败状态
     *
     * @param message 失败原因
     * @return
     */
    public static Result genFailResult(String message) {
        return new Result()
                .setCode(ResultCode.CODE_500.getCode())
                .setMessage(message);
    }

}
