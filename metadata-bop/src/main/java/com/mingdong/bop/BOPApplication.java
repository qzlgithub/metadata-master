package com.mingdong.bop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;

@SpringBootApplication
@ServletComponentScan
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
        System.out.println("##### startup BOP service successfully ...");
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer()
    {
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400.html"));
            container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html"));
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404.html"));
        };
    }
}
