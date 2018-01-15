package com.mingdong.csp.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfigurer extends WebMvcConfigurerAdapter
{
    @Resource
    private ApiAccessInterceptor apiAccessInterceptor;
    @Resource
    private WebAccessInterceptor webAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(webAccessInterceptor).addPathPatterns("/**/**.html").excludePathPatterns("/index.html");
        registry.addInterceptor(apiAccessInterceptor)
                .addPathPatterns("/**/**")
                .excludePathPatterns("/**.html")
                .excludePathPatterns("/**/**.html");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}
