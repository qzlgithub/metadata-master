package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ImageCode;
import com.mingdong.bop.util.CaptchaUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "common")
public class CommonController
{

    @RequestMapping(value = "imageCode")
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
}
