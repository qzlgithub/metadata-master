package com.mingdong.csp.controller;

import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class PageController
{
    @Resource
    private ClientService clientService;

    /**
     * 登陆页
     */
    @GetMapping(value = {"/", "index.html"})
    public ModelAndView indexPage(HttpServletRequest request) throws IOException
    {
        ModelAndView view = new ModelAndView("index");
        ImageCode imageCode = CaptchaUtils.buildImageCode();
        HttpSession session = request.getSession();
        session.setAttribute(Field.CAPTCHA_CODE, imageCode.getCode());
        view.addObject(Field.IMAGE_CAPTCHA, "data:image/png;base64," + imageCode.getBase64Code());
        return view;
    }

    /**
     * 用户首页
     */
    @LoginRequired
    @GetMapping(value = {"/home.html"})
    public ModelAndView getHomeData()
    {
        //        BLResp resp = BLResp.build();
        //        clientService.getHomeData(RequestThread.getClientId(), RequestThread.getUserId(), resp);

        ModelAndView modelAndView = new ModelAndView("/home");

        return modelAndView;
    }

    @LoginRequired
    @GetMapping(value = {"/product/all.html"})
    public ModelAndView productAll()
    {

        ModelAndView modelAndView = new ModelAndView("/product/all");
        return modelAndView;
    }

    @LoginRequired
    @GetMapping(value = {"/product/detail.html"})
    public ModelAndView productDetail()
    {
        ModelAndView modelAndView = new ModelAndView("/product/detail");
        return modelAndView;
    }

    @LoginRequired
    @GetMapping(value = {"/product/introduce.html"})
    public ModelAndView productIntroduce()
    {
        ModelAndView modelAndView = new ModelAndView("/product/introduce");
        return modelAndView;
    }

    @LoginRequired
    @GetMapping(value = {"/product/mine.html"})
    public ModelAndView productMine()
    {
        ModelAndView modelAndView = new ModelAndView("/product/mine");
        return modelAndView;
    }

    @LoginRequired
    @GetMapping(value = {"/product/recharge.html"})
    public ModelAndView productRecharge()
    {
        ModelAndView modelAndView = new ModelAndView("/product/recharge");
        return modelAndView;
    }

    @LoginRequired
    @GetMapping(value = {"/product/request.html"})
    public ModelAndView productRequest()
    {
        ModelAndView modelAndView = new ModelAndView("/product/request");
        return modelAndView;
    }

    @LoginRequired
    @GetMapping(value = {"/system/account-list.html"})
    public ModelAndView systemAccountList()
    {
        ModelAndView modelAndView = new ModelAndView("/system/account-list");
        return modelAndView;
    }

    @LoginRequired
    @GetMapping(value = {"/system/account-set.html"})
    public ModelAndView systemAccountSet()
    {
        ModelAndView modelAndView = new ModelAndView("/system/account-set");
        return modelAndView;
    }

    @LoginRequired
    @GetMapping(value = {"/system/message.html"})
    public ModelAndView systemMessage()
    {
        ModelAndView modelAndView = new ModelAndView("/system/message");
        return modelAndView;
    }

}
