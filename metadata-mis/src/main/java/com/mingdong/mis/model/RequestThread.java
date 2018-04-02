package com.mingdong.mis.model;

import com.mingdong.mis.constant.APIProduct;

public class RequestThread
{
    private static final ThreadLocal<RequestHolder> threadHolder = new ThreadLocal<>();

    public static void init()
    {
        RequestHolder holder = new RequestHolder();
        holder.setTimestamp(System.currentTimeMillis());
        threadHolder.set(holder);
    }

    private static RequestHolder get()
    {
        return threadHolder.get();
    }

    public static void cleanup()
    {
        threadHolder.remove();
    }

    public static void setAccessInfo(UserAuth userAuth)
    {
        get().setUserAuth(userAuth);
        get().setProduct(APIProduct.getByCode(userAuth.getProduct()));
    }

    public static APIProduct getProduct()
    {
        return get().getProduct();
    }

    public static Long getClientId()
    {
        return get().getUserAuth().getClientId();
    }

    public static String getCorpName()
    {
        return get().getUserAuth().getCorpName();
    }

    public static String getPrimaryUsername()
    {
        return get().getUserAuth().getPrimaryUsername();
    }

    public static Long getUserId()
    {
        return get().getUserAuth().getUserId();
    }

    public static String getUsername()
    {
        return get().getUserAuth().getUsername();
    }

    public static Long getProductId()
    {
        return get().getUserAuth().getProductId();
    }

    public static String getProductName()
    {
        return get().getUserAuth().getProductName();
    }

    public static Long getClientProductId()
    {
        return get().getUserAuth().getClientProductId();
    }

    public static Integer getBillPlan()
    {
        return get().getUserAuth().getBillPlan();
    }

    public static Long getStart()
    {
        return get().getUserAuth().getStart();
    }

    public static Long getEnd()
    {
        return get().getUserAuth().getEnd();
    }

    public static String getSecretKey()
    {
        return get().getUserAuth().getSecretKey();
    }

    public static String getHost()
    {
        return get().getUserAuth().getHost();
    }

    public static MDResp getResp()
    {
        return get().getResp();
    }

    public static void setResp(MDResp resp)
    {
        get().setResp(resp);
    }

    public static long getTimestamp()
    {
        return get().getTimestamp();
    }
}
