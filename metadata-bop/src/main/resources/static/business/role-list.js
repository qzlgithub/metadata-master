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
        elem: '#dataTable',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/account/role/list',
        cols: [[
            {field: 'name', title: '角色名称', width: '15%'},
            {field: 'module', title: '功能模块'},
            {title: '操作', align: 'center', toolbar: '#operationBar', fixed: 'right', width: '15%'}
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
    table.on('tool(dataTable)', function(obj) {
        var curr_data = obj.data, event = obj.event;
        if(event === 'disable' || event === 'enable') {
            var remind_txt, status;
            if(event === 'disable') {
                remind_txt = '禁用';
                status = 0;
            }
            else {
                remind_txt = '启用';
                status = 1;
            }
            layer.confirm('确定' + remind_txt + '该角色？', {
                btn: ['确定', '取消'],
                yes: function() {
                    $.ajax({
                        type: "POST",
                        url: "/account/role/status",
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify({"roleId": curr_data.id, "enabled": status}),
                        success: function(res) {
                            if(res.code === '000000') {
                                main_table.reload();
                            }
                            else {
                                layer.msg("操作失败:" + res.message, {time: 2000});
                            }
                        }
                    });
                    layer.closeAll();
                },
                no: function() {
                    layer.closeAll();
                }
            });
        }
    });
});