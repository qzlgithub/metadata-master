var table, main_table;
layui.config({
    base: '../../static/build/js/'
}).use(['app','table', 'form', 'laydate'], function() {
    table = layui.table;
    var form = layui.form, laydate = layui.laydate;
    laydate.render({elem: '#fromDate'});
    laydate.render({elem: '#toDate'});
    form.verify({
        none_date: function(val) {
            if(val !== '' && !new RegExp('^\\d{4}\\-\\d{2}\\-\\d{2}$').test(val)) {
                return '日期格式不正确';
            }
        }
    });
    main_table = table.render({
        elem: '#data-table',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/finance/recharge',
        where: {
            keyword: $("#keyword").val(),
            product: $("#product").val(),
            manager: $("#manager").val(),
            rechargeType: $("#rechargeType").val(),
            fromDate: $("#fromDate").val(),
            toDate: $("#toDate").val()
        },
        cols: [[
            {field: 'rechargeAt', title: '充值时间'},
            {field: 'tradeNo', title: '充值单号'},
            {field: 'corpName', title: '公司全称'},
            {field: 'shortName', title: '公司简称'},
            {field: 'username', title: '用户名'},
            {field: 'product', title: '产品服务'},
            {field: 'rechargeType', title: '充值类型'},
            {field: 'amount', title: '充值金额'},
            {field: 'balance', title: '产品余额'},
            {field: 'manager', title: '商务经理'},
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
        },
        done: function(res) {
            $("#summary").text("充值总额：￥" + res.data.totalAmt);
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                keyword: params['keyword'],
                product: params['product'],
                manager: params['manager'],
                rechargeType: params['recharge-type'],
                fromDate: params['recharge-from'],
                toDate: params['recharge-to']
            },
            page: {
                curr: 1
            }
        });
    });
});

function exportXlsx() {
    var uri_param = '';
    var params = ['keyword', 'product', 'manager', 'rechargeType', 'fromDate', 'toDate'];
    var field, val;
    for(var o in params) {
        field = params[o];
        val = $("#" + field).val();
        if(val !== '') {
            uri_param = uri_param + "&" + field + "=" + val;
        }
    }
    if(uri_param !== '') {
        uri_param = uri_param.replace('&', '?');
    }
    location.href = encodeURI('/exp/finance/recharge' + uri_param);
}