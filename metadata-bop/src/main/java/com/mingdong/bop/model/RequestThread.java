package com.mingdong.bop.model;

import com.mingdong.core.constant.RoleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestThread
{
    private static final ThreadLocal<RequestHolder> threadHolder = new ThreadLocal<>();

    public static void set(Long managerId, String managerName, Integer roleType, List<String> privilege)
    {
        RequestHolder holder = new RequestHolder();
        holder.setiId(managerId);
        holder.setiName(managerName);
        holder.setiPrivilege(privilege);
        holder.setRoleType(roleType);
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
        m.put("i_privilege", get().getiPrivilege());
        m.put("i_module", get().getiModule());
        m.put("i_system", get().getiSystem());
        return m;
    }

    public static Long getOperatorId()
    {
        return get().getiId();
    }

    public static boolean isManager()
    {
        return RoleType.ADMIN.getId().equals(get().getRoleType());
    }

    public static void cleanup()
    {
        threadHolder.remove();
    }
}
