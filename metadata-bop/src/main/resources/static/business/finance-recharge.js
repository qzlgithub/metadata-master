var table, main_table;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'table', 'form', 'laydate'], function() {
    table = layui.table;
    var form = layui.form, laydate = layui.laydate;
    laydate.render({
        elem: '#dates'
        , range: true,
        done: function(value, date) {
            if(value != "") {
                var dates = value.split(" - ");
                $("#fromDate").val(dates[0]);
                $("#toDate").val(dates[1]);
            }
            else {
                $("#fromDate").val("");
                $("#toDate").val("");
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
            {field: 'rechargeAt', title: '充值时间', width: '10%'},
            {field: 'tradeNo', title: '充值单号', width: '10%'},
            {field: 'corpName', title: '公司名称', width: '10%'},
            {field: 'product', title: '产品服务', width: '10%'},
            {field: 'rechargeType', title: '充值类型', width: '6%'},
            {field: 'amount', title: '充值金额', width: '10%'},
            {field: 'content', title: '服务时间/单价', width: '15%'},
            {field: 'manager', title: '经手人', width: '10%'},
            {field: 'contractNo', title: '合同编号', width: '10%'},
            {field: 'remark', title: '备注'}
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
                fromDate: params['fromDate'],
                toDate: params['toDate']
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
        if(val !== '' && val !== undefined) {
            uri_param = uri_param + "&" + field + "=" + val;
        }
    }
    if(uri_param !== '') {
        uri_param = uri_param.replace('&', '?');
    }
    location.href = encodeURI('/exp/finance/recharge' + uri_param);
}