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
        elem: '#fromDate'
    });
    laydate.render({
        elem: '#toDate'
    });
    main_table = table.render({
        elem: '#dataTable',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/client/recharge/list',
        where: {
            clientId: $("#clientId").val(),
            productId: $("#productId").val(),
            fromDate: $("#fromDate").val(),
            toDate: $("#toDate").val()
        },
        cols: [[
            {field: 'rechargeAt', title: '时间'},
            {field: 'rechargeNo', title: '消费单号'},
            {field: 'productName', title: '产品服务'},
            {field: 'rechargeTypeName', title: '充值类型'},
            {field: 'billPlanName', title: '计费方式'},
            {field: 'amount', title: '充值金额'},
            {field: 'balance', title: '产品余额'},
            {field: 'managerName', title: '经手人'},
            {field: 'contractNo', title: '合同编号'}
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
                clientId: params['client-id'],
                productId: params['product-id'],
                fromDate: params['from-date'],
                toDate: params['to-date']
            },
            page: {
                curr: 1
            }
        });
    });
    form.on('submit(export)', function(data) {
        var params = data.field;
        var uri = "/client/recharge/export?clientId=" + params["client-id"] + "&productId=" + params['product-id']
            + "&fromDate=" + params['from-date'] + "&toDate=" + params['to-date'];
        location.href = encodeURI(uri);
    });
});