package com.mingdong.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@MapperScan("com.mingdong.backend.domain.mapper")
@ImportResource({"classpath:dubbo.xml"})
public class BackendApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(BackendApplication.class, args);
        System.out.println("##### startup Backend service successfully ...");
    }
}
