package com.mingdong.csp.controller;

import com.mingdong.common.util.StringUtils;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.util.CaptchaUtils;
import com.mingdong.csp.constant.Field;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;

@RestController
public class CommonController
{
    @GetMapping(value = "/captcha")
    public RestResp getImageCaptcha(HttpServletRequest request)
    {
        RestResp resp = new RestResp();
        HttpSession session = request.getSession();
        String txt = StringUtils.getRandomString(4);
        session.setAttribute(Field.IMAGE_CAPTCHA, txt);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CaptchaUtils.outputWithText(txt, outputStream, 120, 35);
        String pic64 = Base64.encodeBase64String(outputStream.toByteArray());
        resp.addData(Field.IMAGE_CAPTCHA, "data:image/png;base64," + pic64);
        return resp;
    }
}
