package com.mingdong.csp.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class ClientController
{
    @Resource
    private ClientService clientService;

    /**
     * 用户登陆
     */
    @PostMapping(value = "/client/user/login")
    public RestResp userLogin(HttpServletRequest request, @RequestBody JSONObject jsonReq)
    {
        RestResp resp = new RestResp();
        HttpSession session = request.getSession();
        String userCode = jsonReq.getString(Field.CODE);
        String code = (String) session.getAttribute(Field.IMAGE_CAPTCHA);
        if(StringUtils.isNullBlank(userCode) || !userCode.equalsIgnoreCase(code))
        {
            resp.setError(RestResult.INVALID_CAPTCHA);
            return resp;
        }
        String username = jsonReq.getString(Field.USERNAME);
        String password = jsonReq.getString(Field.PASSWORD);
        if(StringUtils.isNullBlank(username) || StringUtils.isNullBlank(password))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        clientService.userLogin(username, password, session.getId(), resp);
        return resp;
    }

    /**
     * 用户注销
     */
    @PostMapping(value = "/client/user/logout")
    public RestResp userLogout(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        clientService.userLogout(sessionId);
        return new RestResp();
    }

    /**
     * 用户修改密码
     */
    @LoginRequired
    @PostMapping(value = "/client/user/password")
    public RestResp changePassword(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = new RestResp();
        String oldPwd = jsonReq.getString(Field.ORG_PASSWORD);
        String newPwd = jsonReq.getString(Field.NEW_PASSWORD);
        if(StringUtils.isNullBlank(oldPwd) || StringUtils.isNullBlank(newPwd))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        clientService.changePassword(RequestThread.getUserId(), oldPwd, newPwd, resp);
        return resp;
    }

    /**
     * 获取用户产品凭证信息
     */
    @LoginRequired
    @GetMapping(value = "/client/user/credential")
    public RestResp getUserCredential(@RequestParam(value = Field.PRODUCT_ID) Long productId,
            @RequestParam(value = Field.PASSWORD) String password)
    {
        RestResp resp = new RestResp();
        clientService.getUserCredential(RequestThread.getUserId(), password, productId, resp);
        return resp;
    }

    /**
     * 更新用户产品凭证信息
     */
    @LoginRequired
    @PostMapping(value = "/client/user/credential")
    public RestResp saveUserCredential(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = new RestResp();
        Long productId = jsonReq.getLong(Field.PRODUCT_ID);
        String appKey = jsonReq.getString(Field.APP_KEY);
        String reqHost = jsonReq.getString(Field.REQ_HOST);
        if(productId == null || StringUtils.isNullBlank(appKey) || StringUtils.isNullBlank(reqHost))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        clientService.saveUserCredential(RequestThread.getUserId(), productId, appKey, reqHost, resp);
        return resp;
    }

    /**
     * 添加子账号
     */
    @LoginRequired
    @PostMapping(value = "/client/account/addition")
    public RestResp addAccount(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = new RestResp();
        String username = jsonReq.getString(Field.USERNAME);
        String password = jsonReq.getString(Field.PASSWORD);
        String name = jsonReq.getString(Field.NAME);
        String phone = jsonReq.getString(Field.PHONE);
        if(StringUtils.isNullBlank(username) || StringUtils.isNullBlank(password) || StringUtils.isNullBlank(name) ||
                StringUtils.isNullBlank(phone))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        clientService.addAccount(RequestThread.getUserId(), username, password, name, phone, resp);
        return resp;
    }

    /**
     * 获取子账号列表
     */
    @LoginRequired
    @GetMapping(value = "/client/sub-account/list")
    public RestListResp getClientSubAccountList()
    {
        RestListResp res = new RestListResp();
        clientService.getClientSubAccountList(res);
        return res;
    }

    /**
     * 子账号的启用禁用
     */
    @LoginRequired
    @PostMapping(value = "/client/sub-account/status")
    public RestResp changeSubUserStatus(@RequestBody JSONObject jsonObject)
    {
        RestResp resp = new RestResp();
        if(!TrueOrFalse.TRUE.equals(RequestThread.getPrimary()))
        {
            resp.setError(RestResult.ONLY_PRIMARY_USER);
            return resp;
        }
        Long clientUserId = jsonObject.getLong(Field.CLIENT_USER_ID);
        Integer status = jsonObject.getInteger(Field.STATUS);
        if(clientUserId == null || (!TrueOrFalse.TRUE.equals(status) && !TrueOrFalse.FALSE.equals(status)))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        clientService.changeSubUserStatus(clientUserId, status, resp);
        return resp;
    }

    @LoginRequired
    @GetMapping(value = "/client/user")
    public RestResp getClientUserInfo(@RequestParam(value = Field.USER_ID) Long userId)
    {
        RestResp resp = new RestResp();
        clientService.getClientUserInfo(userId, resp);
        return resp;
    }

    /**
     * 编辑子账号
     */
    @LoginRequired
    @PostMapping(value = "/client/editChildAccount")
    public RestResp editChildAccount(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = new RestResp();
        Long clientUserId = jsonReq.getLong(Field.CLIENT_USER_ID);
        String username = jsonReq.getString(Field.USERNAME);
        String password = jsonReq.getString(Field.PASSWORD);
        String name = jsonReq.getString(Field.NAME);
        String phone = jsonReq.getString(Field.PHONE);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(clientUserId == null)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        if(StringUtils.isNullBlank(username) || StringUtils.isNullBlank(name) || StringUtils.isNullBlank(phone))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        if(!TrueOrFalse.TRUE.equals(RequestThread.getPrimary()))
        {
            resp.setError(RestResult.ONLY_PRIMARY_USER);
            return resp;
        }
        clientService.editSubUser(clientUserId, username, password, name, phone, enabled, resp);
        return resp;
    }

    @LoginRequired
    @PostMapping(value = "/client/user/deletion")
    public RestResp dropSubUser(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = new RestResp();
        Long clientUserId = jsonReq.getLong(Field.CLIENT_USER_ID);
        if(clientUserId == null)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        clientService.setSubUserDeleted(RequestThread.getUserId(), clientUserId, resp);
        return resp;
    }

    @LoginRequired
    @GetMapping(value = "/client/message")
    public RestListResp getClientMessageList(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
        clientService.getClientMessageList(new Page(pageNum, pageSize), res);
        return res;
    }

}
