var export_param = {}, startDateOcx = $("#startDate"), endDateOcx = $("#endDate"), exportOcx = $("#exportBtn");
layui.config({
    base: '../build/js/'
}).use(['app', 'form', 'message', 'laydate', 'table'], function() {
    var laydate = layui.laydate;
    var table = layui.table;
    var form = layui.form;
    laydate.render({
        elem: '#dateRange',
        btns: ['confirm'],
        format: 'yyyy/MM/dd',
        value: startDateOcx.val() + ' ~ ' + endDateOcx.val(),
        range: '~',
        min: -180,
        max: 0,
        done: function(value) {
            if(value !== '') {
                var dates = value.split(' ~ ');
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
        url: '/product/request/list',
        where: {
            productId: $("#product-sel").val(),
            hit: $("#hit").val(),
            fromDate: startDateOcx.val(),
            toDate: endDateOcx.val()
        },
        cols: [[
            {field: 'tradeAt', title: '查询时间', align: 'center', width: '20%'},
            {field: 'tradeNo', title: '查询单号', align: 'center', width: '20%'},
            {field: 'productName', title: '产品服务', align: 'center', width: '13%'},
            {field: 'billPlan', title: '计费方式', align: 'center', width: '13%'},
            {field: 'hit', title: '是否成功', align: 'center', width: '10%'},
            {field: 'unitAmt', title: '消费（元）', align: 'center', width: '12%'},
            {field: 'balance', title: '余额（元）', align: 'center', width: '12%'}
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
            export_param.productId = $("#product-sel").val();
            export_param.hit = $("#hit").val();
            export_param.startDate = startDateOcx.val();
            export_param.endDate = endDateOcx.val();
            if(res.total > 0) {
                exportOcx.removeAttr("disabled");
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
                productId: params['product'],
                hit: params['hit'],
                fromDate: params['start-date'],
                toDate: params['end-date']
            },
            page: {
                curr: 1
            }
        });
    });
});
$(function() {
    exportOcx.on('click', function() {
        requestExport();
    });
});

function requestExport() {
    var url = 'productId=' + export_param.productId + "&hit=" + export_param.hit + "&fromDate="
        + export_param.startDate + "&toDate=" + export_param.endDate;
    location.href = encodeURI('/product/request/export?' + url);
}