package com.mingdong.csp.constant;

public enum PathPage
{
    HOME_PAGE("/home.html", "home"),
    MANAGER_ADDITION("/manager/addition.html", "my-product"),
    MANAGER_EDIT("/manager/edit.html", "all-product"),
    ROLE_INDEX("/role/index.html", "system");

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
