package com.mingdong.csp.model;

import com.mingdong.csp.constant.Field;

import java.util.HashMap;
import java.util.Map;

public class RequestThread
{
    private static final ThreadLocal<RequestHolder> threadHolder = new ThreadLocal<>();

    public static void set(Long clientId, Long userId, String username, Integer primary)
    {
        RequestHolder holder = new RequestHolder();
        holder.setClientId(clientId);
        holder.setUserId(userId);
        holder.setUsername(username);
        holder.setPrimary(primary);
        threadHolder.set(holder);
    }

    public static Boolean getIsLogin()
    {
        return threadHolder.get() != null;
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

    public static Integer getPrimary()
    {
        return get().getPrimary();
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

    public static String getUsername()
    {
        return get().getUsername();
    }

}
