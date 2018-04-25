var export_param = {};
var startDateOcx = $("#fromDate"), endDateOcx = $("#toDate"), exportOcx = $("#exportBtn");
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'laydate'], function() {
    var form = layui.form,
        laydate = layui.laydate,
        table = layui.table;
    laydate.render({
        elem: '#dates',
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
        url: '/finance/bill',
        where: {
            keyword: $("#keyword").val(),
            product: $("#product").val(),
            billPlan: $("#billPlan").val(),
            fromDate: $("#fromDate").val(),
            toDate: $("#toDate").val(),
            hit: $("#hit").val()
        },
        cols: [[
            {field: 'requestAt', title: '消费时间'},
            {field: 'tradeNo', title: '消费单号'},
            {field: 'corpName', title: '公司名称'},
            {field: 'username', title: '消费账号'},
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
            statusCode: '000000',
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        },
        done: function(res) {
            export_param.keyword = $("#keyword").val();
            export_param.product = $("#product").val();
            export_param.billPlan = $("#billPlan").val();
            export_param.hit = $("#hit").val();
            export_param.fromDate = $("#fromDate").val();
            export_param.toDate = $("#toDate").val();
            $("#summary").text("共请求" + res.total + "次，收入：￥" + res.data.totalFee);
            if(res.total > 0) {
                exportOcx.removeAttr('disabled');
                exportOcx.removeClass('layui-btn-disabled');
            }
            else {
                exportOcx.attr('disabled', true);
                exportOcx.addClass('layui-btn-disabled');
            }
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                keyword: params['keyword'],
                product: params['product'],
                billPlan: params['bill-plan'],
                fromDate: params['fromDate'],
                toDate: params['toDate'],
                hit: params['hit']
            },
            page: {
                curr: 1
            }
        });
    });
});

function exportXlsx() {
    var uri_param = 'keyword=' + export_param.keyword + '&product=' + export_param.product + '&billPlan='
        + export_param.billPlan + '&hit=' + export_param.hit + '&fromDate=' + export_param.fromDate
        + '&toDate=' + export_param.toDate;
    location.href = encodeURI('/exp/finance/request?' + uri_param);
}
