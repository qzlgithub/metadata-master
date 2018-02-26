package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.ImageCode;
import com.mingdong.core.util.CaptchaUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CommonController
{
    @Resource
    private SystemService systemService;

    @GetMapping(value = "/m/captcha/img")
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

    @GetMapping(value = "/system/industry/childList")
    public List<Map<String, Object>> getSubIndustryList(@RequestParam(value = Field.INDUSTRY_ID) Long industryId)
    {
        return systemService.getIndustryList(industryId, TrueOrFalse.TRUE);
    }



    /**
     * 行业字典 - 根据上级行业ID获取下级行业字典
     */
    @GetMapping(value = "/m/dict/sub-industry")
    public List<Map<String, Object>> configIndustry(
            @RequestParam(value = Field.PARENT_ID, defaultValue = "0") Long parentId)
    {
        return systemService.getIndustryMap(parentId, TrueOrFalse.TRUE);
    }
}
