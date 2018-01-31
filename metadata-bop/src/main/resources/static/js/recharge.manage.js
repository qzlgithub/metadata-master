var message;
layui.config({
    base: '../../static/build/js/'
}).use(['form', 'app', 'message'], function() {
    var form = layui.form,
        app = layui.app,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    $('#addclass').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#add-manage'),
            area: ['500px'],
            shadeClose: true
        });
    });
    form.on('switch(switchTest)', function() {
        var id = $(this).parent().attr("obj-id");
        enable_recharge_type(id, this.checked);
    });
});

function enable_recharge_type(id, enabled) {
    $.ajax({
        type: "post",
        url: "/config/recharge/status",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "id": id,
            "enabled": enabled ? 1 : 0
        })
    });
}
$(".edit-recharge").click(function() {
    var id = $(this).attr("recharge-id");
    editRecharge(id);
});
$(".drop-recharge").click(function() {
    var id = $(this).attr("recharge-id");
    dropRecharge(id);
});

function saveNewRechargeType() {
    var newName = $("#newName").val();
    if(newName === '')
    {
        layer.msg("类型名称为必填项");
        return;
    }
    var newRemark = $("#newRemark").val();
    $.ajax({
        type: "POST",
        url: "/config/recharge/addition",
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
                    window.location.href = "/config/recharge.html";
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

function dropRecharge(id) {
    layer.confirm('是否确定删除？', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();
            $.get(
                "/config/recharge/deletion",
                {"rechargeTypeId": id},
                function() {
                    layer.msg("删除成功!", {
                        time: 2000
                    }, function() {
                        window.location.href = "/config/recharge.html";
                    });
                }
            );
            layer.closeAll();
        },
        no: function() {
            layer.closeAll();
        }
    });
}

function editRecharge(id) {
    $.get(
        "/config/recharge/info",
        {"rechargeTypeId": id},
        function(data) {
            if(data.errCode !== "000000") {
                layer.msg(data.errMsg, {
                    time: 2000
                });
            }
            else {
                var d = data.dataMap;
                $("#editId").val(d.id);
                $("#editName").val(d.name);
                $("#editRemark").val(d.remark);
                showEditBlock();
            }
        }
    );
}

function showEditBlock() {
    layer.open({
        title: false,
        type: 1,
        content: $('#edit-manage'),
        area: ['500px'],
        shadeClose: true
    });
}

function updateRechargeType() {
    $.ajax({
        type: "POST",
        url: "/config/recharge/modification",
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
                    window.location.href = "/config/recharge.html";
                });
            }
        }
    });
}
