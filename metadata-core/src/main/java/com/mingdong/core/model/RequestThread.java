package com.mingdong.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestThread
{
    private static final ThreadLocal<RequestHolder> threadHolder = new ThreadLocal<>();

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
}
