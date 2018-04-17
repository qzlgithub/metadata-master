package com.mingdong.bop.constant;

public enum ModulePath
{
    //管理平台====================
    // 系统设置
    SETTING_MENU("/setting/menu.html", ModuleName.SETTING_MENU, "12100"),
    SETTING_RECHARGE("/setting/recharge.html", ModuleName.SETTING_RECHARGE, "12200"),
    SETTING_INDUSTRY("/setting/industry.html", ModuleName.SETTING_INDUSTRY, "12300"),
    SETTING_OTHER("/setting/other.html", ModuleName.SETTING_OTHER, "12400"),

    SETTING_ROLE("/setting/role.html", ModuleName.SETTING_ROLE, "11200"),
    SETTING_ROLE_ADD("/setting/role/add.html", ModuleName.SETTING_ROLE, ""),
    SETTING_ROLE_EDIT("/setting/role/edit.html", ModuleName.SETTING_ROLE, ""),
    SETTING_USER("/setting/user.html", ModuleName.SETTING_USER, "11100"),
    SETTING_USER_ADD("/setting/user/add.html", ModuleName.SETTING_USER, ""),
    SETTING_USER_EDIT("/setting/user/edit.html", ModuleName.SETTING_USER, ""),
    // 产品
    SETTING_PRODUCT("/setting/product.html", ModuleName.SETTING_PRODUCT, "21200"),
    SETTING_PRODUCT_EDIT("/setting/product/edit.html", ModuleName.SETTING_PRODUCT, ""),
    //新闻动态
    STRING_ARTICLES("/setting/articles.html", ModuleName.STRING_ARTICLES, "61100"),
    STRING_ARTICLES_ADD("/setting/articles/add.html", ModuleName.STRING_ARTICLES, ""),
    STRING_ARTICLES_EDIT("/setting/articles/edit.html", ModuleName.STRING_ARTICLES, ""),
    // 客户
    CLIENT_INDEX("/client/index.html", ModuleName.CLIENT_INDEX, "31100"),
    CLIENT_ADD("/client/add.html", ModuleName.CLIENT_INDEX, ""),
    CLIENT_EDIT("/client/edit.html", ModuleName.CLIENT_INDEX, ""),
    CLIENT_DETAIL("/client/detail.html", ModuleName.CLIENT_INDEX, ""),
    CLIENT_RECHARGE("/client/recharge.html", ModuleName.CLIENT_INDEX, ""),
    CLIENT_CONSUMPTION("/client/consumption.html", ModuleName.CLIENT_INDEX, ""),
    // 财务
    FINANCE_RECHARGE("/finance/recharge.html", ModuleName.FINANCE_RECHARGE, "51100"),
    FINANCE_CONSUMPTION("/finance/consumption.html", ModuleName.FINANCE_CONSUMPTION, "51200"),
    //============================

    //监控平台=======================
    //数据监控
    MONITORING_PRODUCT("/monitoring/product.html", ModuleName.MONITORING_PRODUCT, ""),
    MONITORING_CUSTOMER("/monitoring/client.html", ModuleName.MONITORING_CUSTOMER, ""),
    MONITORING_OTHER("/monitoring/third.html", ModuleName.MONITORING_THIRD, ""),
    //警报系统

    //运营统计
    STATS_CLIENT("/stats/client.html", ModuleName.STATS_CLIENT, ""),
    STATS_RECHARGE("/stats/recharge.html", ModuleName.STATS_RECHARGE, ""),
    STATS_REQUEST_CLIENT("/stats/request/client.html", ModuleName.STATS_REQUEST_CLIENT, ""),
    STATS_REQUEST_PRODUCT("/stats/request/product.html", ModuleName.STATS_REQUEST_PRODUCT, ""),
    STATS_REQUEST_THIRD("/stats/request/third.html", ModuleName.STATS_REQUEST_THIRD, ""),
    STATS_REVENUE("/stats/revenue.html", ModuleName.STATS_REVENUE, ""),
    //警报设置
    WARNING_SETTING_LIST("/warning/setting/list.html", ModuleName.WARNING_SETTING, ""),
    WARNING_SETTING_EDIT("/warning/setting/edit.html", ModuleName.WARNING_SETTING, "");
    //===============================

    private final String path;
    private final String module;
    private final String moduleId;

    ModulePath(String path, String module, String moduleId)
    {
        this.path = path;
        this.module = module;
        this.moduleId = moduleId;
    }

    public static String getByPath(String path)
    {
        if(path != null)
        {
            for(ModulePath pm : ModulePath.values())
            {
                if(path.equals(pm.path))
                {
                    return pm.module;
                }
            }
        }
        return null;
    }

    public static String getPathByModuleId(String moduleId)
    {
        if(moduleId != null)
        {
            for(ModulePath pm : ModulePath.values())
            {
                if(moduleId.equals(pm.moduleId))
                {
                    return pm.path;
                }
            }
        }
        return null;
    }

    public String getPath()
    {
        return path;
    }

    public String getModule()
    {
        return module;
    }

    public String getModuleId()
    {
        return moduleId;
    }
}
