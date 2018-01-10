package com.mingdong.csp.controller;

import com.alibaba.dubbo.common.json.JSONObject;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.RequestThread;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "user")
public class UserController
{
    @Resource
    private UserService userService;

    @PostMapping(value = "login")
    public BLResp userLogin(HttpServletRequest request, @RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        String userCode = jsonReq.getString(Field.CODE);
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute(Field.CODE);
        if(StringUtils.isNullBlank(userCode) || !userCode.equalsIgnoreCase(code))
        {
            return resp.result(RestResult.INVALID_CAPTCHA);
        }
        String username = jsonReq.getString(Field.USERNAME);
        String password = jsonReq.getString(Field.PASSWORD);
        if(StringUtils.isNullBlank(username) || StringUtils.isNullBlank(password))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        userService.userLogin(username, password, resp);
        return resp;
    }

    @PostMapping(value = "changePwd")
    public BLResp changePwd(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        String oldPwd = jsonReq.getString(Field.OLD_PWD);
        String newPwd = jsonReq.getString(Field.NEW_PWD);
        if(StringUtils.isNullBlank(oldPwd) || StringUtils.isNullBlank(newPwd))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        userService.changePassword(RequestThread.getOperatorId(), oldPwd, newPwd, resp);
        return resp;
    }

}
