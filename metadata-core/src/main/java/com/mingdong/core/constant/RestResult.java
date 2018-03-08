package com.mingdong.core.constant;

public enum RestResult
{
    SUCCESS("000000", "ok"),
    ACCOUNT_NOT_EXIST("100001", "账号不存在"),
    INVALID_PASSCODE("100002", "账号密码不匹配"),
    ACCOUNT_DISABLED("100003", "账户已禁用"),
    CATEGORY_CODE_EXIST("100004", "类型编码已存在"),
    OBJECT_NOT_FOUND("100005", "找不到对象"),
    DUPLICATE_PRODUCT_CODE("100006", "重复的产品编码"),
    INVALID_PRODUCT_TYPE("100007", "无效的产品类型"),
    CATEGORY_NAME_EXIST("100008", "类型名称已存在"),
    PARENT_INDUSTRY_NOT_EXIST("100009", "上级行业不存在"),
    ACCOUNT_IS_EXIST("100010", "账号已存在"),
    USERNAME_EXIST("100011", "用户名已存在"),
    INVALID_CAPTCHA("100012", "无效的验证码"),
    INVALID_PRODUCT_NAME("100013", "无效的产品类型名称"),
    KEY_FIELD_MISSING("100014", "关键字段不能为空"),
    ROLE_NAME_EXIST("100015", "角色名已存在"),
    INDUSTRY_CODE_EXIST("100016", "行业编号已被占用"),
    PRODUCT_OPENED("100017", "该产品服务已开通"),
    ONLY_PRIMARY_USER("100018", "非主账号无此操作权限"),
    CONTRACT_IS_EXIST("100019", "合同编号已存在"),
    PRODUCT_NAME_EXIST("100020", "产品名称已存在"),
    PRODUCT_NOT_OPEN("100021", "客户未开通本产品"),
    ACCOUNT_COUNT_MAX("100022", "账号数量已到上限"),
    PARAMETER_ERROR("100023", "参数错误"),
    INSUFFICIENT_BALANCE("100024", "账户余额不足或已过期"),
    CLIENT_NAME_EXIST("100025", "公司名称已存在"),
    CLIENT_LICENSE_EXIST("100026", "社会信用代码已存在"),
    CLIENT_PRODUCT_NO_DELETE("100027", "服务已被开通无法移除"),
    STATS_EXIST("100028", "统计时间已经存在"),

    SYSTEM_ERROR("999997", "发生错误，请联系管理员"),
    ACCESS_LIMITED("999998", "访问受限（缺少访问凭证）"),
    INTERNAL_ERROR("999999", "系统繁忙，请稍后重试"),
    PRODUCT_NOT_EXIST("100028", "指定产品不存在"),
    SPECIFIED_PRODUCT("100029", "定制产品无权开通"),
    SUB_USER_NOT_EXIST("100030", "子账号不存在");

    private final String code;
    private final String message;

    RestResult(String code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public static RestResult getByCode(String code)
    {
        if(code != null)
        {
            for(RestResult o : RestResult.values())
            {
                if(code.equals(o.code))
                {
                    return o;
                }
            }
        }
        return INTERNAL_ERROR;
    }

    public String getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
