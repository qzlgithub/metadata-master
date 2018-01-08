package com.mingdong.csp.controller;

import com.mingdong.core.service.RemoteClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController
{
    @Resource
    private RemoteClientService clientApi;

    @PostMapping(value = "login")
    public void userLogin()
    {

    }
}
