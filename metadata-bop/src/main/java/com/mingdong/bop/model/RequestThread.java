package com.mingdong.bop.model;

import com.mingdong.bop.constant.ModulePath;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.constant.RoleType;

import java.util.ArrayList;
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

    public static final List<String> clientLinks = new ArrayList<>();
    public static final List<String> financeLinks = new ArrayList<>();
    public static final List<String> systemLinks = new ArrayList<>();

    static
    {
        clientLinks.add(ModulePath.CLIENT_INDEX.getModuleId());
        financeLinks.add(ModulePath.FINANCE_RECHARGE.getModuleId());
        financeLinks.add(ModulePath.FINANCE_CONSUMPTION.getModuleId());
        systemLinks.add(ModulePath.SETTING_USER.getModuleId());
        systemLinks.add(ModulePath.SETTING_ROLE.getModuleId());
        systemLinks.add(ModulePath.SETTING_PRODUCT.getModuleId());
        systemLinks.add(ModulePath.STRING_ARTICLES.getModuleId());
        systemLinks.add(ModulePath.SETTING_MENU.getModuleId());
        systemLinks.add(ModulePath.SETTING_RECHARGE.getModuleId());
        systemLinks.add(ModulePath.SETTING_INDUSTRY.getModuleId());
        systemLinks.add(ModulePath.SETTING_OTHER.getModuleId());
    }

    public static String clientLinkPath;
    public static String financeLinkPath;
    public static String systemLinkPath;

    public static Map<String, Object> getMap()
    {
        Map<String, Object> m = new HashMap<>();
        m.put("i_privilege", get().getiPrivilege());
        m.put("i_module", get().getiModule());
        m.put("i_system", get().getiSystem());
        m.put("i_enter", isManager());
        m.put("i_client_index", getClientLink());
        m.put("i_finance_index", getFinanceLink());
        m.put("i_system_index", getSystemLink());
        return m;
    }

    public static String getClientLink()
    {
        if(clientLinkPath == null)
        {
            List<String> tempList = new ArrayList<>(get().getiPrivilege());
            tempList.retainAll(clientLinks);
            if(!CollectionUtils.isEmpty(tempList))
            {
                clientLinkPath = ModulePath.getPathByModuleId(tempList.get(0));
            }else{
                clientLinkPath = "";
            }
        }
        return clientLinkPath;
    }

    public static String getFinanceLink()
    {
        if(financeLinkPath == null)
        {
            List<String> tempList = new ArrayList<>(get().getiPrivilege());
            tempList.retainAll(financeLinks);
            if(!CollectionUtils.isEmpty(tempList))
            {
                financeLinkPath = ModulePath.getPathByModuleId(tempList.get(0));
            }else{
                financeLinkPath = "";
            }
        }
        return financeLinkPath;
    }

    public static String getSystemLink()
    {
        if(systemLinkPath == null)
        {
            List<String> tempList = new ArrayList<>(get().getiPrivilege());
            tempList.retainAll(systemLinks);
            if(!CollectionUtils.isEmpty(tempList))
            {
                systemLinkPath = ModulePath.getPathByModuleId(tempList.get(0));
            }else{
                systemLinkPath = "";
            }
        }
        return systemLinkPath;
    }

    /**
     * 登录人员id
     */
    public static Long getOperatorId()
    {
        return get().getiId();
    }

    /**
     * 是否管理员
     */
    public static boolean isManager()
    {
        return RoleType.ADMIN.getId().equals(get().getRoleType());
    }

    /**
     * 是否业务员
     */
    public static boolean isSalesman()
    {
        return RoleType.SALESMAN.getId().equals(get().getRoleType());
    }

    /**
     * 是否运营人员
     */
    public static boolean isOperation()
    {
        return RoleType.OPERATION.getId().equals(get().getRoleType());
    }

    public static void cleanup()
    {
        threadHolder.remove();
    }
}
