var export_param = {};
var startDateOcx = $("#fromDate"), endDateOcx = $("#toDate"), exportOcx = $("#exportBtn");
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message', 'laydate'], function() {
    var app = layui.app, form = layui.form, table = layui.table, laydate = layui.laydate;
    app.set({type: 'iframe'}).init();
    laydate.render({
        elem: '#dates',
        btns: ['confirm'],
        format: 'yyyy/MM/dd',
        value: startDateOcx.val() + ' ~ ' + endDateOcx.val(),
        range: '~',
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
        url: '/client/recharge/list',
        where: {
            clientId: $("#clientId").val(),
            productId: $("#productId").val(),
            fromDate: startDateOcx.val(),
            toDate: endDateOcx.val()
        },
        cols: [[
            {field: 'rechargeAt', title: '充值时间'},
            {field: 'rechargeNo', title: '充值单号'},
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
            statusCode: '000000',
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        },
        done: function(res) {
            export_param.clientId = $("#clientId").val();
            export_param.productId = $("#productId").val();
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
                productId: params['product-id'],
                fromDate: params['fromDate'],
                toDate: params['toDate']
            },
            page: {
                curr: 1
            }
        });
    });
    form.on('submit(export)', function() {
        var uri = "clientId=" + export_param.clientId + "&productId=" + export_param.productId
            + "&fromDate=" + export_param.startDate + "&toDate=" + export_param.endDate;
        location.href = encodeURI('/exp/client/recharge?' + uri);
    });
});