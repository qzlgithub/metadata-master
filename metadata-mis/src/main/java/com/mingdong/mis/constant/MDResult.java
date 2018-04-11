package com.mingdong.mis.constant;

public enum MDResult
{
    OK(0, "ok"),

    ACCESS_DENIED(1, "访问受限，请求凭证缺失"),
    INVALID_ACCESS_TOKEN(2, "请求凭证无效，请重新获取"),
    ACCESS_RESTRICTED(3, "访问受限，请求凭证无此接口的访问权限"),

    INVALID_CLIENT_IP(100, "无效的请求地址"),
    INVALID_TIMESTAMP(101, "无效的时间戳"),
    INVALID_SIGN(102, "无效的签名"),
    INVALID_REQUEST_PARAM(103, "请求参数无效或缺失"),
    APP_ID_NOT_EXIST(104, "指定的AppID不存在"),
    SECRET_KEY_NOT_SET(105, "客户账号未配置密钥"),
    CLIENT_IP_NOT_SET(106, "ip白名单未设置"),

    CLIENT_ACCT_DISABLED(200, "客户主账号已被禁用"),
    CLIENT_SUB_ACCT_NOT_EXIST(201, "客户子账号不存在或已删除"),
    CLIENT_SUB_ACCT_DISABLED(202, "客户子账号已被禁用"),
    INVALID_ACCESS_KEY(203, "无效的接入密钥"),
    PRODUCT_DISABLED(204, "服务已停用"),
    INSUFFICIENT_BALANCE(205, "账户余额不足"),
    PRODUCT_EXPIRED(206, "服务已过期"),

    SYSTEM_BUSY(900, "系统繁忙，请稍后重试");

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
