package com.xgx.core.exception;

import com.alibaba.fastjson.JSON;
import com.xgx.core.restful.Result;
import com.xgx.core.restful.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *   在Controller层统一对异常进行处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String REQUEST_METHOD_GET = "GET";

    /**
     * 统一处理异常的入口方法
     *
     * @param request
     * @param response
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
        return processException(request, response, ex);
    }

    private ModelAndView processException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        String requestMethod = getRequestMethod(request, response);
        String requestUri = getRequestUri(request, response);
        boolean isAjax = isRequestAjax(request, response);

        if (logger.isInfoEnabled()) {
            logger.error(ex.getMessage());
            logger.info("Start handle exception,Exception message:" + ex.getMessage());
            logger.info(String.format("Current request requestMethod:[%s],requestUri:[%s],isAjax:[%s]", requestMethod, requestUri, isAjax));
        }
        if (StringUtils.equalsIgnoreCase(requestMethod, REQUEST_METHOD_GET)) {
            return processAjax(request, response, ex);
        } else if (isAjax) {
            return processAjax(request, response, ex);
        } else {
            return processAjax(request, response, ex);
        }
    }

    private ModelAndView processAjax(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        Result rtnResult = new Result();
        if (ex instanceof UnauthorizedException) {
            rtnResult.setCode(ResultCode.CODE_403.getCode()).setMessage(ResultCode.CODE_403.getDescribe());
            writerJson(rtnResult, response, ex.getMessage());
        } else if (ex instanceof AuthenticationException) {
            rtnResult.setCode(ResultCode.CODE_401.getCode()).setMessage(ex.getMessage());
            writerJson(rtnResult, response, ex.getMessage());
        } else if (ex instanceof BusinessException) {
            Integer code = ((BusinessException) ex).getCode();
            if (code != null) {
                rtnResult.setCode(code).setMessage(ex.getMessage());
            } else {
                rtnResult.setCode(ResultCode.CODE_500.getCode()).setMessage(ex.getMessage());
            }
            writerJson(rtnResult, response, ex.getMessage());
        } else {
            rtnResult.setCode(ResultCode.CODE_500.getCode()).setMessage(ex.getMessage());
            writerJson(rtnResult, response, ex.getMessage());
        }
        return new ModelAndView();
    }

    /**
     * 用来判断http请求是否是Ajax请求
     * true ajax请求
     * false 非ajax请求
     *
     * @param request
     * @param response
     * @return
     */
    private boolean isRequestAjax(HttpServletRequest request, HttpServletResponse response) {
        String requestedWith = request.getHeader("X-Requested-With");
        return (StringUtils.isNotEmpty(requestedWith) && StringUtils.equalsIgnoreCase(requestedWith, "XMLHttpRequest"));
    }

    /**
     * 获取当前请求的资源路径
     *
     * @param request
     * @param response
     * @return
     */
    private String getRequestUri(HttpServletRequest request, HttpServletResponse response) {
        return request.getRequestURI();
    }

    /**
     * 获取请求的方法 post/get等
     *
     * @param request
     * @param response
     * @return
     */
    private String getRequestMethod(HttpServletRequest request, HttpServletResponse response) {
        return request.getMethod();
    }


    /**
     * @Description: 封装异常信息输出
     * @Param:
     * @return:
     * @Author: sirius
     * @Date: 2018/11/8
     */
    private void writerJson(Result result, HttpServletResponse response, String msg) {
        response.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(result));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
