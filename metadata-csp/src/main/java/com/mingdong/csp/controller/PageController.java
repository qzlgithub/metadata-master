package com.mingdong.csp.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.ProductType;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestResp;
import com.mingdong.csp.component.RedisDao;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.model.UserSession;
import com.mingdong.csp.service.ClientService;
import com.mingdong.csp.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class PageController
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private ClientService clientService;
    @Resource
    private ProductService productService;
    @Resource
    private DefaultKaptcha kaptchaBuilder;

    /**
     * 登陆页
     */
    @GetMapping(value = {"login.html"})
    public ModelAndView loginPage(HttpServletRequest request) throws IOException
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
        ModelAndView view = new ModelAndView("login");
        /*ImageCode imageCode = CaptchaUtils.buildImageCode();
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        view.addObject(Field.IMAGE_CAPTCHA, "data:image/png;base64," + imageCode.getBase64Code());*/
        String txt = StringUtils.getRandomString(4);
        session.setAttribute(Field.IMAGE_CAPTCHA, txt);
        BufferedImage image = kaptchaBuilder.createImage(txt);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String pic64 = encoder.encode(outputStream.toByteArray());
        view.addObject(Field.IMAGE_CAPTCHA, "data:image/png;base64," + pic64);
        return view;
    }

    /**
     * 用户首页
     */
    @LoginRequired
    @GetMapping(value = "/home.html")
    public ModelAndView getHomeData()
    {
        ModelAndView view = new ModelAndView("home");
        RestResp resp = new RestResp();
        clientService.getHomeData(RequestThread.getClientId(), RequestThread.getUserId(), resp);
        view.addAllObjects(resp.getData());
        view.addAllObjects(RequestThread.getPageData());
        view.addObject(Field.IS_PRIMARY, TrueOrFalse.TRUE.equals(RequestThread.getPrimary()));
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/all.html"})
    public ModelAndView productAll()
    {
        ModelAndView view = new ModelAndView("product/all");
        view.addAllObjects(RequestThread.getPageData());
        view.addObject(Field.PRODUCT_TYPE_DICT, ProductType.getProductTypeDict());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/detail.html"})
    public ModelAndView productDetail(@RequestParam(value = Field.ID) Long productId)
    {
        ModelAndView view = new ModelAndView("product/detail");
        RestResp resp = new RestResp();
        productService.getClientProductInfoData(RequestThread.getClientId(), productId, resp);
        view.addAllObjects(resp.getData());
        view.addObject(Field.USERNAME, RequestThread.getUsername());
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/introduce.html"})
    public ModelAndView productIntroduce(@RequestParam(value = Field.ID) Long productId)
    {
        ModelAndView view = new ModelAndView("product/introduce");
        RestResp resp = new RestResp();
        productService.getProductInfo(productId, resp);
        view.addAllObjects(resp.getData());
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/mine.html"})
    public ModelAndView productMine()
    {
        ModelAndView view = new ModelAndView("product/mine");
        RestResp resp = new RestResp();
        productService.getClientProductDetailList(RequestThread.getClientId(), resp);
        view.addAllObjects(resp.getData());
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/recharge.html"})
    public ModelAndView productRecharge(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId)
    {
        ModelAndView view = new ModelAndView("product/recharge");
        List<Dict> productDict = productService.getClientProductDict(RequestThread.getClientId());
        view.addObject(Field.PRODUCT_DICT, productDict);
        if(productId != null)
        {
            view.addObject(Field.PRODUCT_ID, productId + "");
        }
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date lastWeek = calendar.getTime();
        view.addObject(Field.FROM_DATE, DateUtils.format(lastWeek, DateFormat.YYYY_MM_DD));
        view.addObject(Field.TO_DATE, DateUtils.format(today, DateFormat.YYYY_MM_DD));
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/product/request.html"})
    public ModelAndView productRequest(@RequestParam(value = Field.PRODUCT_ID, required = false) Long productId)
    {
        ModelAndView view = new ModelAndView("product/request");
        List<Dict> productDict = productService.getClientProductDict(RequestThread.getClientId());
        view.addObject(Field.PRODUCT_DICT, productDict);
        if(productId != null)
        {
            view.addObject(Field.PRODUCT_ID, productId + "");
        }
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date lastWeek = calendar.getTime();
        view.addObject(Field.FROM_DATE, DateUtils.format(lastWeek, DateFormat.YYYY_MM_DD));
        view.addObject(Field.TO_DATE, DateUtils.format(today, DateFormat.YYYY_MM_DD));
        view.addAllObjects(RequestThread.getPageData());
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/account-list.html"})
    public ModelAndView systemAccountList()
    {
        ModelAndView view = new ModelAndView("system/account-list");
        view.addAllObjects(RequestThread.getPageData());
        view.addObject(Field.IS_PRIMARY, RequestThread.getPrimary() == 1);
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/message.html"})
    public ModelAndView systemMessage()
    {
        ModelAndView view = new ModelAndView("system/message");
        view.addAllObjects(RequestThread.getPageData());
        view.addObject(Field.IS_PRIMARY, RequestThread.getPrimary() == 1);
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/security-setting.html"})
    public ModelAndView securitySetting()
    {
        ModelAndView view = new ModelAndView("system/security-setting");
        view.addAllObjects(RequestThread.getPageData());
        view.addObject(Field.IS_PRIMARY, RequestThread.getPrimary() == 1);
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/edit-pwd.html"})
    public ModelAndView systemPwd()
    {
        ModelAndView view = new ModelAndView("system/edit-pwd");
        view.addAllObjects(RequestThread.getPageData());
        view.addObject(Field.IS_PRIMARY, RequestThread.getPrimary() == 1);
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/edit-validation.html"})
    public ModelAndView systemValidation(@RequestParam(value = Field.P, required = false) Integer p)
    {
        ModelAndView view = new ModelAndView("system/edit-validation");
        view.addAllObjects(RequestThread.getPageData());
        view.addObject(Field.IS_PRIMARY, RequestThread.getPrimary() == 1);
        view.addObject(Field.USERNAME, RequestThread.getUsername());
        view.addObject(Field.P, p);
        return view;
    }

    @LoginRequired
    @GetMapping(value = {"/system/edit-appKey.html"})
    public ModelAndView systemAppKey(@RequestParam(value = Field.P) String pwd)
    {
        boolean isValidate = clientService.validatePassWord(RequestThread.getUserId(), pwd);
        if(!isValidate)
        {
            return new ModelAndView("redirect:/system/edit-validation.html?p=1");
        }
        String appKey = clientService.getAppKeyByUserId(RequestThread.getUserId());
        ModelAndView view = new ModelAndView("system/edit-appKey");
        view.addAllObjects(RequestThread.getPageData());
        view.addObject(Field.IS_PRIMARY, RequestThread.getPrimary() == 1);
        view.addObject(Field.APP_KEY, appKey);
        return view;
    }

}
