package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ClientService;
import com.mingdong.common.model.Page;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestListResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RemindController
{
    @Resource
    private ClientService clientService;

    @LoginRequired
    @GetMapping(value = "/remind/list/date")
    public RestListResp getListDate(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.DISPOSE, required = false) Integer dispose,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp resp = new RestListResp();
        clientService.getClientRemindForDate(null, keyword, dispose, new Page(pageNum, pageSize), resp);
        return resp;
    }

    @LoginRequired
    @GetMapping(value = "/remind/list/time")
    public RestListResp getListTime(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.DISPOSE, required = false) Integer dispose,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp resp = new RestListResp();
        clientService.getClientRemindForTimes(null, keyword, dispose, new Page(pageNum, pageSize), resp);
        return resp;
    }
}
