package com.mingdong.bop.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestThread
{
    private static final ThreadLocal<RequestHolder> threadHolder = new ThreadLocal<>();
    private static final ThreadLocal<Long> threadHolderLong = new ThreadLocal<>();

    public static Long getTimeLong()
    {
        return threadHolderLong.get();
    }

    public static void setTimeLong(Long time)
    {
        threadHolderLong.set(time);
    }

    public static void removeLong()
    {
        threadHolderLong.remove();
    }

    public static void set(Long managerId, String managerName, List<String> privilege)
    {
        RequestHolder holder = new RequestHolder();
        holder.setiId(managerId);
        holder.setiName(managerName);
        holder.setiPrivilege(privilege);
        threadHolder.set(holder);
    }

    private static RequestHolder get()
    {
        return threadHolder.get();
    }

    public static void setModule(String iModule)
    {
        get().setiModule(iModule);
    }

    public static void setSystem(Map<String, String> iSystem)
    {
        get().setiSystem(iSystem);
    }

    public static Map<String, Object> getMap()
    {
        Map<String, Object> m = new HashMap<>();
        m.put("i_id", get().getiId());
        m.put("i_name", get().getiName());
        m.put("i_privilege", get().getiPrivilege());
        m.put("i_module", get().getiModule());
        m.put("i_system", get().getiSystem());
        return m;
    }

    public static Long getOperatorId()
    {
        return get().getiId();
    }

    public static void cleanup()
    {
        threadHolder.remove();
    }
}
