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
    requestListInit();
});

function requestListInit() {
    var obj = {
        pageNum: 1,
        pageSize: 10,
        productId: $("#product-sel").val().trim(),
        fromDate: $("#from-date").val().trim(),
        toDate: $("#to-date").val().trim()
    };
    getRequestList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getRequestList(obj);
            }
        })
    });
}
var template_tr = '<tr>' +
    '<td>#{tradeAt}</td>' +
    '<td>#{tradeNo}</td>' +
    '<td>#{productName}</td>' +
    '<td>#{billPlan}</td>' +
    '<td>#{hit}</td>' +
    '<td>#{unitAmt}</td>' +
    '<td>#{balance}</td>' +
    '</tr>';

function getRequestList(obj, pageFun) {
    $.get(
        "/product/request/list",
        {
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize'],
            "productId": obj['productId'],
            "fromDate": obj['fromDate'] == '' ? '' : obj['fromDate'] + " 00:00:00",
            "toDate": obj['toDate'] == '' ? '' : obj['toDate'] + " 23:59:59"
        },
        function(res) {
            if(res.errCode === '000000') {
                var dataList = $("#data-list");
                var data = res.dataMap;
                var total = data.total;
                var pages = data.pages;
                var list = data.list;
                dataList.empty();
                for(var o in list) {
                    var tr = template_tr.replace(/#{tradeAt}/g, list[o].tradeAt)
                    .replace(/#{tradeNo}/g, list[o].tradeNo)
                    .replace(/#{productName}/g, list[o].productName)
                    .replace(/#{billPlan}/g, list[o].billPlan)
                    .replace(/#{hit}/g, list[o].hit)
                    .replace(/#{unitAmt}/g, list[o].unitAmt)
                    .replace(/#{balance}/g, list[o].balance);
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
    requestListInit();
});

function requestExport() {
    var productId = $("#product-sel").val();
    var fromDate = $("#from-date").val();
    var toDate = $("#to-date").val();
    var url = '/product/request/export?productId=' + productId
        + "&fromDate=" + (fromDate == '' ? '' : fromDate + " 00:00:00")
        + "&toDate=" + (toDate == '' ? '' : toDate + " 23:59:59");
    location.href = encodeURI(url);
}