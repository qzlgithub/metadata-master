package com.mingdong.bop.configurer;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/druid/*", initParams = {@WebInitParam(name = "loginUsername", value = "wdkj"),// 用户名
        @WebInitParam(name = "loginPassword", value = "wdkj@druid"),// 密码
        @WebInitParam(name = "resetEnable", value = "false")// 禁用HTML页面上的“Reset All”功能
})
public class DruidStatViewServlet extends StatViewServlet
{
}
