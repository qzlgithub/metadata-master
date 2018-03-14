$(".date").datepicker({dateFormat: "yy-mm-dd"});
var message;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message', 'laydate', 'table'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    laydate = layui.laydate;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    laydate.render({
        elem: '#open-dates'
        ,range: true,
        done: function(value, date){
            if(value != ""){
                var dates = value.split(" - ");
                $("#open-start").val(dates[0]);
                $("#open-end").val(dates[1]);
            }else{
                $("#open-start").val("");
                $("#open-end").val("");
            }
        }
    });
    laydate.render({
        elem: '#renew-dates'
        ,range: true,
        done: function(value, date){
            if(value != ""){
                var dates = value.split(" - ");
                $("#renew-start").val(dates[0]);
                $("#renew-end").val(dates[1]);
            }else{
                $("#renew-start").val("");
                $("#renew-end").val("");
            }
        }
    });
    $('#pay').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            /*content: '<img src="../../static/build/images/pay.png" />',*/
            content: $('#key-modal'),
            /*area: ['500px', '250px'],*/
            area: ['700px'],
            shadeClose: true
        });
    });
    $('.app-id').on('click', function() {
        var appId = $(this).attr("app-id");
        var appName = $(this).attr("app-name");
        $("#app-id-prod").text(appName);
        $("#app-id-val").text(appId);
        layer.open({
            title: false,
            type: 1,
            content: $('#key-modal'),
            area: ['700px'],
            shadeClose: true
        });
    });
    $('.renew').on('click', function() {
        var productId = $(this).data("product");
        $("#renewProduct").val(productId);
        if($(this).hasClass('is-selected-class')) {
            $("#renew-info").empty();
            $("#renew-server-id").html("开通服务");
            layer.open({
                title: false,
                type: 1,
                content: $('#renew-modal'),
                area: ['700px'],
                shadeClose: true
            });
            return;
        }
        $("#renew-server-id").html("续费服务");
        var clientId = $("#client-id").val();
        $.get(
            "/client/product/renewInfo", // TODO ===========
            {"clientId": clientId, "productId": productId},
            function(res) {
                $("#renew-info").empty();
                var row1 =
                    '<li>服务时间：#{startDate} - #{endDate}</li><li>客户价：#{amount}元</li><li>总充值：#{totalAmt}元</li>';
                var row2 =
                    '<li>当前余额：#{balance}元</li><li>客价：#{unitAmt}元/次</li><li>总充值：#{totalAmt}元</li>';
                var row;
                if(res.billPlan === 1) {
                    row = row1.replace("#{startDate}", res.startDate).replace("#{endDate}", res.endDate)
                    .replace("#{amount}", res.amount).replace("#{totalAmt}", res.totalAmt);
                }
                else {
                    row = row2.replace("#{balance}", res.balance).replace("#{unitAmt}", res.unitAmt)
                    .replace("#{totalAmt}", res.totalAmt);
                }
                $("#renew-info").append(row);
                layer.open({
                    title: false,
                    type: 1,
                    content: $('#renew-modal'),
                    area: ['700px'],
                    shadeClose: true
                });
            }
        );
    });
    $('.open-product').on('click', function() {
        var productId = $(this).attr("data-product-id");
        $("#open-product-id").val(productId);
        layer.open({
            title: false,
            type: 1,
            content: $('#openServe-modal'),
            area: ['700px'],
            shadeClose: true
        });
    });
    $('#service').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#service-modal'),
            area: ['700px'],
            shadeClose: true
        });
    });
});
var logTr = '<ul class="log"><li><div class="title-log pt10 pb10 clearfix">' +
    '<span class="w50 tl">#{type}</span>' +
    '<span class="w25 tr">#{managerName}</span>' +
    '<span class="w25 tr">#{operateTime}</span></div>' +
    '<div class="lh25 tl col2">#{reason}</div></li></ul>';
$(".show-log").click(function() {
    var obj = {
        clientId: $("#client-id").val(),
        pageNum: 1,
        pageSize: 5
    };
    getOperateLogList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getOperateLogList(pageObj);
            }
        })
    }, function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#ban-div'),
            area: ['700px'],
            shadeClose: true
        });
    });
});

