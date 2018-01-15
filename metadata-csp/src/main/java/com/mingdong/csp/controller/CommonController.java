package com.mingdong.csp.controller;

import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ImageCode;
import com.mingdong.core.util.CaptchaUtils;
import com.mingdong.csp.constant.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class CommonController
{
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @GetMapping(value = "captcha")
    public BLResp getImageCaptcha(HttpServletRequest request) throws IOException
    {
        BLResp resp = BLResp.build();
        HttpSession session = request.getSession();
        ImageCode imageCode = CaptchaUtils.buildImageCode();
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        resp.addData(Field.IMAGE_CAPTCHA, "data:image/png;base64," + imageCode.getBase64Code());
        return resp;
    }
}
