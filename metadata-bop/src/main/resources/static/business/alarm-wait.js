var table, layer;
layui.config({
    base: '../../static/build/js/'
}).use(['laydate', 'laypage', 'layer', 'table'], function() {
    var laydate = layui.laydate; //日期
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
    table.render({
        elem: '#data-table',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/alarm/wait/list',
        where: {
            type: $("#warningType").val()
        },
        cols: [[
            {field: 'warningName', title: '警报类型'},
            {field: 'name', title: '产品/客户/接口'},
            {field: 'warningAt', title: '警报时间'},
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
}