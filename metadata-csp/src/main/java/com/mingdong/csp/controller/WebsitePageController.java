package com.mingdong.csp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebsitePageController
{
    @GetMapping(value = {"/", "/index.html"})
    public ModelAndView indexPage()
    {
        ModelAndView view = new ModelAndView("website/index");
        return view;
    }

    @GetMapping(value = {"/product.html"})
    public ModelAndView productPage()
    {
        ModelAndView view = new ModelAndView("website/product");
        return view;
    }

    @GetMapping(value = {"/programme.html"})
    public ModelAndView programmePage()
    {
        ModelAndView view = new ModelAndView("website/programme");
        return view;
    }

    @GetMapping(value = {"/news.html"})
    public ModelAndView newsBusinessPage()
    {
        ModelAndView view = new ModelAndView("website/news-business");
        return view;
    }

    @GetMapping(value = {"/news-company.html"})
    public ModelAndView newsCompanyPage()
    {
        ModelAndView view = new ModelAndView("website/news-company");
        return view;
    }

    @GetMapping(value = {"/news-use.html"})
    public ModelAndView newsUsePage()
    {
        ModelAndView view = new ModelAndView("website/news-use");
        return view;
    }

    @GetMapping(value = {"/news-privacy.html"})
    public ModelAndView newsPrivacyPage()
    {
        ModelAndView view = new ModelAndView("website/news-privacy");
        return view;
    }

    @GetMapping(value = {"/news-law.html"})
    public ModelAndView newsLawPage()
    {
        ModelAndView view = new ModelAndView("website/news-law");
        return view;
    }

    @GetMapping(value = {"/about.html"})
    public ModelAndView aboutPage()
    {
        ModelAndView view = new ModelAndView("website/about");
        return view;
    }

}
