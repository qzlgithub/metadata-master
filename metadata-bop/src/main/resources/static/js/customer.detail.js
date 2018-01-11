function openProduct() {
    var clientId = $("#client-id").val();
    var productId = $("#open-product-id").val();
    var chargeType = $("#open-charge").val();
    var start = $("#open-start").val();
    var end = $("#open-end").val();
    var unit = $("#open-unit").val();
    var amount = $("#open-amt").val();
    var rechargeType = $("#open-recharge-type").val();
    var contract = $("#open-contract").val();
    var remark = $("#open-remark").val();
    $.ajax({
        type: "post",
        url: "/client/product/open",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "clientId": clientId,
            "productId": productId,
            "billPlan": chargeType,
            "rechargeType": rechargeType,
            "amount": amount,
            "contractNo": contract,
            "remark": remark,
            "startDate": start,
            "endDate": end,
            "unitAmt": unit
        }),
        success: function(data) {
            if(data.errCode !== '000000') {
                layer.msg("开通失败:" + data.errMsg, {
                    time: 2000
                });
            }
            else {
                layer.msg("开通成功", {
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                }, function() {
                    window.location.href = "/client/detail.html?clientId=" + clientId;
                });
            }
        }
    });
}

function renewProduct() {
    var clientProductId = $("#renew-client-product-id").val();
    var billPlan = $("#renew-bill-plan").val();
    var startDate = $("#renew-start").val();
    var endDate = $("#renew-end").val();
    var unit = $("#renew-unit").val();
    var amount = $("#renew-amt").val();
    var rechargeType = $("#renew-recharge-type").val();
    var contract = $("#renew-contract").val();
    var remark = $("#renew-remark").val();
    $.ajax({
        type: "post",
        url: "/client/product/renew",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "clientProductId": clientProductId,
            "billPlan": billPlan,
            "rechargeType": rechargeType,
            "contractNo": contract,
            "amount": amount,
            "remark": remark,
            "startDate": startDate,
            "endDate": endDate,
            "unitAmt": unit
        }),
        success: function(data) {
            if(data.errCode !== '000000') {
                layer.msg("续费失败:" + data.errMsg, {
                    time: 2000
                });
            }
            else {
                layer.msg("续费成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/client/detail.html?clientId=" + $("#client-id").val();
                });
            }
        }
    });
}

function accountRecharge() {
    var clientId = $("#client-id").val();
    var amount = $("#account-recharge-amt").val();
    var rechargeType = $("#account-recharge-type").val();
    var remark = $("#account-recharge-remark").val();
    $.ajax({
        type: "post",
        url: "/client/account/recharge",//此处缺少账户充值方法
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "clientId": clientId,
            "amount": amount,
            "rechargeType": rechargeType,
            "remark": remark
        }),
        success: function(data) {
            if(data.errCode !== '000000') {
                layer.msg("充值失败:" + data.errMsg, {
                    time: 2000
                });
            }
            else {
                layer.msg("充值成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/client/detail.html?clientId=" + $("#client-id").val();
                });
            }
        }
    });
}

//开通服务切换
$(document).ready(function() {
    $("#open-charge").on("change", function() {
        if($("option:selected", this).val() == '1') {
            $("#year-div").show();
            $("#times-div").hide();
        }
        else if($("option:selected", this).val() == '2') {
            $("#times-div").show();
            $("#year-div").hide();
        }
        else if($("option:selected", this).val() == '3') {
            $("#times-div").show();
            $("#year-div").hide();
        }
        else {
            $("#year-div").hide();
            $("#times-div").hide();
        }
    });
    //续费切换
    $("#renew-bill-plan").on("change", function() {
        if($("option:selected", this).val() == '1') {
            $("#serviceTime").show();
            $("#priceIput").hide();
        }
        else if($("option:selected", this).val() == '2') {
            $("#priceIput").show();
            $("#serviceTime").hide();
        }
        else if($("option:selected", this).val() == '3') {
            $("#priceIput").show();
            $("#serviceTime").hide();
        }
        else {
            $("#serviceTime").hide();
            $("#priceIput").hide();
        }
    });
});
//续费js
/*function checkSelectBill() {
    var selectBill = $("#renew-bill-plan").val();
    if(selectBill !== "") {
        $("#selectBillTip").text("");
        $("#selectBillTip").hide();
    }
    else {
        $("#selectBillTip").text("请选择计费模式！");
        $("#selectBillTip").show();
    }
}*/
function checkStartTime() {
    var startTime = $("#renew-start").val();
    var reg = new RegExp(
        "^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))$");
    if(startTime !== "") {
        if(reg.test(startTime)) {
            $("#startTimeTip").text("");
            $("#startTimeTip").hide();
        }
        else {
            $("#startTimeTip").text("时间格式错误！");
            $("#startTimeTip").show();
        }
    }
    else {
        $("#startTimeTip").text("请选择服务开始时间！");
        $("#startTimeTip").show();
    }
}

function checkEndTime() {
    var startTime = $("#renew-start").val();
    var endTime = $("#renew-end").val();
    var reg =
        new
        RegExp("^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))$");
    if(endTime !== "") {
        if(reg.test(endTime)) {
            if(getDate(startTime) - getDate(endTime) > 0) {
                $("#endTimeTip").text("结束时间不能小于开始时间");
                $("#endTimeTip").show();
                return false;
            }
            else {
                $("#endTimeTip").text("");
                $("#endTimeTip").hide();
            }
        }
        else {
            $("#endTimeTip").text("时间格式错误");
            $("#endTimeTip").show();
        }
    }
    else {
        $("#endTimeTip").text("请选择服务结束时间！");
        $("#endTimeTip").show();
    }
}

