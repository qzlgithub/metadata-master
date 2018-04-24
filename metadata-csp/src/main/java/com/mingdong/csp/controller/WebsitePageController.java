package com.mingdong.csp.controller;

import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class WebsitePageController
{
    @Resource
    private SystemService systemService;

    @GetMapping(value = {"/", "/index.html"})
    public ModelAndView indexPage()
    {
        ModelAndView view = new ModelAndView("website/index");
        view.addObject(Field.LOGIN, RequestThread.getIsLogin());
        view.addObject(Field.SERVICE_QQ, systemService.getServiceQq());
        return view;
    }

    @GetMapping(value = {"/product.html"})
    public ModelAndView productPage()
    {
        ModelAndView view = new ModelAndView("website/product");
        view.addObject(Field.LOGIN, RequestThread.getIsLogin());
        return view;
    }

    @GetMapping(value = {"/programme.html"})
    public ModelAndView programmePage()
    {
        ModelAndView view = new ModelAndView("website/programme");
        view.addObject(Field.LOGIN, RequestThread.getIsLogin());
        return view;
    }

    @GetMapping(value = {"/news.html"})
    public ModelAndView newsBusinessPage(@RequestParam(value = Field.ID, required = false) Long id)
    {
        ModelAndView view = new ModelAndView("website/news-business");
        view.addObject(Field.LOGIN, RequestThread.getIsLogin());
        view.addObject(Field.ID, id);
        return view;
    }

    @GetMapping(value = {"/news-company.html"})
    public ModelAndView newsCompanyPage(@RequestParam(value = Field.ID, required = false) Integer id)
    {
        ModelAndView view = new ModelAndView("website/news-company");
        view.addObject(Field.LOGIN, RequestThread.getIsLogin());
        view.addObject(Field.ID, id);
        return view;
    }

    @GetMapping(value = {"/news-use.html"})
    public ModelAndView newsUsePage()
    {
        ModelAndView view = new ModelAndView("website/news-use");
        view.addObject(Field.LOGIN, RequestThread.getIsLogin());
        return view;
    }

    @GetMapping(value = {"/news-privacy.html"})
    public ModelAndView newsPrivacyPage()
    {
        ModelAndView view = new ModelAndView("website/news-privacy");
        view.addObject(Field.LOGIN, RequestThread.getIsLogin());
        return view;
    }

    @GetMapping(value = {"/news-law.html"})
    public ModelAndView newsLawPage()
    {
        ModelAndView view = new ModelAndView("website/news-law");
        view.addObject(Field.LOGIN, RequestThread.getIsLogin());
        return view;
    }

    @GetMapping(value = {"/about.html"})
    public ModelAndView aboutPage()
    {
        ModelAndView view = new ModelAndView("website/about");
        view.addObject(Field.LOGIN, RequestThread.getIsLogin());
        view.addObject(Field.SERVICE_QQ, systemService.getServiceQq());
        return view;
    }

}
