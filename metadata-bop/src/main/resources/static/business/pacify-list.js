var form, message, table, main_table;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message', 'laydate'], function() {
    var app = layui.app;
    app.set({type: 'iframe'}).init();
    message = layui.message;
    form = layui.form;
    table = layui.table;
    var laydate = layui.laydate;
    laydate.render({
        elem: '#dates',
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
    main_table = table.render({
        elem: '#data-table',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/pacify/list',
        id:'table-main',
        where: {
            keyword: $.trim($("#keyword").val()),
            status: $("#status").val(),
            dispose: $("#dispose").val(),
            fromDate: $("#fromDate").val(),
            toDate: $("#toDate").val()
        },
        cols: [[
            {field: 'corpName', title: '公司名称'},
            {field: 'linkName', title: '联系人'},
            {field: 'phone', title: '联系方式'},
            {field: 'productName', title: '出错产品'},
            {title: '错误状态', templet: "#errorStatus-tpl"},
            {field: 'errorTime', title: '出错时间'},
            {title: '安抚状态', templet: "#dispose-tpl"},
            {title: '操作', align: 'center', toolbar: '#operation-bar', fixed: 'right'}
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
                keyword: params['keyword'],
                status: params['status'],
                dispose: params['dispose'],
                fromDate: params['fromDate'],
                toDate: params['toDate']
            },
            page: {
                curr: 1
            }
        });
    });
});

function showNotice(id) {
    $("#pacifyId").val(id);
    $("#remark").val("");
    layer.open({
        title: false,
        type: 1,
        content: $('#notice-warp'),
        area: ['500px'],
        shadeClose: true
    });
}

var isSubmit = false;

function saveRemark() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    var remark = $.trim($("#remark").val());
    if(remark == '') {
        layer.msg("备注不能为空！", {
            time: 2000
        });
        isSubmit = false;
        return;
    }
    var pacifyId = $("#pacifyId").val();
    $.ajax({
        type: "POST",
        url: "/pacify/dispose",
        data: {"id": pacifyId, "remark": remark},
        success: function(res) {
            if(res.code === '000000') {
                layer.msg("保存成功", {
                    time: 1000
                }, function() {
                    layer.closeAll();
                    table.reload('table-main');
                });
            }
            else {
                layer.msg("保存失败", {
                    time: 2000
                }, function() {
                    layer.closeAll();
                });
                isSubmit = false;
            }
        }
    });
}