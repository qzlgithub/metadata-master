package com.mingdong.mis.model;

public class RequestThread
{
    private static final ThreadLocal<RequestHolder> threadHolder = new ThreadLocal<>();

    public static void set(String requestIp, Long productId, Long clientId, Long userId)
    {
        RequestHolder holder = new RequestHolder();
        holder.setRequestIp(requestIp);
        holder.setProductId(productId);
        holder.setClientId(clientId);
        holder.setUserId(userId);
        threadHolder.set(holder);
    }

    private static RequestHolder get()
    {
        return threadHolder.get();
    }

    public static String getRequestIp()
    {
        return get().getRequestIp();
    }

    public static Long getProductId()
    {
        return get().getProductId();
    }

    public static Long getClientId()
    {
        return get().getClientId();
    }

    public static Long getUserId()
    {
        return get().getUserId();
    }

    public static void cleanup()
    {
        threadHolder.remove();
    }
}
