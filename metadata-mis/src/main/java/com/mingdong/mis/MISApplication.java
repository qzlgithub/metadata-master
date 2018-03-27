package com.mingdong.mis;

import com.mingdong.mis.configurer.MultipleMongoProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EnableConfigurationProperties(MultipleMongoProperties.class)
@MapperScan("com.mingdong.mis.domain.mapper")
@ImportResource({"classpath:config/dubbo.xml"})
public class MISApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MISApplication.class, args);
        System.out.println("##### startup MIS service successfully ...");
    }
}
