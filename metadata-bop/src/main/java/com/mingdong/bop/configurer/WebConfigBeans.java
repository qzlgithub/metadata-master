package com.mingdong.bop.configurer;

import com.mingdong.core.component.StringToDateConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebConfigBeans
{
    @Resource
    private RequestMappingHandlerAdapter handlerAdapter;

    /**
     * 增加字符串转日期的功能
     */
    @PostConstruct
    public void initEditableValidation()
    {
        ConfigurableWebBindingInitializer initializer =
                (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
        if(initializer.getConversionService() != null)
        {
            GenericConversionService genericConversionService =
                    (GenericConversionService) initializer.getConversionService();
            genericConversionService.addConverter(new StringToDateConverter());
        }
    }

    /**
     * thymeleaf 全局静态变量设置
     */
    @Resource
    private void configureThymeleafStaticVars(ThymeleafViewResolver viewResolver)
    {
        if(viewResolver != null)
        {
            Map<String, Object> vars = new HashMap<>();
            //            vars.put("ctx", "/app/");
            //            vars.put("var1", "var1");
            //            vars.put("var2", "var2");
            viewResolver.setStaticVariables(vars);
        }
    }
}
