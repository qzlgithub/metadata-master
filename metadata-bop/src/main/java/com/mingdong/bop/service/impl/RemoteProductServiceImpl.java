package com.mingdong.bop.service.impl;

import com.mingdong.core.service.RemoteProductService;

public class RemoteProductServiceImpl implements RemoteProductService
{
    @Override
    public String sayHi(String name)
    {
        return "Hello " + name + "!";
    }
}
