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
        limit: 3,
        limits: [3, 15, 30, 50],
        url: '/manager/list',
        where: {
            roleId: $("#roleId").val(),
            enabled: $("#enabled").val()
        },
        cols: [[
            {field: 'id', title: '编号', width: '15%'},
            {field: 'username', title: '用户名', width: '10%'},
            {field: 'name', title: '姓名', width: '15%'},
            {field: 'phone', title: '联系电话', width: '10%'},
            {field: 'roleName', title: '角色', width: '15%'},
            {title: '状态', width: '10%',templet: "#status-tpl"},
            {field: 'registerDate', title: '注册日期', sort: true, width: '10%'},
            {title: '操作', align: 'center', toolbar: '#operation-bar', fixed: 'right'}
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
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                roleId: params["roleId"],
                enabled: params['enabled']
            },
            page: {
                curr: 1
            }
        });
    });
});

function changeStatus(id) {
    layer.confirm('是否确定？', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();
            $.ajax({
                type: "POST",
                url: "/manager/changeStatus",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": id}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        var obj = data.dataMap;
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