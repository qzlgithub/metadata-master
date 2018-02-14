var form, message, table, main_table, laydate;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message', 'laydate'], function() {
    var app = layui.app;
    app.set({type: 'iframe'}).init();
    message = layui.message;
    form = layui.form;
    table = layui.table;
    laydate = layui.laydate;
    laydate.render({
        elem: '#start-time'
    });
    laydate.render({
        elem: '#end-time'
    });
    main_table = table.render({
        elem: '#data-table',
        page: true,
        limit: 3,
        limits: [3, 15, 30, 50],
        url: '/client/rechargeList',
        where: {
            clientId: $("#client-id").val(),
            productId: $("#product").val(),
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
            {field: 'rechargeType', title: '充值类型'},
            {field: 'amount', title: '充值金额'},
            {field: 'balance', title: '产品服务余额'},
            {field: 'managerName', title: '经手人'},
            {field: 'contractNo', title: '合同编号'},
            {field: 'remark', title: '备注'}
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

function exportToExcel() {
    var clientId = $("#client-id").val();
    var productId = $("#product").val();
    var startTime = $("#start-time").val();
    var endTime = $("#end-time").val();
    location.href = "/client/product/recharge/export?clientId=" + clientId + "&productId=" + productId + "&startTime=" + startTime + "&endTime=" + endTime;
}