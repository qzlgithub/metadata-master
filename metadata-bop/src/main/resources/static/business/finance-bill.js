layui.config({
    base: '../../static/build/js/'
}).use(['app','form', 'table', 'laydate'], function() {
    var form = layui.form,
        laydate = layui.laydate,
        table = layui.table;
    laydate.render({elem: '#fromDate'});
    laydate.render({elem: '#toDate'});
    form.verify({
        none_date: function(val) {
            if(val !== '' && !new RegExp('^\\d{4}\\-\\d{2}\\-\\d{2}$').test(val)) {
                return '日期格式不正确';
            }
        }
    });
    var main_table = table.render({
        elem: '#dataTable',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/finance/bill',
        where: {
            keyword: $("#keyword").val(),
            product: $("#product").val(),
            billPlan: $("#billPlan").val(),
            fromDate: $("#fromDate").val(),
            toDate: $("#toDate").val()
        },
        cols: [[
            {field: 'requestAt', title: '请求时间'},
            {field: 'tradeNo', title: '消费单号'},
            {field: 'corpName', title: '公司全称'},
            {field: 'shortName', title: '公司简称'},
            {field: 'username', title: '公司账号'},
            {field: 'product', title: '产品服务'},
            {field: 'billPlan', title: '计费方式'},
            {title: '是否击中', templet: "#hittpl"},
            {field: 'fee', title: '消费（元）'},
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
        },
        done: function(res) {
            $("#summary").text("共请求" + res.total + "次，收入：￥" + res.extradata.totalFee);
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                keyword: params['keyword'],
                product: params['product'],
                billPlan: params['bill-plan'],
                fromDate: params['from-date'],
                toDate: params['to-date']
            },
            page: {
                curr: 1
            }
        });
    });
});

function exportXlsx() {
    var uri_param = '';
    var params = ['keyword', 'product', 'billPlan', 'fromDate', 'toDate'];
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
    location.href = encodeURI('/finance/bill/export' + uri_param);
}
