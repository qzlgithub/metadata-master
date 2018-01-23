package com.mingdong.mis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:config/dubbo.xml"})
public class MISApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MISApplication.class, args);
        System.out.println("##### startup CSP service successfully ...");
    }
}
