package com.mingdong.mis.constant;

public enum MessageType
{
    NOTICE(1, ""),
    NEW_OPEN(2, "恭喜您成功开通%s产品服务，开通金额：%s元，感谢您对天镜的支持！"),
    RENEW(3, "您已成功对%s产品服务续费，续费金额：%s元！");

    private final int id;
    private final String content;

    MessageType(int id, String content)
    {
        this.id = id;
        this.content = content;
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
