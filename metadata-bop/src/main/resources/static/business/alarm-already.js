var form, table, layer;
layui.config({
    base: '../../static/build/js/'
}).use(['form', 'laydate', 'laypage', 'layer', 'table'], function() {
    form = layui.form;
    var laydate = layui.laydate; //日期
    laydate.render({
        elem: '#select-time',
        range: true,
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
    table = layui.table;//表格
    getWarningList();
    $(".select-type li").click(function() {
        $(".select-type li").removeClass("active");
        $(this).addClass("active");
        var attData = $(this).attr("attData");
        $("#warningType").val(attData);
        getWarningList();
    });
});

function getWarningList() {
    var main_table = table.render({
        elem: '#data-table',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/alarm/already/list',
        where: {
            type: $("#warningType").val()
        },
        cols: [[
            {field: 'warningName', title: '警报类型'},
            {field: 'name', title: '产品/客户/接口'},
            {field: 'warningAt', title: '警报时间'},
            {field: 'managerName', title: '处理人'},
            {title: '操作', align: 'center', toolbar: '#operation-bar', fixed: 'right', width: '10.3%'}
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
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                type: params['warningType'],
                managerId: params['disposeManagerId'],
                fromDate: params['fromDate'],
                toDate: params['toDate']
            },
            page: {
                curr: 1
            }
        });
    });
}