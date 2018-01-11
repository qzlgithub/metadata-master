package com.mingdong.csp.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

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
        userService.userLogin(username, password, session.getId(), resp);
        return resp;
    }

    @PostMapping(value = "logout")
    public BLResp userLogout(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        userService.userLogout(sessionId);
        return new BLResp();
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
        userService.changePassword(RequestThread.getUserId(), oldPwd, newPwd, resp);
        return resp;
    }

    /**
     * 添加子账号
     */
    @PostMapping(value = "/addition")
    public BLResp addChildAccount(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long clientUserId = jsonReq.getLong(Field.CLIENT_USER_ID);
        String username = jsonReq.getString(Field.USERNAME);
        String password = jsonReq.getString(Field.PASSWORD);
        String name = jsonReq.getString(Field.NAME);
        String phone = jsonReq.getString(Field.PHONE);
        if(clientUserId == null || clientUserId <= 0)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(StringUtils.isNullBlank(username) || StringUtils.isNullBlank(password) || StringUtils.isNullBlank(name) ||
                StringUtils.isNullBlank(phone))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        userService.addChildAccount(clientUserId, username, password, name, phone, resp);

        return resp;
    }

    /**
     * 获取子账号列表
     */
    @GetMapping(value = "childList")
    public Map<String, Object> getChildAccountList(@RequestParam(value = Field.CLIENT_USER_ID) Long clientUserId)
    {
        BLResp resp = BLResp.build();
        userService.getChildAccountMap(clientUserId);
        return resp.getDataMap();
    }

    /**
     * 子账号的启用禁用
     */
    @PostMapping(value = "changeStatus")
    public BLResp changeStatus(@RequestBody JSONObject jsonObject)
    {
        BLResp resp = BLResp.build();
        Long clientUserId = jsonObject.getLong(Field.CLIENT_USER_ID);
        userService.changeStatus(clientUserId, resp);

        return resp;
    }

    /**
     * 编辑子账号
     */
    @PostMapping(value = "editChildAccount")
    public BLResp editChildAccount(@RequestBody JSONObject jsonObject)
    {
        BLResp resp = BLResp.build();
        Long clientUserId = jsonObject.getLong(Field.CLIENT_USER_ID);
        String username = jsonObject.getString(Field.USERNAME);
        String password = jsonObject.getString(Field.PASSWORD);
        String name = jsonObject.getString(Field.NAME);
        String phone = jsonObject.getString(Field.PHONE);
        if(clientUserId == null || clientUserId <= 0)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(StringUtils.isNullBlank(username) || StringUtils.isNullBlank(password) || StringUtils.isNullBlank(name) ||
                StringUtils.isNullBlank(phone))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        userService.editChildAccount(clientUserId, username, password, name, phone, resp);
        return resp;
    }

    /**
     * 删除子账号
     */
    @PostMapping(value = "dropChildAccount")
    public BLResp dropChildAccount(@RequestParam(value = Field.CLIENT_USER_ID) Long clientUserId)
    {
        BLResp resp = BLResp.build();
        return resp;
    }

}
