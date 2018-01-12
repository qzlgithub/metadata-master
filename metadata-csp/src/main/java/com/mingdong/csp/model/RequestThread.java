package com.mingdong.csp.model;

public class RequestThread
{
    private static final ThreadLocal<RequestHolder> threadHolder = new ThreadLocal<>();

    public static void set(Long userId, Long clientId)
    {
        RequestHolder holder = new RequestHolder();
        holder.setUserId(userId);
        holder.setClientId(clientId);
        threadHolder.set(holder);
    }

    private static RequestHolder get()
    {
        return threadHolder.get();
    }

    public static Long getUserId()
    {
        return get().getUserId();
    }

    public static Long getClientId()
    {
        return get().getClientId();
    }

    public static void cleanup()
    {
        threadHolder.remove();
    }
}
