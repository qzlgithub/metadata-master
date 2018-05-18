package com.mingdong.bop.configurer;

import com.mingdong.bop.service.SystemService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InstallerClass implements CommandLineRunner
{
    @Resource
    private SystemService systemService;

    @Override
    public void run(String... strings)
    {
        try
        {
            //刷新菜单缓存
            systemService.cacheSystemModule();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
