package com.mingdong.csp.controller;

import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.service.RemoteProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class IndexController
{
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Resource
    private RemoteProductService productApi;

    /**
     * 用户首页
     */
    @LoginRequired
    @GetMapping(value = {"/", "/home.html"})
    public BLResp userHome()
    {
        BLResp resp = BLResp.build();

        return resp;
    }

}
