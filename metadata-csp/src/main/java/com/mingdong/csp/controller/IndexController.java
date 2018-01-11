package com.mingdong.csp.controller;

import com.mingdong.core.model.BLResp;
import com.mingdong.core.service.RemoteProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@Controller
public class IndexController
{
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Resource
    private RemoteProductService productApi;

    /**
     * 用户首页
     */
    @GetMapping(value = {"/", "/home.html"})
    public BLResp userHome()
    {
        BLResp resp = BLResp.build();

        return resp;
    }

}
