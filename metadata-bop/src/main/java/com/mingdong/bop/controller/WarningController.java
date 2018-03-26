package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.WarningSettingVO;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class WarningController
{
    @Resource
    private SystemService systemService;

    @LoginRequired
    @GetMapping(value = "/warning/setting")
    public RestResp list()
    {
        RestResp res = new RestResp();
        systemService.getWarningSettingList(res);
        return res;
    }

    @LoginRequired
    @PostMapping(value = "/warning/setting/modification")
    public RestResp edit(@RequestParam(value = "upfile", required = false) MultipartFile upfile,
            WarningSettingVO warningSettingVO)
    {
        RestResp resp = new RestResp();
        systemService.updateWarningSetting(upfile, warningSettingVO, resp);
        return resp;
    }

    @LoginRequired
    @PostMapping(value = "/warning/setting/enabled")
    public RestResp edit(@RequestParam(value = Field.ID) Long id, @RequestParam(value = Field.STATUS) Integer status)
    {
        RestResp resp = new RestResp();
        systemService.changeWarningSettingStatus(id, status, resp);
        return resp;
    }

}
