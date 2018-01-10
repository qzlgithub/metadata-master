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
