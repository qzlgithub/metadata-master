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
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/articles/list',
        cols: [[
            {field: 'orderId', title: '排序号'},
            {title: '类别', templet: "#typeName"},
            {field: 'title', title: '文章标题'},
            {title: '状态', templet: "#statusTpl"},
            {field: 'publishTime', title: '发布时间'},
            {title: '操作', align: 'center', toolbar: '#operationBar', fixed: 'right'}
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

function deleteArticles(id) {
    layer.confirm('确定删除该文章？', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();
            $.ajax({
                type: "POST",
                url: "/articles/deletion",
                data: {"id": id},
                success: function(res) {
                    if(res.code === '000000') {
                        layer.msg("删除成功", {
                            time: 1000
                        }, function() {
                            layer.closeAll();
                            window.location.reload();
                        });
                    }
                    else {
                        layer.msg("删除失败", {
                            time: 2000
                        }, function() {
                            layer.closeAll();
                        });
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