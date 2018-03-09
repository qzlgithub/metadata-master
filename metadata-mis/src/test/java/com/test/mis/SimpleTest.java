package com.test.mis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class SimpleTest
{

    private static Logger logger = LoggerFactory.getLogger(SimpleTest.class);

    @Test
    public void secretConfig(){
        logger.info("SimpleTest---secretConfig正确返回！");
    }
}
