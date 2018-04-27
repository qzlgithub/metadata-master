package com.mingdong.csp.constant;

public enum PathPage
{
    HOME_PAGE("/home.html", "home"),
    MINE_PROD("/product/mine.html", "my-product"),
    MINE_PROD_DETAIL("/product/detail.html", "my-product"),
    ALL_PROD("/product/all.html", "all-product"),
    SETTING_MESSAGE("/system/message.html", "system"),
    SETTING_ACCOUNT("/system/account-list.html", "system"),
    SECURITY_SETTING("/system/security-setting.html", "system"),
    EDIT_PWD("/system/edit-pwd.html", "system"),
    EDIT_VALIDATION("/system/edit-validation.html", "system"),
    EDIT_APP_KEY("/system/edit-appKey.html", "system");

    private String path;
    private String page;

    PathPage(String path, String page)
    {
        this.path = path;
        this.page = page;
    }

    public static String getPageByPath(String path)
    {
        if(path != null)
        {
            for(PathPage pm : PathPage.values())
            {
                if(path.equals(pm.path))
                {
                    return pm.page;
                }
            }
        }
        return null;
    }

    public String getPath()
    {
        return path;
    }

    public String getPage()
    {
        return page;
    }
}
