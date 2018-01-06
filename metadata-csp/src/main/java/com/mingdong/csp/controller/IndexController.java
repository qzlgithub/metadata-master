package com.mingdong.csp.controller;

import com.mingdong.core.service.RemoteProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
public class IndexController
{
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Resource
    private RemoteProductService productApi;

    @GetMapping(value = "/test")
    public void test(@RequestParam(value = "name") String name)
    {
        logger.info(">>>>> call: {}", name);
        String back = productApi.sayHi(name);
        logger.info(">>>>> back: {}", back);
    }

}
