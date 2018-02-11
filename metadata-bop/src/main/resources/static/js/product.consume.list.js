var form, message, table, main_table,laydate;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message','laydate'], function() {
    var app = layui.app;
    app.set({type: 'iframe'}).init();
    message = layui.message;
    form = layui.form;
    table = layui.table;
    laydate = layui.laydate;
    laydate.render({
        elem: '#start-time'
    });laydate.render({
        elem: '#end-time'
    });
    main_table = table.render({
        elem: '#data-table',
        page: true,
        limit: 3,
        limits: [3, 15, 30, 50],
        url: '/client/consumeList',
        where: {
            productId: $("#product").val(),
            clientId: $("#client-id").val(),
            startTime: $("#start-time").val(),
            endTime: $("#end-time").val()
        },
        cols: [[
            {field: 'tradeAt', title: '时间'},
            {field: 'tradeNo', title: '消费单号'},
            {field: 'corpName', title: '公司'},
            {field: 'shortName', title: '公司简称'},
            {field: 'username', title: '账号'},
            {field: 'productName', title: '产品服务'},
            {field: 'billPlan', title: '计费方式'},
            {field: 'hit', title: '是否击中'},
            {field: 'unitAmt', title: '消费（元）'},
            {field: 'balance', title: '金额（元）'},
        ]],
        request: {
            pageName: 'pageNum', limitName: 'pageSize'
        },
        response: {
            statusName: 'code',
            statusCode: 0,
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                productId: params['product'],
                clientId: params['client-id'],
                startTime: params['start-time'],
                endTime: params['end-time']
            },
            page: {
                curr: 1
            }
        });
    });
});

//var message;
//layui.config({
//    base: '../../static/build/js/'
//}).use(['app', 'message', 'laydate'], function() {
//    var app = layui.app,
//        $ = layui.jquery,
//        layer = layui.layer;
//    laydate = layui.laydate;
//    //将message设置为全局以便子页面调用
//    message = layui.message;
//    //主入口
//    app.set({
//        type: 'iframe'
//    }).init();
//    $('#pay').on('click', function() {
//        layer.open({
//            title: false,
//            type: 1,
//            content: '<img src="../../static/build/images/pay.png" />',
//            area: ['500px', '250px'],
//            shadeClose: true
//        });
//    });
//    //日期
//    laydate.render({
//        elem: '#start-time'
//    });
//    laydate.render({
//        elem: '#end-time'
//    });
//});
//
//$(function() {
//    prodConsumeListInit();
//});
//
//function prodConsumeListInit(){
//    var obj = {
//        pageNum: 1,
//        pageSize: 10,
//        productId: $("#product").val(),
//        clientId: $("#client-id").val(),
//        startTime: $("#start-time").val(),
//        endTime: $("#end-time").val()
//    };
//    getProdConsumeList(obj, function(pageObj, pages, total) {
//        $('#pagination').paging({
//            initPageNo: pageObj['pageNum'],
//            totalPages: pages,
//            totalCount: '合计' + total + '条数据',
//            slideSpeed: 600,
//            jump: false,
//            callback: function(currentPage) {
//                pageObj['pageNum'] = currentPage;
//                getProdConsumeList(obj);
//            }
//        })
//    });
//}
//
//var rowStr = '<tr>' +
//    '<td>#{createTime}</td>' +
//    '<td>#{tradeNo}</td>' +
//    '<td>#{clientName}</td>' +
//    '<td>#{shortName}</td>' +
//    '<td>#{username}</td>' +
//    '<td>#{productName}</td>' +
//    '<td>#{billPlan}</td>' +
//    '<td>#{hit}</td>' +
//    '<td>#{unitAmt}</td>' +
//    '<td>#{balance}</td>' +
//    '</tr>';
//
//function getProdConsumeList(obj, pageFun) {
//    $.get("/client/consumeList",
//        {
//            "pageNum": obj['pageNum'],
//            "pageSize": obj['pageSize'],
//            "productId": obj['productId'],
//            "clientId": obj['clientId'],
//            "startTime": obj['startTime'] == '' ? '' : obj['startTime'] + " 00:00:00",
//            "endTime": obj['endTime'] == '' ? '' : obj['endTime'] + " 23:59:59"
//        }
//        , function(data) {
//            var list = data.list;
//            var total = data.total;
//            var pages = data.pages;
//            $("#dataBody").empty();
//            for(var d in list) {
//                var row = rowStr.replace("#{createTime}", list[d].tradeAt)
//                .replace("#{tradeNo}", list[d].tradeNo)
//                .replace("#{clientName}", list[d].corpName)
//                .replace("#{shortName}", list[d].shortName)
//                .replace("#{username}", list[d].username)
//                .replace("#{productName}", list[d].productName)
//                .replace("#{billPlan}", list[d].billPlan)
//                .replace("#{hit}", list[d].hit)
//                .replace("#{unitAmt}", list[d].unitAmt)
//                .replace("#{balance}", list[d].balance);
//                $("#dataBody").append(row);
//            }
//            if(typeof pageFun === 'function') {
//                pageFun(obj, pages, total);
//            }
//        });
//}

function consumeOutPrint() {
    var productId = $("#product").val().trim();
    var clientId = $("#client-id").val().trim();
    var startDate = $("#start-time").val().trim();
    var endDate = $("#end-time").val().trim();
    var url = '/client/consumeList/export?clientId=' + clientId + "&productId=" + productId
        + "&startTime=" + (startDate == '' ? '' : startDate + " 00:00:00")
        + "&endTime=" + (endDate == '' ? '' : endDate + " 23:59:59");
    location.href = encodeURI(url);
}