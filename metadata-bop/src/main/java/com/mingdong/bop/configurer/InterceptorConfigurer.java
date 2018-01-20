package com.mingdong.bop.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfigurer extends WebMvcConfigurerAdapter
{
    @Resource
    private AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(accessInterceptor)
                .addPathPatterns("/**/**.html")
                .addPathPatterns("/client/**")
                .addPathPatterns("/stats/**")
                .addPathPatterns("/changePwd")
                .addPathPatterns("/client/product/open")
                .addPathPatterns("/client/product/renew")
                .excludePathPatterns("/index.html");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}
