package com.mingdong.bop.controller;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.ImageCode;
import com.mingdong.core.util.CaptchaUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MController
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private SystemService systemService;

    @GetMapping(value = {"/", "/index.html", "/login.html"})
    public ModelAndView index(HttpServletRequest request) throws IOException
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        if(!StringUtils.isNullBlank(sessionId))
        {
            ManagerSession ms = redisDao.getManagerSession(sessionId);
            if(ms != null)
            {
                return new ModelAndView("redirect:/stats/index.html");
            }
        }
        ImageCode imageCode = CaptchaUtils.buildImageCode(); // 获取图片验证码
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        ModelAndView view = new ModelAndView("index");
        view.addObject(Field.IMAGE, "data:image/png;base64," + imageCode.getBase64Code());
        return view;
    }

    /**
     * 获取图片验证码
     */
    @GetMapping(value = "/m/captcha/img")
    @ResponseBody
    public Map<String, Object> getImageCode(HttpServletRequest request) throws IOException
    {
        ImageCode imageCode = CaptchaUtils.buildImageCode();
        Map<String, Object> map = new HashMap<>();
        map.put(Field.IMAGE, "data:image/png;base64," + imageCode.getBase64Code());
        HttpSession session = request.getSession();
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        System.out.println(">>>>>>>>: " + imageCode.getCode());
        return map;
    }

    /**
     * 根据上级行业ID获取下级行业字典
     */
    @GetMapping(value = "/m/dict/sub-industry")
    @ResponseBody
    public List<Map<String, Object>> configIndustry(
            @RequestParam(value = Field.PARENT_ID, defaultValue = "0") Long parentId)
    {
        return systemService.getIndustryList(parentId, TrueOrFalse.TRUE);
    }
}
