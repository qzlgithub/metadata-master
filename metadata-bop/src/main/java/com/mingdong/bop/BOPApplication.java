package com.mingdong.bop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@MapperScan("com.mingdong.bop.domain.mapper")
@ImportResource({"classpath:config/dubbo.xml"})
public class BOPApplication
{
    /**
     * 业务运营平台
     */
    public static void main(String[] args)
    {
        SpringApplication.run(BOPApplication.class, args);
        System.out.println("##### startup BOP service successfully ... #####");
    }
}
