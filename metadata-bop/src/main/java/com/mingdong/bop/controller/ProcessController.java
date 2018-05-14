package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.model.Page;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class ProcessController
{
    @Resource
    private SystemService systemService;

    @LoginRequired
    @GetMapping(value = "/alarm/wait/list")
    public RestListResp alarmWaitList(@RequestParam(value = Field.TYPE, required = false) Integer type,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
        Page page = new Page(pageNum, pageSize);
        systemService.getWarningManageListByWarningType(type, null, null, null, TrueOrFalse.FALSE, res, page);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/alarm/already/list")
    public RestListResp alarmAlreadyList(@RequestParam(value = Field.TYPE, required = false) Integer type,
            @RequestParam(value = Field.MANAGER_ID, required = false) Long managerId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
        Page page = new Page(pageNum, pageSize);
        systemService.getWarningManageListByWarningType(type, managerId, fromDate, toDate, TrueOrFalse.TRUE, res, page);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/alarm/detail/list")
    public RestListResp alarmDetailList(@RequestParam(value = Field.ID, required = false) Long id,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
        Page page = new Page(pageNum, pageSize);
        systemService.getWarningDetailListByManageId(id, page, res);
        return res;
    }

    @LoginRequired
    @PostMapping(value = "/alarm/dispose")
    public RestResp alarmDispose(@RequestParam(value = Field.ID, required = false) Long id,
            @RequestParam(value = Field.REMARK, required = false) String remark)
    {
        RestResp res = new RestListResp();
        systemService.updateWarningManageDispose(id, remark, res);
        return res;
    }
}
