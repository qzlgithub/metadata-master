package com.mingdong.bop.service.impl;

import com.mingdong.core.service.RemoteProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteProductServiceImpl implements RemoteProductService
{
    private static Logger logger = LoggerFactory.getLogger(RemoteProductServiceImpl.class);

    @Override
    public String sayHi(String name)
    {
        logger.info("####### coming: {}", name);
        return "Hello " + name + "!";
    }
}
