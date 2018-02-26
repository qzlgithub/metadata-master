package com.mingdong.csp.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
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
    @PostMapping(value = "client/user/login")
    public BLResp userLogin(HttpServletRequest request, @RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        HttpSession session = request.getSession();
        // TODO 开发过程暂时注释掉
        /*String userCode = jsonReq.getString(Field.CODE);
        String code = (String) session.getAttribute(Field.IMAGE_CAPTCHA);
        if(StringUtils.isNullBlank(userCode) || !userCode.equalsIgnoreCase(code))
        {
            return resp.result(RestResult.INVALID_CAPTCHA);
        }*/
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
    @PostMapping(value = "client/user/logout")
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
    @LoginRequired
    @PostMapping(value = "client/user/password")
    public BLResp changePassword(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        String oldPwd = jsonReq.getString(Field.ORG_PASSWORD);
        String newPwd = jsonReq.getString(Field.NEW_PASSWORD);
        if(StringUtils.isNullBlank(oldPwd) || StringUtils.isNullBlank(newPwd))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.changePassword(RequestThread.getUserId(), oldPwd, newPwd, resp);
        return resp;
    }

    /**
     * 获取用户产品凭证信息
     */
    @LoginRequired
    @GetMapping(value = "client/user/credential")
    public BLResp getUserCredential(@RequestParam(value = Field.PRODUCT_ID) Long productId,
            @RequestParam(value = Field.PASSWORD) String password)
    {
        BLResp resp = BLResp.build();
        clientService.getUserCredential(RequestThread.getUserId(), password, productId, resp);
        return resp;
    }

    /**
     * 更新用户产品凭证信息
     */
    @LoginRequired
    @PostMapping(value = "client/user/credential")
    public BLResp saveUserCredential(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long productId = jsonReq.getLong(Field.PRODUCT_ID);
        String appKey = jsonReq.getString(Field.APP_KEY);
        String reqHost = jsonReq.getString(Field.REQ_HOST);
        if(productId == null || StringUtils.isNullBlank(appKey) || StringUtils.isNullBlank(reqHost))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.saveUserCredential(RequestThread.getUserId(), productId, appKey, reqHost, resp);
        return resp;
    }

    /**
     * 添加子账号
     */
    @LoginRequired
    @PostMapping(value = "client/account/addition")
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
    @GetMapping(value = "client/sub-account/list")
    public ListRes getClientSubAccountList()
    {
        ListRes res = new ListRes();
        clientService.getClientSubAccountList(res);
        return res;
    }

    /**
     * 子账号的启用禁用
     */
    @LoginRequired
    @PostMapping(value = "client/changeStatus")
    public BLResp changeStatus(@RequestBody JSONObject jsonObject)
    {
        BLResp resp = BLResp.build();
        Long clientUserId = jsonObject.getLong(Field.CLIENT_USER_ID);
        clientService.changeStatus(RequestThread.getUserId(), clientUserId, resp);
        return resp;
    }

    @LoginRequired
    @GetMapping(value = "client/childAccountDetail")
    public BLResp getAccountDetail(@RequestParam(value = Field.CLIENT_USER_ID, required = false) Long clientUserId)
    {
        BLResp resp = BLResp.build();
        clientService.getAccountByUserId(clientUserId, resp);
        return resp;
    }

    /**
     * 编辑子账号
     */
    @LoginRequired
    @PostMapping(value = "client/editChildAccount")
    public BLResp editChildAccount(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long clientUserId = jsonReq.getLong(Field.CLIENT_USER_ID);
        String username = jsonReq.getString(Field.USERNAME);
        String password = jsonReq.getString(Field.PASSWORD);
        String name = jsonReq.getString(Field.NAME);
        String phone = jsonReq.getString(Field.PHONE);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(clientUserId == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(StringUtils.isNullBlank(username) || StringUtils.isNullBlank(name) || StringUtils.isNullBlank(phone))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.editChildAccount(RequestThread.getUserId(), clientUserId, username, password, name, phone,
                enabled, resp);
        return resp;
    }

    @LoginRequired
    @PostMapping(value = "client/user/deletion")
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
    @GetMapping(value = "client/message")
    public ListRes getClientMessageList(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        clientService.getClientMessageList(new Page(pageNum, pageSize), res);
        return res;
    }

}
