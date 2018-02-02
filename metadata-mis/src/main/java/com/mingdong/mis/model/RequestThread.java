package com.mingdong.mis.model;

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

    public static String getIp()
    {
        return get().getIp();
    }

    public static void setIp(String ip)
    {
        get().setIp(ip);
    }

    public static Long getProductId()
    {
        return get().getProductId();
    }

    public static void setProductId(Long productId)
    {
        get().setProductId(productId);
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

    public static MetadataRes getResult()
    {
        return get().getRes();
    }

    public static void setResult(MetadataRes result)
    {
        get().setRes(result);
    }

    public static void cleanup()
    {
        threadHolder.remove();
    }
}
