package com.mingdong.mis.model;

import com.mingdong.mis.constant.APIProduct;

public class RequestThread
{
    private static final ThreadLocal<RequestHolder> threadHolder = new ThreadLocal<>();

    public static void init()
    {
        RequestHolder holder = new RequestHolder();
        threadHolder.set(holder);
    }

    private static RequestHolder get()
    {
        return threadHolder.get();
    }

    public static Long getClientId()
    {
        return get().getClientId();
    }

    public static void setClientId(Long clientId)
    {
        get().setClientId(clientId);
    }

    public static Long getUserId()
    {
        return get().getUserId();
    }

    public static void setUserId(Long userId)
    {
        get().setUserId(userId);
    }

    public static Long getProductId()
    {
        return get().getProductId();
    }

    public static void setProductId(Long productId)
    {
        get().setProductId(productId);
    }

    public static Long getClientProductId()
    {
        return get().getClientProductId();
    }

    public static void setClientProductId(Long accountId)
    {
        get().setClientProductId(accountId);
    }

    public static APIProduct getProduct()
    {
        return get().getProduct();
    }

    public static void setProduct(APIProduct product)
    {
        get().setProduct(product);
    }

    public static Integer getBillPlan()
    {
        return get().getBillPlan();
    }

    public static void setBillPlan(Integer billPlan)
    {
        get().setBillPlan(billPlan);
    }

    public static Long getStart()
    {
        return get().getStart();
    }

    public static void setStart(Long start)
    {
        get().setStart(start);
    }

    public static Long getEnd()
    {
        return get().getEnd();
    }

    public static void setEnd(Long end)
    {
        get().setEnd(end);
    }

    public static String getAppSecret()
    {
        return get().getAppSecret();
    }

    public static void setAppSecret(String appSecret)
    {
        get().setAppSecret(appSecret);
    }

    public static String getHost()
    {
        return get().getHost();
    }

    public static void setHost(String ip)
    {
        get().setHost(ip);
    }

    public static MDResp getResp()
    {
        return get().getResp();
    }

    public static void setResp(MDResp resp)
    {
        get().setResp(resp);
    }

    public static void cleanup()
    {
        threadHolder.remove();
    }
}