function getDate(date) {
    var dates = date.split("-");
    var dateReturn = '';
    for(var i = 0; i < dates.length; i++) {
        dateReturn += dates[i];
    }
    return dateReturn;
}

function checkUnitPrice() {
    var unitPrice = $("#renew-unit").val();
    var reg = new RegExp("^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$"); // 匹配价格(保留小数点后两位)
    if(unitPrice !== "") {
        if(reg.test(unitPrice)) {
            $("#unitPriceTip").text("");
            $("#unitPriceTip").hide();
        }
        else {
            $("#unitPriceTip").text("单价格式错误！");
            $("#unitPriceTip").show();
        }
    }
    else {
        $("#unitPriceTip").text("请填写单价！");
        $("#unitPriceTip").show();
    }
}

function checkRecharge() {
    var recharge = $("#renew-amt").val();
    var reg = new RegExp("^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$"); // 充值金额价格(保留小数点后两位)
    if(recharge !== "") {
        if(reg.test(recharge)) {
            $("#rechargeTip").text("");
            $("#rechargeTip").hide();
        }
        else {
            $("#rechargeTip").text("充值金额格式错误");
            $("#rechargeTip").show();
        }
    }
    else {
        $("#rechargeTip").text("请填写充值金额！");
        $("#rechargeTip").show();
    }
}

/*function checkRechargeType() {
    var rechargeType = $("#renew-recharge-type").val();
    if(rechargeType !== "") {
        $("#rechargeTypeTip").text("");
        $("#rechargeTypeTip").hide();
    }
    else {
        $("#rechargeTypeTip").text("请选择充值类型！");
        $("#rechargeTypeTip").show();
    }
}*/
function checkContNumber() {
    var contNumber = $("#renew-contract").val();
    if(contNumber !== "") {
        $("#contNumberTip").text("");
        $("#contNumberTip").hide();
    }
    else {
        $("#contNumberTip").text("请填写合同编号！");
        $("#contNumberTip").show();
    }
}

//开通服务js
function checkOpenStart() {
    var startTime = $("#open-start").val();
    var reg = new RegExp(
        "^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))$");
    if(startTime !== "") {
        if(reg.test(startTime)) {
            $("#openStartTip").text("");
            $("#openStartTip").hide();
        }
        else {
            $("#openStartTip").text("时间格式错误！");
            $("#openStartTip").show();
        }
    }
    else {
        $("#openStartTip").text("请选择服务开始时间！");
        $("#openStartTip").show();
    }
}

function checkOpenEnd() {
    var startTime = $("#open-start").val();
    var endTime = $("#open-end").val();
    var reg =
        new
        RegExp("^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))$");
    if(endTime !== "") {
        if(reg.test(endTime)) {
            if(getDate(startTime) - getDate(endTime) > 0) {
                $("#openEndTip").text("结束时间不能小于开始时间");
                $("#openEndTip").show();
                return false;
            }
            else {
                $("#openEndTip").text("");
                $("#openEndTip").hide();
            }
        }
        else {
            $("#openEndTip").text("时间格式错误");
            $("#openEndTip").show();
        }
    }
    else {
        $("#openEndTip").text("请选择服务结束时间！");
        $("#openEndTip").show();
    }
}

function checkOpenUnit() {
    var unitPrice = $("#open-unit").val();
    var reg = new RegExp("^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$"); // 匹配价格(保留小数点后两位)
    if(unitPrice !== "") {
        if(reg.test(unitPrice)) {
            $("#openUnitTip").text("");
            $("#openUnitTip").hide();
        }
        else {
            $("#openUnitTip").text("单价格式错误！");
            $("#openUnitTip").show();
        }
    }
    else {
        $("#openUnitTip").text("请填写单价！");
        $("#openUnitTip").show();
    }
}

function checkOpenAmt() {
    var recharge = $("#open-amt").val();
    var reg = new RegExp("^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$"); // 充值金额价格(保留小数点后两位)
    if(recharge !== "") {
        if(reg.test(recharge)) {
            $("#openAmtTip").text("");
            $("#openAmtTip").hide();
        }
        else {
            $("#openAmtTip").text("充值金额格式错误");
            $("#openAmtTip").show();
        }
    }
    else {
        $("#openAmtTip").text("请填写充值金额！");
        $("#openAmtTip").show();
    }
}

function checkOpenContract() {
    var contNumber = $("#open-contract").val();
    if(contNumber !== "") {
        $("#openContractTip").text("");
        $("#openContractTip").hide();
    }
    else {
        $("#openContractTip").text("请填写合同编号！");
        $("#openContractTip").show();
    }
}

//账户充值js
function checkRechargeAmt() {
    var recharge = $("#account-recharge-amt").val();
    var reg = new RegExp("^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$"); // 充值金额价格(保留小数点后两位)
    if(recharge !== "") {
        if(reg.test(recharge)) {
            $("#rechargeAmtTip").text("");
            $("#rechargeAmtTip").hide();
        }
        else {
            $("#rechargeAmtTip").text("充值金额格式错误");
            $("#rechargeAmtTip").show();
        }
    }
    else {
        $("#rechargeAmtTip").text("请填写充值金额！");
        $("#rechargeAmtTip").show();
    }
}