var reg;

//验证不为空 notnull
function isNotNull(obj) {
    obj = $.trim(obj);
    if(obj.length == 0 || obj == null || obj == undefined) {
        return false;
    }
    else {
        return true;
    }
}

//验证用户名
function isUsername(obj) {
    reg = /^[a-zA-Z0-9_-]{4,16}$/;
    if(!reg.test(obj)) {
        return false;
    }
    else {
        return true;
    }
}

//验证密码-密码规则：6-20位字母数字
function isPassword(obj) {
    if(obj == null || (obj.length < 6 || obj.length > 20)) {
        return false;
    }
    var reg1 = new RegExp(/^[0-9A-Za-z]+$/);
    if (!reg1.test(obj)) {
        return false;
    }
    var reg = new RegExp(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/);
    if (reg.test(obj)) {
        return true;
    } else {
        return false;
    }
}

//匹配社会信用代码
function isSocialCreditCode(obj) {
    reg = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/;
    if(!reg.test(obj)) {
        return false;
    }
    else {
        return true;
    }
}

//匹配double精度为2的正数
function isDouble2(obj) {
    reg = /^[0-9]+(.[0-9]{1,2})?$/;
    if(!reg.test(obj) && obj != '0') {
        return false;
    }
    else {
        return true;
    }
}

//Email验证 email
function isEmail(obj) {
    reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    if(!reg.test(obj)) {
        return false;
    }
    else {
        return true;
    }
}

//验证是否电话号码 phone
function isTelephone(obj) {
    reg = /^(\d{3,4}\-)?[1-9]\d{6,7}$/;
    if(!reg.test(obj)) {
        return false;
    }
    else {
        return true;
    }
}

//验证是否手机号 mobile
function isMobile(obj) {
    reg = /^(\+\d{2,3}\-)?\d{11}$/;
    if(!reg.test(obj)) {
        return false;
    }
    else {
        return true;
    }
}

//验证是否手机号或电话号码 mobile phone
function isMobileOrPhone(obj) {
    var reg_mobile = /^(\+\d{2,3}\-)?\d{11}$/;
    var reg_phone = /^(\d{3,4}\-)?[1-9]\d{6,7}$/;
    if(!reg_mobile.test(obj) && !reg_phone.test(obj)) {
        return false;
    }
    else {
        return true;
    }
}

/**
 数据验证完整性
 **/
function checkDataValid(id) {
    if(!judgeValidate(id)) {
        return false;
    }
    else {
        return true;
    }
}

/**
 * 自定义标签属性：
 * notnull
 * datatype
 * datames
 *
 */
function judgeValidate(obj) {
    var validateMsg = "";
    var validateFlag = true;
    var tgs = ["INPUT:not(:file)", "SELECT", "TEXTAREA"];
    var len = tgs.length;
    var errorIndex = 0;
    for(var j = 0; j < len; j++) {
        $(obj).find(tgs[j]).each(function() {
            var value = $.trim($(this).val());
            $(this).val(value);
            var id = $(this).attr("id");
            if($("#span-id-" + id).length > 0) {
                $("#span-id-" + id).remove();
            }
            if(typeof($(this).attr("notnull")) != "undefined" && $(this).attr("notnull") == 'true') {
                if(!isNotNull(value)) {
                    validateMsg = (typeof($(this).attr("datames")) != "undefined" ? $(this).attr("datames") : "") + "不能为空！\n";
                    validateFlag = false;
                    tipCss($(this), validateMsg, errorIndex++);
                    return true;
                }
            }
            if(!isNotNull(value)) {
                return true;
            }
            if(typeof($(this).attr("datatype")) == "undefined") {
                return true;
            }
            var datatype = $(this).attr("datatype");
            switch(datatype) {
                case "username":
                    if(!isUsername(value)) {
                        validateMsg = $(this).attr("datames") + "格式不匹配！\n";
                        validateFlag = false;
                        tipCss($(this), validateMsg, errorIndex++);
                    }
                    return true;
                case "double2":
                    if(!isDouble2(value)) {
                        validateMsg = $(this).attr("datames") + "格式不匹配，最多两位小数的正数！\n";
                        validateFlag = false;
                        tipCss($(this), validateMsg, errorIndex++);
                    }
                    return true;
                case "phone":
                    if(!isTelephone(value)) {
                        validateMsg = $(this).attr("datames") + "格式不匹配！\n";
                        validateFlag = false;
                        tipCss($(this), validateMsg, errorIndex++);
                    }
                    return true;
                case "mobile":
                    if(!isMobile(value)) {
                        validateMsg = $(this).attr("datames") + "格式不匹配！\n";
                        validateFlag = false;
                        tipCss($(this), validateMsg, errorIndex++);
                    }
                    return true;
                case "phoneOrMobile":
                    if(!isMobileOrPhone(value)) {
                        validateMsg = $(this).attr("datames") + "格式不匹配！\n";
                        validateFlag = false;
                        tipCss($(this), validateMsg, errorIndex++);
                    }
                    return true;
                case "email":
                    if(!isEmail(value)) {
                        validateMsg = $(this).attr("datames") + "格式不匹配！\n";
                        validateFlag = false;
                        tipCss($(this), validateMsg, errorIndex++);
                    }
                    return true;
                case "socialCreditCode":
                    if(!isSocialCreditCode(value)) {
                        validateMsg = $(this).attr("datames") + "格式不匹配！\n";
                        validateFlag = false;
                        tipCss($(this), validateMsg, errorIndex++);
                    }
                    return true;
                case "password":
                    if(!isPassword(value)) {
                        validateMsg = $(this).attr("datames") + "格式不匹配，必须6-20位字母数字组成！\n";
                        validateFlag = false;
                        tipCss($(this), validateMsg, errorIndex++);
                    }
                default:
                    return true;
            }
        });
    }
    return validateFlag;
}

function tipCss(obj, validateMsg, errorIndex) {
    if(errorIndex == 0) {
        $(obj).focus();
    }
    var id = $(obj).attr("id");
    $(obj).after('<span id="span-id-' + id + '" style="color:red">' + validateMsg + '</span>');
}