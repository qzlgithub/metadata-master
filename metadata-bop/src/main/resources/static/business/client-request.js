layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message', 'laydate'], function() {
    var app = layui.app, form = layui.form, table = layui.table, laydate = layui.laydate;
    app.set({type: 'iframe'}).init();
    laydate.render({elem: '#fromDate'});
    laydate.render({elem: '#toDate'});
    var main_table = table.render({
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
            {field: 'isHit', title: '是否击中', templet: "#hitTpl"},
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
    form.on('submit(export)', function(data) {
        var params = data.field;
        var uri = "/exp/client/request?clientId=" + params["client-id"] + "&userId=" + params['user-id']
            + "&productId=" + params['product-id'] + "&fromDate=" + params['from-date'] + "&toDate=" + params['to-date'];
        location.href = encodeURI(uri);
    });
});