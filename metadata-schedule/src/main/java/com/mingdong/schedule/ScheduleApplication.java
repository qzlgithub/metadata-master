package com.mingdong.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:dubbo.xml"})
public class ScheduleApplication
{
    /**
     * 客户服务平台
     */
    public static void main(String[] args)
    {
        SpringApplication.run(ScheduleApplication.class, args);
        System.out.println("##### startup SCHEDULE service successfully ...");
    }

}
