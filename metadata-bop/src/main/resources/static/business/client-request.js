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
        url: '/client/request/list',
        where: {
            clientId: $("#clientId").val(),
            userId: $("#userId").val(),
            productId: $("#productId").val(),
            startTime: $("#fromDate").val(),
            endTime: $("#toDate").val()
        },
        cols: [[
            {field: 'requestAt', title: '请求时间'},
            {field: 'requestNo', title: '请求单号'},
            {field: 'username', title: '客户账号'},
            {field: 'productName', title: '产品服务'},
            {field: 'billPlan', title: '计费方式'},
            {field: 'isHit', title: '是否击中'},
            {field: 'fee', title: '费用（元）'},
            {field: 'balance', title: '余额（元）'}
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
                userId: params['user-id'],
                productId: params['product-id'],
                startTime: params['from-date'],
                endTime: params['to-date']
            },
            page: {
                curr: 1
            }
        });
    });
});

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