package com.mingdong.csp.controller;

import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.BLResp;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@Controller
public class PageController
{
    @Resource
    private UserService userService;

    /**
     * 用户首页
     */
    @LoginRequired
    @GetMapping(value = {"/home.html"})
    public BLResp getHomeData()
    {
        BLResp resp = BLResp.build();
        userService.getHomeData(RequestThread.getClientId(), RequestThread.getUserId(), resp);
        return resp;
    }

}
