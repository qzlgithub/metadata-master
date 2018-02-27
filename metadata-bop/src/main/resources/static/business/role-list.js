var form, message, table, main_table;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message'], function() {
    var app = layui.app;
    app.set({type: 'iframe'}).init();
    message = layui.message;
    form = layui.form;
    table = layui.table;
    main_table = table.render({
        elem: '#data-table',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/account/role/list',
        cols: [[
            {field: 'name', title: '分组名称', width: '15%'},
            {field: 'privilege', title: '权限类型'},
            {title: '操作', align: 'center', toolbar: '#operation-bar', fixed: 'right', width: '15%'}
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
});

function changeStatus(id) {
    layer.confirm('是否确定？', {
        btn: ['确定', '取消'],
        yes: function() {
            $.ajax({
                type: "POST",
                url: "/account/role/status",
                contentType: "application/json",
                data: JSON.stringify({"id": id}),
                success: function(res) {
                    if(res.code === '000000') {
                        var obj = res.data;
                        if(obj.enabled === 1) {
                            layer.msg("启用成功", {
                                time: 2000
                            });
                        }
                        else {
                            layer.msg("禁用成功", {
                                time: 2000
                            });
                        }
                        main_table.reload();
                    }
                }
            });
            layer.closeAll();
        },
        btn2: function() {
            layer.closeAll();
        }
    });
}