package com.mingdong.bop.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.NewClientVO;
import com.mingdong.bop.model.ProdRechargeVO;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
import com.mingdong.core.util.BusinessUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "client")
public class ClientController
{
    @Resource
    private ClientService clientService;
    @Resource
    private TradeService tradeService;

    @GetMapping(value = "check")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(value = Field.USERNAME) String username)
    {
        BLResp resp = BLResp.build();
        clientService.checkIfUsernameExist(username, resp);
        return resp.getDataMap();
    }

    /**
     * 判断合同编号唯一性
     */
    @GetMapping(value = "checkContract")
    @ResponseBody
    public Map<String, Object> getContractList(@RequestParam(value = Field.CONTRACT_NO) String contractNo)
    {
        BLResp resp = BLResp.build();
        clientService.checkIfContractExist(contractNo, resp);
        return resp.getDataMap();
    }

    @GetMapping(value = "list")
    @ResponseBody
    public ListRes getClientList(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.PARENT_INDUSTRY_ID, required = false) Long parentIndustryId,
            @RequestParam(value = Field.INDUSTRY_ID, required = false) Long industryId,
            @RequestParam(value = Field.ENABLED, required = false) Integer enabled,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        Page page = new Page(pageNum, pageSize);
        if(!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled))
        {
            enabled = null;
        }
        keyword = StringUtils.isNullBlank(keyword) ? null : keyword.trim();
        clientService.getClientList(keyword, parentIndustryId, industryId, enabled, page, res);
        return res;
    }

    @GetMapping(value = "rechargeList")
    @ResponseBody
    public ListRes getRechargeList(@RequestParam(value = Field.CLIENT_ID) Long clientId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.START_TIME, required = false) Date startTime,
            @RequestParam(value = Field.END_TIME, required = false) Date endTime,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        tradeService.getProductRechargeList(clientId, productId, startTime, endTime, new Page(pageNum, pageSize), res);
        return res;
    }

    @GetMapping(value = "/request/list")
    @ResponseBody
    public ListRes getClientRequestList(@RequestParam(value = Field.CLIENT_ID) Long clientId,
            @RequestParam(value = Field.USER_ID, required = false) Long userId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.START_TIME, required = false) Date startTime,
            @RequestParam(value = Field.END_TIME, required = false) Date endTime,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        startTime = startTime == null ? null : BusinessUtils.getDayStartTime(startTime);
        endTime = endTime == null ? null : BusinessUtils.getLastDayStartTime(endTime);
        clientService.getClientRequestList(clientId, userId, productId, startTime, endTime, new Page(pageNum, pageSize),
                res);
        return res;

    }

    @GetMapping(value = "userConsumeList")
    @ResponseBody
    public ListRes getUserConsumeList(@RequestParam(value = Field.USER_ID) Long userId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.START_TIME, required = false) Date startTime,
            @RequestParam(value = Field.END_TIME, required = false) Date endTime,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        startTime = startTime == null ? null : BusinessUtils.getDayStartTime(startTime);
        endTime = endTime == null ? null : BusinessUtils.getLastDayStartTime(endTime);
        tradeService.getClientBillList(null, null, null, userId, productId, startTime, endTime,
                new Page(pageNum, pageSize), res);
        return res;

    }

    @GetMapping(value = "/request/export")
    public void exportConsumeList(@RequestParam(value = Field.CLIENT_ID) Long clientId,
            @RequestParam(value = Field.USER_ID, required = false) Long userId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate, HttpServletResponse response)
            throws IOException
    {

        XSSFWorkbook wb = tradeService.createClientBillListXlsx(clientId, userId, productId, fromDate, toDate,
                new Page(1, 1000));
        String filename = new String("消费记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @GetMapping(value = "/userConsumeList/export")
    public void exportUserConsumeList(@RequestParam(value = Field.USER_ID) Long userId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.START_TIME, required = false) Date startTime,
            @RequestParam(value = Field.END_TIME, required = false) Date endTime, HttpServletResponse response)
            throws IOException
    {
        XSSFWorkbook wb = tradeService.createClientBillListXlsx(null, null, null, userId, productId, startTime, endTime,
                new Page(1, 1000));
        String filename = new String("消费记录".getBytes(), "ISO8859-1");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        response.setContentType("application/vnd.ms-excel");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @GetMapping(value = "subAccount/list")
    @ResponseBody
    public List<Map<String, Object>> getSubAccountList(@RequestParam(value = Field.ID) Long clientId)
    {
        return clientService.getSubAccountList(clientId);
    }

    @PutMapping(value = "addition")
    @ResponseBody
    public BLResp addNewClient(@RequestBody NewClientVO vo)
    {
        BLResp resp = BLResp.build();
        clientService.addClient(vo, resp);
        return resp;
    }

    @PostMapping(value = "modification")
    @ResponseBody
    public BLResp editClient(@RequestBody NewClientVO vo)
    {
        BLResp resp = BLResp.build();
        clientService.editClient(vo, resp);
        return resp;
    }

    @PostMapping(value = "status")
    @ResponseBody
    private BLResp changeClientStatus(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        JSONArray idArr = jsonReq.getJSONArray(Field.ID);
        List<Long> idList = idArr.toJavaList(Long.class);
        Integer status = jsonReq.getInteger(Field.STATUS);
        String reason = jsonReq.getString(Field.REASON);
        if(CollectionUtils.isEmpty(idList) || (!TrueOrFalse.TRUE.equals(status) && !TrueOrFalse.FALSE.equals(status)) ||
                StringUtils.isNullBlank(reason))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.changeClientStatus(idList, status, reason, RequestThread.getOperatorId(), resp);
        return resp;
    }

    @DeleteMapping(value = "")
    @ResponseBody
    private BLResp deleteClient(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        JSONArray idArr = jsonReq.getJSONArray(Field.ID);
        List<Long> idList = idArr.toJavaList(Long.class);
        if(CollectionUtils.isEmpty(idList))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.deleteClient(idList);
        return resp;
    }

    @GetMapping(value = "same")
    @ResponseBody
    private Map<String, Object> getSameClient(@RequestParam(value = Field.NAME) String name,
            @RequestParam(value = Field.ID, required = false) Long clientId)
    {
        BLResp resp = BLResp.build();
        clientService.getSimilarCorp(name, clientId, resp);
        return resp.getDataMap();
    }

    @PostMapping(value = "reset")
    @ResponseBody
    private BLResp resetPassword(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        JSONArray idArr = jsonReq.getJSONArray(Field.ID);
        List<Long> idList = idArr.toJavaList(Long.class);
        if(CollectionUtils.isEmpty(idList))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.resetPassword(idList, resp);
        return resp;
    }

    @PostMapping(value = "product/open")
    @ResponseBody
    public BLResp openProductService(@RequestBody ProdRechargeVO vo)
    {
        BLResp resp = BLResp.build();
        if(vo.getClientId() == null || vo.getProductId() == null || vo.getBillPlan() == null ||
                vo.getRechargeType() == null || StringUtils.isNullBlank(vo.getContractNo()) || vo.getAmount() == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        BillPlan billPlan = BillPlan.getById(vo.getBillPlan());
        if(billPlan == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(billPlan == BillPlan.BY_TIME)
        {
            if(vo.getStartDate() == null || vo.getEndDate() == null)
            {
                return resp.result(RestResult.KEY_FIELD_MISSING);
            }
            clientService.openProductService(vo.getClientId(), vo.getProductId(), vo.getContractNo(), vo.getBillPlan(),
                    vo.getRechargeType(), vo.getAmount(), vo.getStartDate(), vo.getEndDate(), vo.getRemark(), resp);
        }
        else
        {
            if(vo.getUnitAmt() == null)
            {
                return resp.result(RestResult.KEY_FIELD_MISSING);
            }
            clientService.openProductService(vo.getClientId(), vo.getProductId(), vo.getContractNo(), vo.getBillPlan(),
                    vo.getRechargeType(), vo.getAmount(), vo.getUnitAmt(), vo.getRemark(), resp);
        }
        return resp;
    }

    @PostMapping(value = "product/select")
    @ResponseBody
    public BLResp productSelect(@RequestParam(value = Field.CLIENT_ID) Long clientId,
            @RequestParam(value = Field.IDS) String ids)
    {
        BLResp resp = BLResp.build();
        String[] split;
        List<Long> productIds = new ArrayList<>();
        if(!StringUtils.isNullBlank(ids))
        {
            split = ids.split(",");
            for(String item : split)
            {
                productIds.add(Long.valueOf(item));
            }
        }
        clientService.selectCustomProduct(clientId, productIds, resp);
        return resp;
    }

    @PostMapping(value = "product/remove")
    @ResponseBody
    public BLResp productRemove(@RequestParam(value = Field.ID) Long id)
    {
        BLResp resp = BLResp.build();
        clientService.removeCustomClientProduct(id, resp);
        return resp;
    }

    @PostMapping(value = "product/renew")
    @ResponseBody
    public BLResp renewProductService(@RequestBody ProdRechargeVO vo)
    {
        BLResp resp = BLResp.build();
        if(vo.getClientProductId() == null || vo.getBillPlan() == null || vo.getRechargeType() == null ||
                StringUtils.isNullBlank(vo.getContractNo()) || vo.getAmount() == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        BillPlan billPlan = BillPlan.getById(vo.getBillPlan());
        if(billPlan == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(billPlan == BillPlan.BY_TIME)
        {
            if(vo.getStartDate() == null || vo.getEndDate() == null)
            {
                return resp.result(RestResult.KEY_FIELD_MISSING);
            }
            clientService.renewProductService(vo.getClientProductId(), vo.getContractNo(), vo.getBillPlan(),
                    vo.getRechargeType(), vo.getAmount(), vo.getStartDate(), vo.getEndDate(), vo.getRemark(), resp);
        }
        else
        {
            if(vo.getUnitAmt() == null)
            {
                return resp.result(RestResult.KEY_FIELD_MISSING);
            }
            clientService.renewProductService(vo.getClientProductId(), vo.getContractNo(), vo.getBillPlan(),
                    vo.getRechargeType(), vo.getAmount(), vo.getUnitAmt(), vo.getRemark(), resp);
        }
        return resp;
    }

    @GetMapping(value = "product/renewInfo")
    @ResponseBody
    public Map<String, Object> getProductRenewInfo(@RequestParam(value = Field.CLIENT_PRODUCT_ID) Long clientProductId)
    {
        BLResp resp = BLResp.build();
        clientService.getProductRenewInfo(clientProductId, resp);
        return resp.getDataMap();
    }

    @GetMapping(value = "operate/log")
    @ResponseBody
    public BLResp getClientOperateLog(@RequestParam(value = Field.ID) Long clientId,
            @RequestParam(value = Field.PAGE_NUM, required = false) int pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) int pageSize)
    {
        BLResp resp = BLResp.build();
        clientService.getClientOperateLog(clientId, new Page(pageNum, pageSize), resp);
        return resp;
    }

    @GetMapping(value = "product/recharge/export")
    public void exportProductRechargeRecord(@RequestParam(value = Field.CLIENT_ID) Long clientId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.START_TIME, required = false) Date startTime,
            @RequestParam(value = Field.END_TIME, required = false) Date endTime, HttpServletResponse response)
            throws IOException
    {
        XSSFWorkbook wb = clientService.createProductRechargeXlsx(clientId, productId, startTime, endTime);
        String filename = new String("产品充值记录".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
