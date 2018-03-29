package com.mingdong.csp.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.model.RestResp;
import com.mingdong.csp.constant.Field;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class CommonController
{
    @Resource
    private DefaultKaptcha kaptchaBuilder;

    @GetMapping(value = "/captcha")
    public RestResp getImageCaptcha(HttpServletRequest request) throws IOException
    {
        RestResp resp = new RestResp();
        HttpSession session = request.getSession();
        /*ImageCode imageCode = CaptchaUtils.buildImageCode();
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        resp.addData(Field.IMAGE_CAPTCHA, "data:image/png;base64," + imageCode.getBase64Code());*/
        String txt = StringUtils.getRandomString(4);
        session.setAttribute(Field.IMAGE_CAPTCHA, txt);
        BufferedImage image = kaptchaBuilder.createImage(txt);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String pic64 = encoder.encode(outputStream.toByteArray());
        resp.addData(Field.IMAGE_CAPTCHA, "data:image/png;base64," + pic64);
        return resp;
    }
}
