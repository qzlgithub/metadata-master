package com.test.bop;

import com.mingdong.bop.service.impl.ClientServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class SimpleTest
{

    private static Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Test
    public void secretConfig(){
        logger.info("SimpleTest---secretConfig正确返回！");
    }
}
