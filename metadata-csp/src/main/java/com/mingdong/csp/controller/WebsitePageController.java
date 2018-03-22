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

    @GetMapping(value = {"/news.html"})
    public ModelAndView newsPage()
    {
        ModelAndView view = new ModelAndView("website/news");
        return view;
    }

    @GetMapping(value = {"/about.html"})
    public ModelAndView aboutPage()
    {
        ModelAndView view = new ModelAndView("website/about");
        return view;
    }

}
