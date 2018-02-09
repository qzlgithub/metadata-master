var message, form, table, main_table;
layui.config({base: '../../build/js/'}).use(['app', 'message'], function() {
    var app = layui.app;
    form = layui.form;
    message = layui.message;
    app.set({type: 'iframe'}).init();
    table = layui.table;
    form.render();
    main_table = table.render({
        elem: '#data-table',
        page: true,
        limit: 3,
        limits: [3, 15, 30, 50],
        url: '/product/list',
        where: {
            keyword: $("#keyword").val(),
            type: $("#type").val(),
            custom: $("#custom").val(),
            status: $("#status").val()
        },
        cols: [[
            {field: 'id', title: 'No.', width: 80},
            {field: 'code', title: '编号', width: 200},
            {field: 'name', title: '名称', width: 200},
            {field: 'type', title: '类别', width: 200},
            {title: '种类', width: 160, templet: '#custom-tpl'},
            {field: 'costAmt', title: '成本价', width: 160},
            {title: '状态', width: 160, templet: "#status-tpl"},
            {field: 'remark', title: '备注说明', width: 240},
            {title: '操作', align: 'center', toolbar: '#operation-bar', fixed: 'right', width: 180}
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
    table.on('tool(main)', function(obj) {
        var curr_data = obj.data, event = obj.event;
        console.log(curr_data + " - " + event);
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
            layer.confirm('是否确定' + remind_txt + '该产品？请谨慎操作！', {
                btn: ['确定', '取消'],
                yes: function() {
                    $.ajax({
                        type: "POST",
                        url: "/product/status",
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify({"id": curr_data.id, "enabled": status}),
                        success: function(data) {
                            if(data.errCode === '000000') {
                                main_table.reload();
                            }
                            else {
                                layer.msg("操作失败:" + data.errMsg, {time: 2000});
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

/**
 * 列表查询
 */
function search() {
    main_table.reload({
        where: {
            keyword: $("#keyword").val(),
            type: $("#type").val(),
            custom: $("#custom").val(),
            status: $("#status").val()
        },
        page: {
            curr: 1
        }
    });
}