package com.mingdong.mis.constant;

public enum MDResult
{
    // code: 0-9
    OK(0, "ok"),
    SYSTEM_BUSY(9, "系统繁忙，请稍后重试"),

    ACCESS_DENIED(1, "访问受限，请求凭证缺失"),
    ACCESS_RESTRICTED(13, "访问受限，请求凭证无此接口的访问权限"),
    INVALID_ACCESS_TOKEN(2, "请求凭证无效，请重新获取"),
    INVALID_CLIENT_IP(3, "请求IP未配置白名单"),

    CLIENT_ACCT_DISABLED(14, "客户主账号已被禁用"),
    CLIENT_SUB_ACCT_NOT_EXIST(5, "客户子账号不存在或已删除"),
    CLIENT_SUB_ACCT_DISABLED(8, "客户子账号已被禁用"),
    SECRET_KEY_NOT_SET(6, "客户账号未配置密钥"),
    CLIENT_IP_NOT_SET(13, "ip白名单未设置"),
    INVALID_ACCESS_KEY(7, "无效的接入密钥"),

    APP_ID_NOT_EXIST(901, "指定的AppID不存在"),
    INVALID_TIMESTAMP(902, "无效的时间戳"),
    INVALID_SIGN(903, "无效的签名"),
    PRODUCT_DISABLED(12, "服务已停用"),
    INSUFFICIENT_BALANCE(10, "账户余额不足"),
    PRODUCT_EXPIRED(11, "服务已过期"),
    // just comment
    ;

    private final int code;
    private final String message;

    MDResult(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
