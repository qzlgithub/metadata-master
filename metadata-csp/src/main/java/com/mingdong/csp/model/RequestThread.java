package com.mingdong.csp.model;

public class RequestThread
{
    private static final ThreadLocal<RequestHolder> threadHolder = new ThreadLocal<>();

    public static void set(Long userId)
    {
        RequestHolder holder = new RequestHolder();
        holder.setUserId(userId);
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

    public static void cleanup()
    {
        threadHolder.remove();
    }
}
