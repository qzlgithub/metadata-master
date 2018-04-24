var startDateOcx = $("#startDate"), endDateOcx = $("#endDate");
layui.use(['form', 'laydate', 'table'], function() {
    var table = layui.table;
    var laydate = layui.laydate;
    var form = layui.form;
    laydate.render({
        elem: '#dateRange',
        btns: ['confirm'],
        format: 'yyyy/MM/dd',
        value: startDateOcx.val() + ' ~ ' + endDateOcx.val(),
        range: '~',
        min: -180,
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
        url: '/product/recharge',
        where: {
            productId: $("#product-sel").val(),
            fromDate: startDateOcx.val(),
            toDate: endDateOcx.val()
        },
        cols: [[
            {field: 'tradeTime', title: '时间', align: 'center', width: '20%'},
            {field: 'tradeNo', title: '充值单号', align: 'center', width: '20%'},
            {field: 'productName', title: '产品服务', align: 'center', width: '13%'},
            {field: 'rechargeType', title: '充值类型', align: 'center', width: '13%'},
            {field: 'amount', title: '充值金额', align: 'center', width: '10%'},
            {field: 'balance', title: '产品余额', align: 'center', width: '12%'},
            {field: 'contractNo', title: '合同编号', align: 'center', width: '12%'}
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
            if(res.total > 0) {
                $("#exportOcx").removeAttr("disabled");
            }
            else {
                $("#exportOcx").attr('disabled', "true");
            }
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                productId: params['product'],
                fromDate: params['start-date'],
                toDate: params['end-date']
            },
            page: {
                curr: 1
            }
        });
    });
});

function rechargeExport() {
    var productId = $("#product-sel").val();
    var fromDate = startDateOcx.val();
    var toDate = endDateOcx.val();
    var url = '/product/recharge/export?productId=' + productId + "&fromDate=" + fromDate + "&toDate=" + toDate;
    location.href = encodeURI(url);
}