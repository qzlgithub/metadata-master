var table, main_table, exportOcx = $("#exportBtn");
var export_param = {}, startDateOcx = $("#fromDate"), endDateOcx = $("#toDate");
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'table', 'form', 'laydate'], function() {
    table = layui.table;
    var form = layui.form, laydate = layui.laydate;
    laydate.render({
        elem: '#dates',
        btns: ['confirm'],
        format: 'yyyy/MM/dd',
        value: startDateOcx.val() + ' ~ ' + endDateOcx.val(),
        range: '~',
        max: 0,
        done: function(value) {
            if(value !== "") {
                var dates = value.split(" ~ ");
                $("#fromDate").val(dates[0]);
                $("#toDate").val(dates[1]);
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
            export_param.keyword = $("#keyword").val();
            export_param.product = $("#product").val();
            export_param.manager = $("#manager").val();
            export_param.rechargeType = $("#rechargeType").val();
            export_param.fromDate = $("#fromDate").val();
            export_param.toDate = $("#toDate").val();
            $("#summary").text("充值总额：￥" + res.data.totalAmt);
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
$(function() {
    exportOcx.on('click', function() {
        exportXlsx();
    })
});

function exportXlsx() {
    var uri_param = 'keyword=' + export_param.keyword + '&product=' + export_param.product + '&manager='
        + export_param.manager + '&rechargeType=' + export_param.rechargeType + '&fromDate=' + export_param.fromDate
        + '&toDate=' + export_param.toDate;
    location.href = encodeURI('/exp/finance/recharge?' + uri_param);
}