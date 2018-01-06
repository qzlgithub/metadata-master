package com.mingdong.bop.constant;

public enum PathModule
{
    MANAGER_INDEX("/manager/index.html", "manager_index"),
    MANAGER_ADDITION("/manager/addition.html", "manager_index"),
    MANAGER_EDIT("/manager/edit.html", "manager_index"),
    ROLE_INDEX("/role/index.html", "role_index"),
    ROLE_ADDITION("/role/addition.html", "role_index"),
    ROLE_EDIT("/role/edit.html", "role_index"),
    PRIVILEGE_INDEX("/privilege/index.html", "privilege_index"),
    CONFIG_RECHARGE("/config/recharge.html", "config_recharge"),
    CONFIG_INDUSTRY("/config/industry.html", "config_industry"),
    PRODUCT_INDEX("/product/index.html", "product_index"),
    PRODUCT_CATEGORY_INDEX("/product/category/index.html", "product_category_index"),
    CLIENT_INDEX("/client/index.html", "client_index"),
    CLIENT_ADDITION("/client/addition.html", "client_index"),
    CLIENT_EDIT("/client/edit.html", "client_index"),
    CLIENT_DETAIL("/client/detail.html", "client_index"),
    CLIENT_PRODUCT_RECHARGE("/client/product/recharge.html", "client_index"),
    CLIENT_PRODUCT_CONSUME("/client/product/consume.html", "client_index"),
    CLIENT_ACCOUNT_RECHARGE("/client/account/recharge.html", "client_index"),
    CLIENT_ACCOUNT_CONSUME("/client/account/consume.html", "client_index"),
    STATS_INDEX("/stats/index.html", "stats_index"),
    STATS_CUSTOMER("/stats/customer.html", "stats_customer"),
    STATS_RECHARGE("/stats/recharge.html", "stats_recharge"),
    STATS_REVENUE("/stats/revenue.html", "stats_revenue"),
    STATS_REQUEST("/stats/request.html", "stats_request");

    private String path;
    private String module;

    PathModule(String path, String module)
    {
        this.path = path;
        this.module = module;
    }

    public static String getByPath(String path)
    {
        if(path != null)
        {
            for(PathModule pm : PathModule.values())
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
