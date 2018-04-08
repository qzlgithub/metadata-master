package com.mingdong.mis.constant;

public enum MDResult
{
    // code: 0-9
    OK(0, "ok"),
    SYSTEM_BUSY(9, "系统繁忙，请稍后重试"),

    ACCESS_DENIED(101, "访问受限，请求凭证缺失"),
    INVALID_CLIENT_IP(102, "无效的请求地址"),
    INVALID_TIMESTAMP(103, "无效的时间戳"),
    INVALID_SIGN(104, "无效的签名"),
    APP_ID_NOT_EXIST(105, "指定的AppID不存在"),
    ACCESS_RESTRICTED(106, "访问受限，请求凭证无此接口的访问权限"),
    INVALID_ACCESS_TOKEN(107, "请求凭证无效，请重新获取"),
    INVALID_REQUEST_PARAM(108, "请求参数无效或字段缺失"),

    CLIENT_ACCT_DISABLED(201, "客户主账号已被禁用"),
    CLIENT_SUB_ACCT_NOT_EXIST(202, "客户子账号不存在或已删除"),
    CLIENT_SUB_ACCT_DISABLED(203, "客户子账号已被禁用"),
    SECRET_KEY_NOT_SET(204, "客户账号未配置密钥"),
    CLIENT_IP_NOT_SET(205, "ip白名单未设置"),
    INVALID_ACCESS_KEY(206, "无效的接入密钥"),
    PRODUCT_DISABLED(207, "服务已停用"),
    INSUFFICIENT_BALANCE(208, "账户余额不足"),
    PRODUCT_EXPIRED(209, "服务已过期");

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
