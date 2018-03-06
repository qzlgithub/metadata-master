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
        url: '/account/list',
        where: {
            roleType: $("#roleType").val(),
            enabled: $("#enabled").val()
        },
        cols: [[
            {field: 'username', title: '用户名', width: '10%'},
            {field: 'name', title: '姓名', width: '15%'},
            {field: 'phone', title: '联系电话', width: '10%'},
            {title: '角色', width: '15%', templet: "#roleTypeTpl"},
            {field: 'groupName', title: '分组', width: '15%'},
            {title: '状态', width: '10%', templet: "#status-tpl"},
            {field: 'registerDate', title: '注册日期', sort: true, width: '10%'},
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
                roleType: params["roleType"],
                enabled: params['enabled']
            },
            page: {
                curr: 1
            }
        });
    });
    table.on('tool(dataTable)', function(obj) {
        var data = obj.data, layEvent = obj.event;
        if(layEvent === 'enable' || layEvent === 'disable') {
            var tip, status;
            if(layEvent === 'enable') {
                tip = '启用';
                status = 1;
            }
            else {
                tip = '禁用';
                status = 0;
            }
            layer.confirm('确定' + tip + '？', {
                btn: ['确定', '取消'],
                yes: function() {
                    $.ajax({
                        type: "POST",
                        url: "/account/status",
                        contentType: "application/json",
                        data: JSON.stringify({"id": data.id, "status": status}),
                        success: function(res) {
                            if(res.code === '000000') {
                                layer.msg(tip + "成功", {time: 2000});
                                main_table.reload();
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