function getOperateLogList(obj, pageFun, openLayerFun) {
    $.get(
        "/client/operate/log",
        {"id": obj['clientId'], "pageNum": obj['pageNum'], "pageSize": obj['pageSize']},
        function(res) {
            var div = $("#ban-data-body");
            div.empty();
            var total = res.total;
            var pages = Math.floor((total - 1) / parseInt(obj['pageSize'])) + 1;
            var list = res.list;
            if(typeof(list) !== "undefined" && list.length > 0) {
                for(var o in list) {
                    var tr = logTr.replace(/#{type}/g, list[o].type === 1 ? "解冻操作" : "冻结操作")
                    .replace(/#{managerName}/g, list[o].managerName)
                    .replace(/#{operateTime}/g, list[o].operateTime)
                    .replace(/#{reason}/g, list[o].reason);
                    div.append(tr);
                }
                if(typeof pageFun === 'function') {
                    pageFun(obj, pages, total);
                }
            }
            else {
                div.append("<h3>暂无数据</h3>");
            }
            if(typeof openLayerFun === 'function') {
                openLayerFun();
            }
        }
    );
}

function openProduct() {
    var chargeType = $("#open-charge").val();
    if(chargeType == 1){
        if($("#open-dates").val() === ""){
            $("#openTimeTip").text("服务时间不能为空！").show();
            return;
        }
    }else{
        if(!checkUnitPrice('open-unit','openUnitTip')){
            return;
        }
    }
    if(!checkRecharge('open-amt','openAmtTip')){
        return;
    }
    if(!checkOpenContract()){
        return;
    }
    var clientId = $("#client-id").val();
    var productId = $("#open-product-id").val();
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
        success: function(res) {
            if(res.code !== '000000') {
                layer.msg("开通失败:" + res.message, {
                    time: 2000
                });
            }
            else {
                layer.msg("开通成功", {
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                }, function() {
                    window.location.href = "/client/detail.html?id=" + clientId;
                });
            }
        }
    });
}

function renewProduct() {
    var billPlan = $("#renew-bill-plan").val();
    if(billPlan == 1){
        if($("#renew-dates").val() === ""){
            $("#renewTimeTip").text("服务时间不能为空！").show();
            return;
        }
    }else{
        if(!checkUnitPrice('renew-unit','unitPriceTip')){
            return;
        }
    }
    if(!checkRecharge('renew-amt','rechargeTip')){
        return;
    }
    if(!checkContNumber()){
        return;
    }
    var clientId = $("#client-id").val();
    var productId = $("#renewProduct").val();
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
            "clientId": clientId,
            "productId": productId,
            "billPlan": billPlan,
            "rechargeType": rechargeType,
            "contractNo": contract,
            "amount": amount,
            "remark": remark,
            "startDate": startDate,
            "endDate": endDate,
            "unitAmt": unit
        }),
        success: function(res) {
            if(res.code !== '000000') {
                layer.msg("续费失败:" + res.message, {
                    time: 2000
                });
            }
            else {
                layer.msg("续费成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/client/detail.html?id=" + $("#client-id").val();
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

function getDate(date) {
    var dates = date.split("-");
    var dateReturn = '';
    for(var i = 0; i < dates.length; i++) {
        dateReturn += dates[i];
    }
    return dateReturn;
}

function checkUnitPrice(unitId,unitPriceTipId) {
    var unitPrice = $("#"+unitId).val();
    var reg = new RegExp("^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$"); // 匹配价格(保留小数点后两位)
    $("#"+unitPriceTipId).text("").hide();
    if(unitPrice !== "") {
        if(!reg.test(unitPrice)) {
            $("#"+unitPriceTipId).text("单价格式错误！").show();
            return false;
        }
    }
    else {
        $("#"+unitPriceTipId).text("请填写单价！").show();
        return false;
    }
    return true;
}

function checkRecharge(amtId,amtTipId) {
    var recharge = $("#"+amtId).val();
    var reg = new RegExp("^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$"); // 充值金额价格(保留小数点后两位)
    $("#"+amtTipId).text("").hide();
    if(recharge !== "") {
        if(!reg.test(recharge)) {
            $("#"+amtTipId).text("充值金额格式错误").show();
            return false;
        }
    }
    else {
        $("#"+amtTipId).text("请填写充值金额！").show();
        return false;
    }
    return true;
}

function checkContNumber() {
    var contNumber = $("#renew-contract").val();
    $("#contNumberTip").text("").hide();
    if(contNumber === "") {
        $("#contNumberTip").text("请填写合同编号！").show();
        return false;
    }
    return true;
}

function checkOpenContract() {
    var conNumber = $("#open-contract").val();
    $("#openContractTip").text("").hide();
    if(conNumber !== "") {
        $.get(
            "/client/checkContract",
            {"contractNo": conNumber},
            function(data) {
                if(data.exist === 1) {
                    $("#openContractTip").text("该合同编号已存在！").show();
                }
            }
        );
    }
    else {
        $("#openContractTip").text("请填写合同编号！").show();
        return false;
    }
    return true;
}

$("#all-product").on("change", ".product-checkbox-class", function() {
    var productId = $(this).attr("value");
    var productName = $(this).parents("tr").find("#productName-" + productId).html();
    if($(this).is(':checked')) {
        var html = '<li id="ul-li-' + productId + '"><span >' + productName + '</span><i class="layui-icon fz-14 ul-li-class" value="' + productId + '">&#x1006;</i></li>';
        $("#selected-product").append(html);
    }
    else {
        $("#ul-li-" + productId).remove();
    }
});
$("#selected-product").on("click", ".ul-li-class", function() {
    var productId = $(this).attr("value");
    $("#checkbox-" + productId).prop("checked", false);
    $("#ul-li-" + productId).remove();
});
$(".remove-class").on("click", function() {
    var clientProductId = $(this).attr("value");
    layer.confirm('是否确定移除？（已被开通的项目无法移除）', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();
            $.ajax({
                type: "POST",
                url: "/client/product/remove",
                data: {"id": clientProductId},
                success: function(res) {
                    if(res.code !== '000000') {
                        layer.msg("移除失败:" + res.message, {
                            time: 2000
                        });
                    }
                    else {
                        layer.msg("移除成功", {
                            time: 1000
                        }, function() {
                            window.location.reload();
                        });
                    }
                }
            });
            layer.closeAll();
        },
        btn2: function() {
            layer.closeAll();
        }
    });
});

function findProductTr() {
    var selectProductId = $("#select-product").val();
    $("#all-product").find("tr").each(function() {
        if(selectProductId === "" || selectProductId === $(this).attr("value")) {
            $(this).show();
        }
        else {
            $(this).hide();
        }
    });
}

var isSubmit = false;

function selectProductSave() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    var ids = [];
    $("input[class=product-checkbox-class]:checked").each(function() {
        ids.push($(this).val());
    });
    var clientId = $("#client-id").val();
    $.ajax({
        type: "post",
        url: "/client/product/select",
        data: {clientId: clientId, ids: ids.join(",")},
        success: function(res) {
            if(res.code !== '000000') {
                layer.msg("添加失败:" + res.message, {
                    time: 2000
                });
                isSubmit = false;
            }
            else {
                layer.msg("添加成功", {
                    time: 2000
                }, function() {
                    window.location.reload();
                });
            }
        }
    });
}