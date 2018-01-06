package com.movek.mis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@MapperScan("com.movek.mis.domain.mapper")
@ImportResource({"classpath:config/dubbo.xml"})
public class MISApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MISApplication.class, args);
        System.out.println("##### MIS app is running...");
    }
}
