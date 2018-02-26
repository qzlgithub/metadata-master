var message;
layui.config({
    base: '../../static/build/js/'
}).use(['form', 'app', 'message', 'table'], function() {
    var form = layui.form,
        app = layui.app,
        layer = layui.layer,
        table = layui.table;
    message = layui.message;
    app.set({type: 'iframe'}).init();
    table.render({
        elem: '#dataTable',
        url: '/dict/recharge/list',
        cols: [[
            {field: 'name', title: '名称', width: '10%'},
            {field: 'remark', title: '备注', width: '30%'},
            {field: 'addAt', title: '添加时间', width: '15%', sort: true},
            {title: '状态', align: 'center', templet: "#statusTpl", width: '20%'},
            {title: '操作', align: 'center', toolbar: '#operationBar'}
        ]],
        response: {
            statusName: 'code',
            statusCode: 0,
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        }
    });
    $('#addBt').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#addDiv'),
            area: ['500px'],
            shadeClose: true
        });
    });
    table.on('tool(main-table)', function(obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if(layEvent === 'edit') {
            $("#editId").val(data.id);
            $("#editName").val(data.name);
            $("#editRemark").val(data.remark);
            layer.open({
                title: false,
                type: 1,
                content: $('#editDiv'),
                area: ['500px'],
                shadeClose: true
            });
        }
        else if(layEvent === 'del') {
            del_recharge_type(data.id);
        }
    });
    form.on('switch(status)', function(obj) {
        change_status(this.value, obj.elem.checked);
    });
});

/**
 * 启用/禁用充值类型
 * @param id 充值类型ID
 * @param enabled true-启用，false-禁用
 */
function change_status(id, enabled) {
    $.ajax({
        type: "POST",
        url: "/dict/recharge/status",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "id": id,
            "enabled": enabled ? 1 : 0
        })
    });
}

/**
 * 新增充值类型
 */
function add_recharge_type() {
    var newName = $("#newName").val();
    if(newName === '') {
        layer.msg("类型名称为必填项");
        return;
    }
    var newRemark = $("#newRemark").val();
    $.ajax({
        type: "PUT",
        url: "/dict/recharge",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "name": newName,
            "remark": newRemark
        }),
        success: function(data) {
            if(data.errCode === "000000") {
                layer.msg("添加成功!", {
                    time: 2000
                }, function() {
                    window.location.href = "/setting/recharge.html";
                });
            }
            else {
                layer.msg("添加失败：" + data.errMsg, {
                    time: 2000
                });
            }
        }
    });
}

/**
 * 删除充值类型
 */
function del_recharge_type(id) {
    layer.confirm('确定删除？', {
        btn: ['确定', '取消'],
        yes: function() {
            $.ajax({
                type: "DELETE",
                url: "/dict/recharge",
                contentType: "application/json",
                data: JSON.stringify({"id": id}),
                success: function() {
                    window.location.href = "/setting/recharge.html";
                }
            });
        },
        no: function() {
            layer.closeAll();
        }
    });
}

/**
 * 编辑充值类型
 */
function edit_recharge_type() {
    $.ajax({
        type: "POST",
        url: "/dict/recharge",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "id": $("#editId").val(),
            "name": $("#editName").val(),
            "remark": $("#editRemark").val()
        }),
        success: function(data) {
            if(data.errCode !== "000000") {
                layer.msg("修改失败:" + data.errMsg, {
                    time: 2000
                });
            }
            else {
                layer.msg("修改成功!", {
                    time: 2000
                }, function() {
                    window.location.href = "/setting/recharge.html";
                });
            }
        }
    });
}
