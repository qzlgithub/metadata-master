package com.mingdong.bop.configurer;

import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.RestResp;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler
{

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestResp handleSQLException(HttpServletRequest request, HttpServletResponse response, Exception ep)
    {
        ep.printStackTrace();
        return new RestResp(RestResult.SYSTEM_ERROR);
    }

}
