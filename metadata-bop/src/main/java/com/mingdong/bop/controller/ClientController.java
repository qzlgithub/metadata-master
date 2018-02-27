package com.mingdong.bop.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.NewClientVO;
import com.mingdong.bop.model.ProdRechargeVO;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ClientService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.ListRes;
import com.mingdong.core.util.BusinessUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ClientController
{
    @Resource
    private ClientService clientService;

    @LoginRequired
    @GetMapping(value = "/client/check")
    public Map<String, Object> getList(@RequestParam(value = Field.USERNAME) String username)
    {
        RestResp resp = RestResp.build();
        clientService.checkIfUsernameExist(username, resp);
        return resp.getDataMap();
    }

    /**
     * 判断合同编号唯一性
     */
    @LoginRequired
    @GetMapping(value = "/client/checkContract")
    public Map<String, Object> getContractList(@RequestParam(value = Field.CONTRACT_NO) String contractNo)
    {
        RestResp resp = RestResp.build();
        clientService.checkIfContractExist(contractNo, resp);
        return resp.getDataMap();
    }

    /**
     * 客户列表
     */
    @LoginRequired
    @GetMapping(value = "/client/list")
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

    /**
     * 客户产品充值记录
     */
    @LoginRequired
    @GetMapping(value = "/client/recharge/list")
    public ListRes getRechargeList(@RequestParam(value = Field.CLIENT_ID) Long clientId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        fromDate = fromDate == null ? null : BusinessUtils.getDayStartTime(fromDate);
        toDate = toDate == null ? null : BusinessUtils.getLastDayStartTime(toDate);
        clientService.getProductRechargeList(clientId, productId, fromDate, toDate, new Page(pageNum, pageSize), res);
        return res;
    }

    /**
     * 客户接口请求记录
     */
    @LoginRequired
    @GetMapping(value = "/client/request/list")
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

    @LoginRequired
    @GetMapping(value = "/client/sub-user/list")
    public List<Map<String, Object>> getClientSubUserList(@RequestParam(value = Field.ID) Long clientId)
    {
        return clientService.getClientSubUserList(clientId);
    }

    @LoginRequired
    @PutMapping(value = "/client/addition")
    public RestResp addNewClient(@RequestBody NewClientVO vo)
    {
        RestResp resp = RestResp.build();
        clientService.addClient(vo, resp);
        return resp;
    }

    @LoginRequired
    @PostMapping(value = "/client/modification")
    public RestResp editClient(@RequestBody NewClientVO vo)
    {
        RestResp resp = RestResp.build();
        clientService.editClient(vo, resp);
        return resp;
    }

    @LoginRequired
    @PostMapping(value = "/client/status")
    private RestResp changeClientStatus(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
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

    @LoginRequired
    @DeleteMapping(value = "/client/deletion")
    private RestResp deleteClient(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
        JSONArray idArr = jsonReq.getJSONArray(Field.ID);
        List<Long> idList = idArr.toJavaList(Long.class);
        if(CollectionUtils.isEmpty(idList))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.deleteClient(idList);
        return resp;
    }

    @LoginRequired
    @GetMapping(value = "/client/same")
    private Map<String, Object> getSameClient(@RequestParam(value = Field.NAME) String name,
            @RequestParam(value = Field.ID, required = false) Long clientId)
    {
        RestResp resp = RestResp.build();
        clientService.getSimilarCorp(name, clientId, resp);
        return resp.getDataMap();
    }

    @LoginRequired
    @PostMapping(value = "/client/reset")
    private RestResp resetPassword(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
        JSONArray idArr = jsonReq.getJSONArray(Field.ID);
        List<Long> idList = idArr.toJavaList(Long.class);
        if(CollectionUtils.isEmpty(idList))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.resetPassword(idList, resp);
        return resp;
    }

    @LoginRequired
    @PostMapping(value = "/client/product/open")
    public RestResp openProductService(@RequestBody ProdRechargeVO vo)
    {
        RestResp resp = RestResp.build();
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

    @LoginRequired
    @PostMapping(value = "/client/product/select")
    public RestResp productSelect(@RequestParam(value = Field.CLIENT_ID) Long clientId,
            @RequestParam(value = Field.IDS) String ids)
    {
        RestResp resp = RestResp.build();
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

    @LoginRequired
    @PostMapping(value = "/client/product/remove")
    public RestResp productRemove(@RequestParam(value = Field.ID) Long id)
    {
        RestResp resp = RestResp.build();
        clientService.removeCustomClientProduct(id, resp);
        return resp;
    }

    @LoginRequired
    @PostMapping(value = "/client/product/renew")
    public RestResp renewProductService(@RequestBody ProdRechargeVO vo)
    {
        RestResp resp = RestResp.build();
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

    @LoginRequired
    @GetMapping(value = "/client/product/renewInfo")
    public Map<String, Object> getProductRenewInfo(@RequestParam(value = Field.CLIENT_PRODUCT_ID) Long clientProductId)
    {
        RestResp resp = RestResp.build();
        clientService.getProductRenewInfo(clientProductId, resp);
        return resp.getDataMap();
    }

    @LoginRequired
    @GetMapping(value = "/client/operate/log")
    public RestResp getClientOperateLog(@RequestParam(value = Field.ID) Long clientId,
            @RequestParam(value = Field.PAGE_NUM, required = false) int pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) int pageSize)
    {
        RestResp resp = RestResp.build();
        clientService.getClientOperateLog(clientId, new Page(pageNum, pageSize), resp);
        return resp;
    }
}
