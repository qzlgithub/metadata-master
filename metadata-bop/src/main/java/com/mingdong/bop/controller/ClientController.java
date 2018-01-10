 package com.mingdong.bop.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.BillPlan;
import com.mingdong.bop.constant.Constant;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ClientVO;
import com.mingdong.bop.model.NewClientVO;
import com.mingdong.bop.model.ProdRechargeVO;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.ProductService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.RequestThread;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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
    private ProductService productService;
    @Resource
    private SystemService systemService;
    @Resource
    private TradeService tradeService;

    /**
     * 页面：客户列表
     */
    @RequestMapping(value = "index.html")
    public ModelAndView clientIndexPage()
    {
        ModelAndView view = new ModelAndView("customer-manage/customer-index");
        List<Map<String, Object>> industryList = systemService.getIndustryList(0L, TrueOrFalse.TRUE);
        view.addObject(Field.INDUSTRY_LIST, industryList);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户添加页面
     */
    @RequestMapping(value = "addition.html")
    public ModelAndView gotoClientAddition()
    {
        ModelAndView view = new ModelAndView("customer-manage/customer-add");
        view.addAllObjects(systemService.getInitIndustryMap());
        view.addObject(Field.DEFAULT_PASSWORD, Constant.DEFAULT_PASSWORD);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户编辑页面
     */
    @RequestMapping(value = "edit.html")
    public ModelAndView gotoClientEdit(@RequestParam(value = Field.CLIENT_ID) Long clientId)
    {
        ModelAndView view = new ModelAndView("customer-manage/customer-edit");
        Map<String, Object> clientMap = clientService.findClientInfo(clientId);
        view.addAllObjects(clientMap);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户详情页面
     */
    @RequestMapping(value = "detail.html")
    public ModelAndView gotoClientDetail(@RequestParam(value = Field.CLIENT_ID) Long clientId)
    {
        ModelAndView view = new ModelAndView("customer-manage/customer-detail");
        Map<String, Object> clientMap = clientService.findClientDetail(clientId);
        view.addAllObjects(clientMap);
        view.addObject(Field.RECHARGE_DICT, systemService.getRechargeDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 充值记录
     */
    @RequestMapping(value = "product/recharge.html")
    public ModelAndView gotoRechargeRecord(@RequestParam(value = Field.ID) Long clientProductId)
    {
        ModelAndView view = new ModelAndView("customer-manage/product-recharge");
        view.addAllObjects(clientService.getClientProductInfo(clientProductId));
        view.addObject(Field.PRODUCT_DICT, productService.getProductDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 消费明细
     */
    @RequestMapping(value = "product/consume.html")
    public ModelAndView gotoConsumptionDetail(@RequestParam(value = Field.ID) Long clientProductId)
    {
        ModelAndView view = new ModelAndView("customer-manage/product-consume");
        view.addAllObjects(clientService.getClientProductInfo(clientProductId));
        view.addObject(Field.PRODUCT_DICT, productService.getProductDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 账户充值
     */
    @RequestMapping(value = "account/recharge.html")
    public ModelAndView gotoAccountRecharge(@RequestParam(value = Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("customer-manage/account-recharge");
        view.addObject("clientId", id);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 账户消费
     */

    @RequestMapping(value = "account/consume.html")
    public ModelAndView gotoAccountConsumption(@RequestParam(value = Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("customer-manage/account-consume");
        view.addObject("clientId", id);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "check", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(value = Field.USERNAME) String username)
    {
        BLResp resp = BLResp.build();
        clientService.checkIfUsernameExist(username, resp);
        return resp.getDataMap();
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(value = Field.ENABLED, required = false) Integer enabled,
            @RequestParam(value = Field.PARENT_INDUSTRY_ID, required = false) Long parentIndustryId,
            @RequestParam(value = Field.INDUSTRY_ID, required = false) Long industryId,
            @RequestParam(value = Field.CORP_NAME, required = false) String corpName,
            @RequestParam(value = Field.SHORT_NAME, required = false) String shortName,
            @RequestParam(value = Field.USERNAME, required = false) String username,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        Page page = new Page(pageNum, pageSize);
        if(!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled))
        {
            enabled = null;
        }
        corpName = StringUtils.isNullBlank(corpName) ? null : corpName.trim();
        shortName = StringUtils.isNullBlank(shortName) ? null : shortName.trim();
        username = StringUtils.isNullBlank(username) ? null : username.trim();
        BLResp resp = clientService.getCorp(enabled, username, corpName, shortName, parentIndustryId, industryId, page);
        return resp.getDataMap();
    }

    @RequestMapping(value = "rechargeList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getRechargeList(@RequestParam(value = Field.CLIENT_ID) Long clientId,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.START_TIME, required = false) Date startTime,
            @RequestParam(value = Field.END_TIME, required = false) Date endTime,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        tradeService.getProductRechargeList(clientId, productId, startTime, endTime, new Page(pageNum, pageSize), resp);
        return resp.getDataMap();
    }

    @RequestMapping(value = "consumeList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getConsumeList(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.CLIENT_ID, required = false) Long clientId,
            @RequestParam(value = Field.CREATE_TIME, required = false) Date time,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        // TODO FIX...
        Page page = new Page(pageNum, pageSize);
        //BLResp resp = productRechargeService.getProductRechargeList(productId,clientId,time,page);//clientService.getCorp(username, corpName, shortNamepage);
        BLResp resp = tradeService.testList2(productId, clientId, time, page);
        return resp.getDataMap();

    }

    /**
     * 账户充值列表
     */

    @RequestMapping(value = "account/rechargeList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAccountRechargeList(
            @RequestParam(value = Field.CLIENT_ID, required = false) Long clientId,
            @RequestParam(value = Field.CREATE_TIME, required = false) Date time,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        Page page = new Page(pageNum, pageSize);
        //BLResp resp = productRechargeService.getProductRechargeList(productId,clientId,time,page);//clientService.getCorp(username, corpName, shortNamepage);
        BLResp resp = tradeService.testList3(clientId, time, page);
        return resp.getDataMap();
    }

    /**
     * 账户消费列表
     */

    @RequestMapping(value = "account/consumeList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAccountconsumeList(
            @RequestParam(value = Field.CLIENT_ID, required = false) Long clientId,
            @RequestParam(value = Field.CREATE_TIME, required = false) Date time,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        Page page = new Page(pageNum, pageSize);
        //BLResp resp = productRechargeService.getProductRechargeList(productId,clientId,time,page);//clientService.getCorp(username, corpName, shortNamepage);
        BLResp resp = tradeService.testList4(clientId, time, page);
        return resp.getDataMap();
    }

    @RequestMapping(value = "subAccount/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getSubAccountList(@RequestParam(value = Field.ID) Long clientId)
    {
        return clientService.getSubAccountList(clientId);
    }

    @RequestMapping(value = "addition", method = RequestMethod.POST)
    @ResponseBody
    public BLResp addNewClient(@RequestBody NewClientVO vo)
    {
        BLResp resp = BLResp.build();
        if(StringUtils.isNullBlank(vo.getUsername()) || StringUtils.isNullBlank(vo.getPassword()) ||
                StringUtils.isNullBlank(vo.getCorpName()) || StringUtils.isNullBlank(vo.getShortName()) ||
                StringUtils.isNullBlank(vo.getName()) || StringUtils.isNullBlank(vo.getPhone()) ||
                StringUtils.isNullBlank(vo.getEmail()) || StringUtils.isNullBlank(vo.getLicense()))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(vo.getIndustryId() == null || vo.getIndustryId() <= 0)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(!TrueOrFalse.TRUE.equals(vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled()))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.addCorp(vo.getUsername(), vo.getPassword(), vo.getCorpName(), vo.getShortName(),
                vo.getIndustryId(), vo.getName(), vo.getPhone(), vo.getEmail(), vo.getLicense(), vo.getEnabled(), resp);
        return resp;
    }

    @RequestMapping(value = "modification", method = RequestMethod.POST)
    @ResponseBody
    public BLResp editClient(@RequestBody ClientVO vo)
    {
        BLResp resp = BLResp.build();
        if(vo.getClientId() == null || vo.getIndustryId() == null || vo.getUserEnabled() == null ||
                vo.getAccountEnabled() == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(StringUtils.isNullBlank(vo.getCorpName()) || StringUtils.isNullBlank(vo.getShortName()) ||
                StringUtils.isNullBlank(vo.getName()))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.editClientInfo(vo.getClientId(), vo.getCorpName(), vo.getShortName(), vo.getLicense(),
                vo.getIndustryId(), vo.getName(), vo.getPhone(), vo.getEmail(), vo.getUserEnabled(),
                vo.getAccountEnabled(), resp);
        return resp;
    }

    @RequestMapping(value = "changeStatus", method = RequestMethod.POST)
    @ResponseBody
    private BLResp changeStatus(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        JSONArray idArr = jsonReq.getJSONArray(Field.ID);
        List<Long> idList = idArr.toJavaList(Long.class);
        if(CollectionUtils.isEmpty(idList))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled))
        {
            enabled = null;
        }
        clientService.changeClientStatus(idList, enabled, resp);
        return resp;
    }

    @RequestMapping(value = "deletion", method = RequestMethod.POST)
    @ResponseBody
    private BLResp setClientDeleted(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        JSONArray idArr = jsonReq.getJSONArray(Field.ID);
        List<Long> idList = idArr.toJavaList(Long.class);
        if(CollectionUtils.isEmpty(idList))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.setClientDeleted(idList, resp);
        return resp;
    }

    @RequestMapping(value = "same", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getSameClient(@RequestParam(value = Field.NAME) String name,
            @RequestParam(value = Field.ID, required = false) Long clientId)
    {
        BLResp resp = BLResp.build();
        clientService.getSimilarCorp(name, clientId, resp);
        return resp.getDataMap();
    }

    @RequestMapping(value = "resetPwd", method = RequestMethod.POST)
    @ResponseBody
    private BLResp resetClientPassword(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        JSONArray idArr = jsonReq.getJSONArray(Field.ID);
        List<Long> idList = idArr.toJavaList(Long.class);
        if(CollectionUtils.isEmpty(idList))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.resetClientPassword(idList, resp);
        return resp;
    }

    @RequestMapping(value = "user/resetPwd", method = RequestMethod.POST)
    @ResponseBody
    private BLResp resetClientUserPassword(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long clientUserId = jsonReq.getLong(Field.ID);
        if(clientUserId == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        clientService.resetClientUserPassword(clientUserId, resp);
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
        if(billPlan == BillPlan.YEAR)
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
        if(billPlan == BillPlan.YEAR)
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

    @GetMapping(value = "operateLog")
    @ResponseBody
    public BLResp getClientOperateLog(@RequestParam(value = Field.ID) Long clientId,
            @RequestParam(value = Field.PAGE_NUM, required = false) int pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) int pageSize)
    {
        BLResp resp = BLResp.build();
        clientService.getClientOperateLog(clientId, new Page(pageNum, pageSize), resp);
        return resp;
    }
}
