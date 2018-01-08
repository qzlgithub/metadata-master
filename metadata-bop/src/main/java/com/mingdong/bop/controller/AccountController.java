package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.RequestThread;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AccountController
{
    @Resource
    private ManagerService managerService;

    /**
     * 账号登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public BLResp addNewAccount(HttpServletRequest request, @RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();

        HttpSession session = request.getSession();
        String sessionId = session.getId();
        String realCode = (String) session.getAttribute(Field.IMAGE_CAPTCHA);
        String code = jsonReq.getString(Field.IMAGE_CODE);
        /*if(code == null || !code.equalsIgnoreCase(realCode))
        {
            return resp.result(RestResult.INVALID_CAPTCHA);
        }*/
        session.removeAttribute(Field.IMAGE_CAPTCHA);
        String username = jsonReq.getString(Field.USERNAME);
        String password = jsonReq.getString(Field.PASSWORD);
        managerService.userLogin(username, password, sessionId, resp);
        return resp;
    }

    @RequestMapping(value = "logout")
    public ModelAndView managerLogout(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        managerService.userLogout(sessionId);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "changePwd", method = RequestMethod.POST)
    @ResponseBody
    public BLResp changePwd(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        String oldPwd = jsonReq.getString(Field.OLD_PWD);
        String newPwd = jsonReq.getString(Field.NEW_PWD);
        if(StringUtils.isNullBlank(oldPwd) || StringUtils.isNullBlank(newPwd))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        managerService.changePassword(RequestThread.getOperatorId(), oldPwd, newPwd, resp);
        return resp;
    }
}