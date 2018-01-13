package com.mingdong.csp.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "client")
public class ClientController
{
    @Resource
    private ClientService clientService;

    /**
     * 用户登陆
     */
    @PostMapping(value = "user/login")
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
        clientService.userLogin(username, password, session.getId(), resp);
        return resp;
    }

    /**
     * 用户注销
     */
    @PostMapping(value = "user/logout")
    public BLResp userLogout(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        clientService.userLogout(sessionId);
        return new BLResp();
    }

    /**
     * 用户修改密码
     */
    @PostMapping(value = "user/password")
    public BLResp changePassword(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        String oldPwd = jsonReq.getString(Field.OLD_PWD);
        String newPwd = jsonReq.getString(Field.NEW_PWD);
        if(StringUtils.isNullBlank(oldPwd) || StringUtils.isNullBlank(newPwd))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.changePassword(RequestThread.getUserId(), oldPwd, newPwd, resp);
        return resp;
    }

    /**
     * 添加子账号
     */
    @PostMapping(value = "account/addition")
    public BLResp addAccount(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        String username = jsonReq.getString(Field.USERNAME);
        String password = jsonReq.getString(Field.PASSWORD);
        String name = jsonReq.getString(Field.NAME);
        String phone = jsonReq.getString(Field.PHONE);
        if(StringUtils.isNullBlank(username) || StringUtils.isNullBlank(password) || StringUtils.isNullBlank(name) ||
                StringUtils.isNullBlank(phone))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.addAccount(RequestThread.getUserId(), username, password, name, phone, resp);
        return resp;
    }

    /**
     * 获取子账号列表
     */
    @LoginRequired
    @GetMapping(value = "account/list")
    public BLResp getAccountList()
    {
        BLResp resp = BLResp.build();
        clientService.getAccountList(RequestThread.getClientId(), RequestThread.getUserId(), resp);
        return resp;
    }

    /**
     * 子账号的启用禁用
     */
    @PostMapping(value = "changeStatus")
    public BLResp changeStatus(@RequestBody JSONObject jsonObject)
    {
        BLResp resp = BLResp.build();
        Long clientUserId = jsonObject.getLong(Field.CLIENT_USER_ID);
        clientService.changeStatus(clientUserId, resp);
        return resp;
    }

    /**
     * 编辑子账号
     */
    @PostMapping(value = "editChildAccount")
    public BLResp editChildAccount(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long clientUserId = jsonReq.getLong(Field.CLIENT_USER_ID);
        String username = jsonReq.getString(Field.USERNAME);
        String password = jsonReq.getString(Field.PASSWORD);
        String name = jsonReq.getString(Field.NAME);
        String phone = jsonReq.getString(Field.PHONE);
        if(clientUserId == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(StringUtils.isNullBlank(username) || StringUtils.isNullBlank(password) || StringUtils.isNullBlank(name) ||
                StringUtils.isNullBlank(phone))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.editChildAccount(clientUserId, username, password, name, phone, resp);
        return resp;
    }

    /**
     * 删除子账号
     */
    @LoginRequired
    @PostMapping(value = "user/deletion")
    public BLResp dropSubUser(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long clientUserId = jsonReq.getLong(Field.CLIENT_USER_ID);
        if(clientUserId == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.setSubUserDeleted(RequestThread.getUserId(), clientUserId, resp);
        return resp;
    }

    @LoginRequired
    @GetMapping(value = "message")
    public BLResp getClientMessage(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        clientService.getClientMessage(RequestThread.getClientId(), new Page(pageNum, pageSize), resp);
        return resp;
    }

}
