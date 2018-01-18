var message;
layui.config({
    base: '../build/js/'
}).use(['app', 'message', 'laydate'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
});
var dateRange = new pickerDateRange('date', {
    isTodayValid: true,
    startDate: '2012-06-14',
    endDate: '2012-07-10',
    needCompare: false,
    defaultText: ' 至 ',
    autoSubmit: true,
    inputTrigger: 'input_trigger',
    theme: 'ta',
    success: function(obj) {
        alert('开始时间 : ' + obj.startDate + '<br/>结束时间 : ' + obj.endDate);
    }
});
$(function() {
    var productId = $("#product-sel").val();
    var fromDate = $("#from-date").val();
    var toDate = $("#to-date").val();
    search(productId, fromDate, toDate, 1, 15);
});
$("#search").click(function() {
    var productId = $("#product-sel").val();
    var fromDate = $("#from-date").val();
    var toDate = $("#to-date").val();
    search(productId, fromDate, toDate, 1, 15);
});
var template_tr = '<tr>' +
    '<td>#{tradeTime}</td>' +
    '<td>#{tradeNo}</td>' +
    '<td>#{productName}</td>' +
    '<td>#{rechargeType}</td>' +
    '<td>#{amount}</td>' +
    '<td>#{balance}</td>' +
    '<td>#{contractNo}</td>' +
    '</tr>';

function search(productId, fromDate, toDate, pageNum, pageSize) {
    $.get(
        "/product/recharge",
        {"productId": productId, "fromDate": fromDate, "toDate": toDate, "pageNum": pageNum, "pageSize": pageSize},
        function(res) {
            if(res.errCode === '000000') {
                var dataList = $("#data-list");
                var data = res.dataMap;
                $("#total-qty").text(data.total);
                $("#page-num").val(data.pageNum);
                $("#page-size").val(data.pageSize);
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
            }
        }
    );
}