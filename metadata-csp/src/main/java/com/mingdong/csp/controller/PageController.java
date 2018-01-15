package com.mingdong.csp.controller;

import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.ImageCode;
import com.mingdong.core.util.CaptchaUtils;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(PageController.class);
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
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        view.addObject(Field.IMAGE_CAPTCHA, "data:image/png;base64," + imageCode.getBase64Code());
        return view;
    }

    /**
     * 用户首页
     */
    @LoginRequired
    @GetMapping(value = "home.html")
    public ModelAndView getHomeData()
    {
        ModelAndView view = new ModelAndView("home");
        //        BLResp resp = BLResp.build();
        //        clientService.getHomeData(RequestThread.getClientId(), RequestThread.getUserId(), resp);
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/all.html"})
    public ModelAndView productAll()
    {

        ModelAndView view = new ModelAndView("/product/all");
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/detail.html"})
    public ModelAndView productDetail()
    {
        ModelAndView view = new ModelAndView("/product/detail");
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/introduce.html"})
    public ModelAndView productIntroduce()
    {
        ModelAndView view = new ModelAndView("/product/introduce");
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/mine.html"})
    public ModelAndView productMine()
    {
        ModelAndView view = new ModelAndView("/product/mine");
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/recharge.html"})
    public ModelAndView productRecharge()
    {
        ModelAndView view = new ModelAndView("/product/recharge");
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/request.html"})
    public ModelAndView productRequest()
    {
        ModelAndView view = new ModelAndView("/product/request");
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/account-list.html"})
    public ModelAndView systemAccountList()
    {
        ModelAndView view = new ModelAndView("/system/account-list");
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/account-set.html"})
    public ModelAndView systemAccountSet()
    {
        ModelAndView view = new ModelAndView("/system/account-set");
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/message.html"})
    public ModelAndView systemMessage()
    {
        ModelAndView view = new ModelAndView("/system/message");
        return view;
    }

}
