var export_param = {};
var startDateOcx = $("#fromDate"), endDateOcx = $("#toDate"), exportOcx = $("#exportBtn");
layui.config({
    base: '/static/build/js/'
}).use(['app', 'form', 'table', 'message', 'laydate'], function() {
    var app = layui.app, form = layui.form, table = layui.table, laydate = layui.laydate;
    app.set({type: 'iframe'}).init();
    laydate.render({
        elem: '#dates',
        btns: ['confirm'],
        format: 'yyyy/MM/dd',
        value: startDateOcx.val() + ' ~ ' + endDateOcx.val(),
        range: '~',
        min: -180,
        max: 0,
        done: function(value) {
            if(value !== '') {
                var dates = value.split(" ~ ");
                startDateOcx.val(dates[0]);
                endDateOcx.val(dates[1]);
            }
        }
    });
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
            hit: $("#hit").val(),
            startTime: startDateOcx.val(),
            endTime: endDateOcx.val()
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
            statusCode: '000000',
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        },
        done: function(res) {
            export_param.clientId = $("#clientId").val();
            export_param.userId = $("#userId").val();
            export_param.productId = $("#productId").val();
            export_param.hit = $("#hit").val();
            export_param.startDate = startDateOcx.val();
            export_param.endDate = endDateOcx.val();
            if(res.total > 0) {
                exportOcx.removeAttr('disabled');
                exportOcx.removeClass('layui-btn-disabled');
            }
            else {
                exportOcx.addClass('layui-btn-disabled');
            }
        }
    });
    form.on('submit(search)', function(data) {
        exportOcx.attr('disabled', true);
        var params = data.field;
        main_table.reload({
            where: {
                clientId: params['client-id'],
                userId: params['user-id'],
                productId: params['product-id'],
                startTime: params['fromDate'],
                endTime: params['toDate'],
                hit: params['hit']
            },
            page: {
                curr: 1
            }
        });
    });
    form.on('submit(export)', function() {
        var uri = "clientId=" + export_param.clientId + "&userId=" + export_param.userId + "&productId="
            + export_param.productId + "&hit=" + export_param.hit + "&fromDate=" + export_param.startDate
            + "&toDate=" + export_param.endDate;
        location.href = encodeURI('/exp/client/request?' + uri);
    });
});