package com.mingdong.csp.controller;

import com.mingdong.core.model.ImageCode;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.util.CaptchaUtils;
import com.mingdong.csp.constant.Field;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class CommonController
{
    @GetMapping(value = "/captcha")
    public RestResp getImageCaptcha(HttpServletRequest request) throws IOException
    {
        RestResp resp = new RestResp();
        HttpSession session = request.getSession();
        ImageCode imageCode = CaptchaUtils.buildImageCode();
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        resp.addData(Field.IMAGE_CAPTCHA, "data:image/png;base64," + imageCode.getBase64Code());
        return resp;
    }
}
