package com.mingdong.mis.constant;

public enum MetadataResult
{
    RC_0(0, "请求成功"),
    RC_1(1, "请求未授权"),
    RC_2(2, "无效的请求凭证，请重新获取"),
    RC_3(3, "请求IP地址未注册"),
    RC_4(4, "无效的AppID"),
    RC_5(5, "该用户名对应的子账号不存在"),
    RC_6(6, "请求账号未配置接入凭证"),
    RC_7(7, "无效的请求密钥"),
    RC_8(8, "该账号已被禁用"),
    RC_9(9, "验签失败"),
    RC_10(10, "账户余额不足"),
    RC_11(11, "系统繁忙，请稍后重试"),
    RC_12(12, "该产品服务已被禁用"),
    RC_13(13, "请求凭证无此接口的访问权限"),
    RC_14(14, "企业账号已被禁用");

    private final int code;
    private final String message;

    MetadataResult(int code, String message)
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
