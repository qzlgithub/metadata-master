package com.mingdong.csp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:config/dubbo.xml"})
public class CSPApplication
{
    /**
     * 客户服务平台
     */
    public static void main(String[] args)
    {
        SpringApplication.run(CSPApplication.class, args);
        System.out.println("##### startup CSP service successfully ...");
    }
}
