package com.mingdong.csp.model;

import com.mingdong.csp.constant.Field;
import com.mingdong.csp.constant.PathPage;

import java.util.HashMap;
import java.util.Map;

public class RequestThread
{
    private static final ThreadLocal<RequestHolder> threadHolder = new ThreadLocal<>();

    public static void set(Long clientId, Long userId)
    {
        RequestHolder holder = new RequestHolder();
        holder.setClientId(clientId);
        holder.setUserId(userId);
        threadHolder.set(holder);
    }

    private static RequestHolder get()
    {
        return threadHolder.get();
    }

    public static void setCurrPage(String page)
    {
        get().setCurrPage(page);
    }

    public static Long getUserId()
    {
        return get().getUserId();
    }

    public static Long getClientId()
    {
        return get().getClientId();
    }

    public static Map<String, Object> getPageData()
    {
        Map<String, Object> map = new HashMap<>();
        map.put(Field.CURR_PAGE, get().getCurrPage());
        return map;
    }

    public static void cleanup()
    {
        threadHolder.remove();
    }
}
