// var rowTr = "<tr><td>${id}</td><td>${name}</td><td>${remark}</td><td><div class=\"layui-form\"><input type=\"checkbox\" checked=\"\" name=\"close\" lay-skin=\"switch\" lay-filter=\"switchTest\" lay-text=\"ON|OFF\"/><div class=\"layui-unselect layui-form-switch layui-form-onswitch\" lay-skin=\"_switch\"><em>ON</em><i></i></div></div></td><td><span class=\"mr30\"><em href=\"#\" class=\"edit cp\" id=\"editclass\">编辑</em></span><a href=\"#\" class=\"del\">删除</a></td></tr>";
var rowTr = '<tr><td>#{id}</td><td>#{name}</td><td>#{remark}</td><td><div class="layui-form"><input type="checkbox" checked=#{enabled} name="open" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF"></div></td><td><span class="mr30"><em href="#" class="edit cp" onclick="editRecharge(\'#{id}\')">编辑</em></span><a href="#" class="del" onclick="dropRecharge(\'#{id}\')">删除</a></td></tr>';
$(function() {
    searchRechargeList();
});

function searchRechargeList() {
    $.get(
        "/config/recharge/list",
        function(data) {
            $("#dataBody").empty();
            var list = data.list;
            for(var d in list) {
                var row = rowTr.replace(/#{id}/g, list[d].id).replace("#{name}", list[d].name)
                .replace("#{remark}", list[d].remark).replace("#{enabled}", list[d].enabled === 1 ? "true" : "false");
                $("#dataBody").append(row);
            }
            $("#totalTxt").text("共 " + data.total + " 条");
        }
    );
}

function saveNewRechargeType() {
    var newName = $("#newName").val();
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
                window.location.href = "/config/recharge.html";
            }
            else {
                alert(data.errMsg);
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
                    window.location.href = "/config/recharge.html";
                }
            );
            layer.closeAll();
        },
        btn2: function() {
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
                alert(data.errMsg);
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
                alert(data.errMsg);
            }
            else {
                window.location.href = "/config/recharge.html";
            }
        }
    });
}
