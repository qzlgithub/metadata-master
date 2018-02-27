package com.mingdong.bop.constant;

public enum ModulePath
{
    // 系统设置
    SETTING_MENU("/setting/menu.html", ModuleName.SETTING_MENU),
    SETTING_RECHARGE("/setting/recharge.html", ModuleName.SETTING_RECHARGE),
    SETTING_INDUSTRY("/setting/industry.html", ModuleName.SETTING_INDUSTRY),
    SETTING_OTHER("/setting/other.html", ModuleName.SETTING_OTHER),

    SETTING_ROLE("/setting/role.html", ModuleName.SETTING_ROLE),
    SETTING_ROLE_ADD("/setting/role/add.html", ModuleName.SETTING_ROLE),
    SETTING_ROLE_EDIT("/setting/role/edit.html", ModuleName.SETTING_ROLE),
    SETTING_USER("/setting/user.html", ModuleName.SETTING_USER),
    SETTING_USER_ADD("/setting/user/add.html", ModuleName.SETTING_USER),
    SETTING_USER_EDIT("/setting/user/edit.html", ModuleName.SETTING_USER),
    // 运营
    STATS_INDEX("/stats/index.html", ModuleName.STATS_INDEX),
    STATS_CLIENT("/stats/client.html", ModuleName.STATS_CLIENT),
    STATS_RECHARGE("/stats/recharge.html", ModuleName.STATS_RECHARGE),
    STATS_REVENUE("/stats/revenue.html", ModuleName.STATS_REVENUE),
    STATS_REQUEST("/stats/request.html", ModuleName.STATS_REQUEST),
    // 产品
    SETTING_PRODUCT("/setting/product.html", ModuleName.SETTING_PRODUCT),
    SETTING_PRODUCT_EDIT("/setting/product/edit.html", ModuleName.SETTING_PRODUCT),
    SETTING_PRODUCT_CATEGORY("/setting/product/category.html", ModuleName.SETTING_PRODUCT),
    // 客户
    CLIENT_INDEX("/client/index.html", ModuleName.CLIENT_INDEX),
    CLIENT_ADD("/client/add.html", ModuleName.CLIENT_INDEX),
    CLIENT_EDIT("/client/edit.html", ModuleName.CLIENT_INDEX),
    CLIENT_DETAIL("/client/detail.html", ModuleName.CLIENT_INDEX),
    CLIENT_RECHARGE("/client/recharge.html", ModuleName.CLIENT_INDEX),
    CLIENT_REQUEST("/client/request.html", ModuleName.CLIENT_INDEX),
    // 财务
    FINANCE_REQUEST("/finance/request.html", ModuleName.FINANCE_REQUEST),
    FINANCE_RECHARGE("/finance/recharge.html", ModuleName.FINANCE_RECHARGE);

    private String path;
    private String module;

    ModulePath(String path, String module)
    {
        this.path = path;
        this.module = module;
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

    public String getPath()
    {
        return path;
    }

    public String getModule()
    {
        return module;
    }
}
