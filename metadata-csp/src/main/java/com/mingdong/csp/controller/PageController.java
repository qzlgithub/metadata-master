package com.mingdong.csp.controller;

import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ImageCode;
import com.mingdong.core.util.CaptchaUtils;
import com.mingdong.csp.component.RedisDao;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.model.UserSession;
import com.mingdong.csp.service.ClientService;
import com.mingdong.csp.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class PageController
{
    private static Logger logger = LoggerFactory.getLogger(PageController.class);
    @Resource
    private RedisDao redisDao;
    @Resource
    private ClientService clientService;
    @Resource
    private ProductService productService;

    /**
     * 登陆页
     */
    @GetMapping(value = {"/", "index.html"})
    public ModelAndView indexPage(HttpServletRequest request) throws IOException
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        if(!StringUtils.isNullBlank(sessionId))
        {
            UserSession ms = redisDao.getUserSession(sessionId);
            if(ms != null)
            {
                return new ModelAndView("redirect:/home.html");
            }
        }
        ModelAndView view = new ModelAndView("index");
        ImageCode imageCode = CaptchaUtils.buildImageCode();
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        view.addObject(Field.IMAGE_CAPTCHA, "data:image/png;base64," + imageCode.getBase64Code());
        return view;
    }

    /**
     * 用户首页
     */
    @LoginRequired
    @GetMapping(value = "home.html")
    public ModelAndView getHomeData()
    {
        ModelAndView view = new ModelAndView("home");
        BLResp resp = BLResp.build();
        clientService.getHomeData(RequestThread.getClientId(), RequestThread.getUserId(), resp);
        view.addAllObjects(resp.getDataMap());
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/all.html"})
    public ModelAndView productAll()
    {
        ModelAndView view = new ModelAndView("/product/all");
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/detail.html"})
    public ModelAndView productDetail(@RequestParam(value = Field.ID) Long productId)
    {
        ModelAndView view = new ModelAndView("/product/detail");
        BLResp resp = BLResp.build();
        productService.getClientProductDetail(RequestThread.getClientId(), productId, resp);
        view.addAllObjects(resp.getDataMap());
        view.addObject(Field.USERNAME, RequestThread.getUsername());
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/introduce.html"})
    public ModelAndView productIntroduce()
    {
        ModelAndView view = new ModelAndView("/product/introduce");
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/mine.html"})
    public ModelAndView productMine()
    {
        ModelAndView view = new ModelAndView("/product/mine");
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/recharge.html"})
    public ModelAndView productRecharge(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId)
    {
        ModelAndView view = new ModelAndView("/product/recharge");
        List<Map<String, Object>> productList = productService.getClientProductList(RequestThread.getClientId());
        view.addObject(Field.PRODUCT_LIST, productList);
        if(productId != null)
        {
            view.addObject(Field.PRODUCT_ID, productId + "");
        }
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/request.html"})
    public ModelAndView productRequest(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId)
    {
        ModelAndView view = new ModelAndView("/product/request");
        if(productId != null)
        {
            view.addObject(Field.PRODUCT_ID, productId + "");
        }
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    /*@LoginRequired
    @GetMapping(value = {"/product/recharge.html"})
    public ModelAndView productRecharge(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ModelAndView view = new ModelAndView("/product/recharge");
        BLResp resp = BLResp.build();
        productService.getProductRechargeRecord(RequestThread.getClientId(), productId, fromDate, toDate,
                new Page(pageNum, pageSize), resp);
        view.addAllObjects(resp.getDataMap());
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/request.html"})
    public ModelAndView productRequest(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ModelAndView view = new ModelAndView("/product/request");
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }*/

    @LoginRequired
    @GetMapping(value = {"/system/account-list.html"})
    public ModelAndView systemAccountList()
    {
        ModelAndView view = new ModelAndView("/system/account-list");
        view.addAllObjects(RequestThread.getPageData());
        view.addObject("primary", RequestThread.getPrimary() == 1);
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/account-set.html"})
    public ModelAndView systemAccountSet()
    {
        ModelAndView view = new ModelAndView("/system/account-set");
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/message.html"})
    public ModelAndView systemMessage()
    {
        ModelAndView view = new ModelAndView("/system/message");
        view.addAllObjects(RequestThread.getPageData());
        view.addObject(Field.IS_PRIMARY, RequestThread.getPrimary() == 1);
        return view;
    }

}
