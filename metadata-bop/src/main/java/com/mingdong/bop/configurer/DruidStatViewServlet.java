package com.mingdong.bop.configurer;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/druid/*", initParams = {@WebInitParam(name = "loginUsername", value = "wdkj"),
        @WebInitParam(name = "loginPassword", value = "wdkj@druid"),
        @WebInitParam(name = "resetEnable", value = "false")})
public class DruidStatViewServlet extends StatViewServlet
{
}
