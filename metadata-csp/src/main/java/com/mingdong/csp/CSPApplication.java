package com.mingdong.csp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;

@SpringBootApplication
@ImportResource({"classpath:dubbo.xml"})
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
