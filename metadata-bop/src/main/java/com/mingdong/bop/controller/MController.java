package com.mingdong.bop.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.TrueOrFalse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
    @Resource
    private DefaultKaptcha kaptchaBuilder;

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
        /*ImageCode imageCode = CaptchaUtils.buildImageCode(); // 获取图片验证码
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        ModelAndView view = new ModelAndView("index");
        view.addObject(Field.IMAGE, "data:image/png;base64," + imageCode.getBase64Code());*/
        ModelAndView view = new ModelAndView("index");
        view.addAllObjects(findImageCode(request));
        return view;
    }

    private Map<String, Object> findImageCode(HttpServletRequest request) throws IOException
    {
        Map<String, Object> map = new HashMap<>();
        String txt = StringUtils.getRandomString(4);
        HttpSession session = request.getSession();
        session.setAttribute(Field.IMAGE_CAPTCHA, txt);
        BufferedImage image = kaptchaBuilder.createImage(txt);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String pic64 = encoder.encode(outputStream.toByteArray());
        map.put(Field.IMAGE, "data:image/png;base64," + pic64);
        return map;
    }

    /**
     * 获取图片验证码
     */
    @GetMapping(value = "/m/captcha/img")
    @ResponseBody
    public Map<String, Object> getImageCode(HttpServletRequest request) throws IOException
    {
        /*Map<String, Object> map = new HashMap<>();
        String txt = StringUtils.getRandomString(4);
        HttpSession session = request.getSession();
        session.setAttribute(Field.IMAGE_CAPTCHA, txt);
        BufferedImage image = kaptchaBuilder.createImage(txt);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String pic64 = encoder.encode(outputStream.toByteArray());
        map.put(Field.IMAGE, "data:image/png;base64," + pic64);*/

        /*ImageCode imageCode = CaptchaUtils.buildImageCode();
        Map<String, Object> map = new HashMap<>();
        map.put(Field.IMAGE, "data:image/png;base64," + imageCode.getBase64Code());
        HttpSession session = request.getSession();
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        System.out.println(">>>>>>>>: " + imageCode.getCode());*/
        return findImageCode(request);
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
