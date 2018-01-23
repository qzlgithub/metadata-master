package com.mingdong.mis.controller;

import com.mingdong.core.model.BLResp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api")
public class DataController
{
    @RequestMapping(value = "p1")
    public BLResp getP1Data()
    {
        BLResp resp = BLResp.build();

        return resp;
    }
}
