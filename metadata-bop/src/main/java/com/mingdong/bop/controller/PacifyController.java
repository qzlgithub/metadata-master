package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.model.Page;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class PacifyController
{
    @Resource
    private SystemService systemService;

    @LoginRequired
    @GetMapping(value = "/pacify/list")
    public RestListResp pacifyList(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.STATUS, required = false) Integer errorStatus,
            @RequestParam(value = Field.DISPOSE, required = false) Integer dispose,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
        systemService.getWarningPacifyInfoList(keyword, errorStatus, dispose, fromDate, toDate, res,
                new Page(pageNum, pageSize));
        return res;
    }

    @LoginRequired
    @PostMapping(value = "/pacify/dispose")
    public RestResp pacifyDispose(@RequestParam(value = Field.ID) Long pacifyId,
            @RequestParam(value = Field.REMARK) String remark)
    {
        RestResp resp = new RestResp();
        systemService.updatePacifyDispose(pacifyId, remark, resp);
        return resp;
    }
}
