var message;
layui.config({
    base: '../build/js/'
}).use(['app', 'message', 'laydate'], function() {
    var app = layui.app, $ = layui.jquery, layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    //app.set({type: 'iframe'}).init();
});
$(function() {
    var fromObj = $("#from-date");
    var toObj = $("#to-date");
    new pickerDateRange('date', {
        isTodayValid: true,
        startDate: fromObj.val(),
        endDate: toObj.val(),
        needCompare: false,
        defaultText: ' 至 ',
        autoSubmit: true,
        inputTrigger: 'input_trigger',
        theme: 'ta',
        success: function(obj) {
            fromObj.val(obj.startDate);
            toObj.val(obj.endDate);
        }
    });
    rechargeListInit();
});

function rechargeListInit() {
    var obj = {
        pageNum: 1,
        pageSize: 10,
        productId: $("#product-sel").val().trim(),
        fromDate: $("#from-date").val().trim(),
        toDate: $("#to-date").val().trim()
    };
    getRechargeList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getRechargeList(obj);
            }
        })
    });
}
var template_tr = '<tr>' +
    '<td>#{tradeTime}</td>' +
    '<td>#{tradeNo}</td>' +
    '<td>#{productName}</td>' +
    '<td>#{rechargeType}</td>' +
    '<td>#{amount}</td>' +
    '<td>#{balance}</td>' +
    '<td>#{contractNo}</td>' +
    '</tr>';

function getRechargeList(obj, pageFun) {
    $.get(
        "/product/recharge",
        {
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize'],
            "productId": obj['productId'],
            "fromDate": obj['fromDate'] == '' ? '' : obj['fromDate'] + " 00:00:00",
            "toDate": obj['toDate'] == '' ? '' : obj['toDate'] + " 23:59:59"
        },
        function(res) {
            if(res.code === '000000') {
                var dataList = $("#data-list");
                var data = res.dataMap;
                var total = data.total;
                var pages = data.pages;
                var list = data.list;
                dataList.empty();
                for(var o in list) {
                    var tr = template_tr.replace(/#{tradeTime}/g, list[o].tradeTime)
                    .replace(/#{tradeNo}/g, list[o].tradeNo)
                    .replace(/#{productName}/g, list[o].productName)
                    .replace(/#{rechargeType}/g, list[o].rechargeType)
                    .replace(/#{amount}/g, list[o].amount)
                    .replace(/#{balance}/g, list[o].balance)
                    .replace(/#{contractNo}/g, list[o].contractNo);
                    dataList.append(tr);
                }
                if(typeof pageFun === 'function') {
                    pageFun(obj, pages, total);
                }
            }
        }
    );
}

$("#search").click(function() {
    rechargeListInit();
});

function rechargeExport() {
    var productId = $("#product-sel").val();
    var fromDate = $("#from-date").val();
    var toDate = $("#to-date").val();
    var url = '/product/recharge/export?productId=' + productId
        + "&fromDate=" + (fromDate == '' ? '' : fromDate + " 00:00:00")
        + "&toDate=" + (toDate == '' ? '' : toDate + " 23:59:59");
    location.href = encodeURI(url);
}