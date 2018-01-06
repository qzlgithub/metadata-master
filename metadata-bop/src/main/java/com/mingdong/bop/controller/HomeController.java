package com.mingdong.bop.controller;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ImageCode;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.util.CaptchaUtil;
import com.movek.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class HomeController
{
    @Resource
    private RedisDao redisDao;

    @RequestMapping(value = {"/", "index.html", "login.html"})
    public ModelAndView index(HttpServletRequest request) throws IOException
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        if(!StringUtils.isNullBlank(sessionId))
        {
            ManagerSession ms = redisDao.getManagerSession(sessionId);
            if(ms != null)
            {
                return new ModelAndView("redirect:/manager/index.html");
            }
        }
        ImageCode imageCode = CaptchaUtil.buildImageCode(); // 获取图片验证码
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        System.out.println("====================: " + imageCode.getCode());
        ModelAndView view = new ModelAndView("login");
        view.addObject(Field.IMAGE, "data:image/png;base64," + imageCode.getBase64Code());
        return view;
    }
}
