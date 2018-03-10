package com.mingdong.core.constant;

public enum MessageType
{
    NOTICE(1, "通知", ""),
    NEW_OPEN(2, "产品开通", "恭喜您成功开通%s产品服务，开通金额：%s元！"),
    RENEW(3, "产品续费", "您已成功对%s产品服务续费，续费金额：%s元！");

    private final int id;
    private final String name;
    private final String content;

    MessageType(int id, String name, String content)
    {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public static String getNameById(int id)
    {
        for(MessageType item : MessageType.values())
        {
            if(id == item.id)
            {
                return item.name;
            }
        }
        return null;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    public String getContent()
    {
        return content;
    }
}
