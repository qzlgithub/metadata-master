package com.mingdong.bop.controller;

import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class HomePageController
{
    @Resource
    private SystemService systemService;

    @LoginRequired
    @RequestMapping(value = "/enter.html")
    public ModelAndView enterPage()
    {
        if(RequestThread.isSalesman())
        {
            //业务员
            return new ModelAndView("redirect:/home.html");
        }
        else if(RequestThread.isOperation())
        {
            //运营
            return new ModelAndView("redirect:/stats.html");
        }
        //管理员
        ModelAndView view = new ModelAndView("enter");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/home.html")
    public ModelAndView homePage()
    {
        ModelAndView view = new ModelAndView("home");
        view.addAllObjects(RequestThread.getMap());
        view.addAllObjects(systemService.getHomeData());
        return view;
    }

    /**
     * 统计数据：
     * 1. 客户数：30日新增，总数
     * 2. 充值金额：近7天，近30天
     * 3. 平台收入：近7天，近30天
     * 4. 产品请求次数/失败次数：今日，昨日，本月，累计
     */
    @LoginRequired
    @RequestMapping(value = "/stats.html")
    public ModelAndView statsPage()
    {
        ModelAndView view = new ModelAndView("monitor/index");
        // Map<String,Object> clientStats =
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

}
