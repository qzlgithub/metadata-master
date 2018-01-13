package com.mingdong.csp.service.impl;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import com.mingdong.csp.service.ProductService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductServiceImpl implements ProductService // TODO 余振阳
{
    @Override
    public void getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page,
            BLResp resp)
    {

    }

    @Override
    public XSSFWorkbook createProductRechargeXlsx(Long clientId, Long productId, Date fromDate, Date endDate)
    {
        return null;
    }

    @Override
    public void getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page,
            BLResp resp)
    {

    }

    @Override
    public XSSFWorkbook createProductRequestXlsx(Long clientId, Long productId, Date fromDate, Date endDate)
    {
        return null;
    }
}
