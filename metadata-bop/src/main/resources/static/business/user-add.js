var message;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message', 'form'], function() {
    var app = layui.app,
        $ = layui.jquery,
        form = layui.form,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    form.on('select(role-dict)', function(obj) {
        resetPrivilege();
    })
});
$(function() {
    checkSubPrivAllChecked();
});
$(".parent-privilege").click(function() {
    var pid = $(this).attr("id");
    if(!$(this).is(":checked")) {
        $("." + pid).prop("checked", false);
    }
    else {
        $("." + pid).prop("checked", true);
    }
});
$(".privilege").click(function() {
    var parentId = $(this).attr("p-id");
    var allChecked = true;
    $("." + parentId).each(function() {
        var checked = $(this).is(":checked");
        allChecked = allChecked && checked;
        if(!allChecked) {
            return false;
        }
    });
    $("#" + parentId).prop("checked", allChecked);
});

function resetPrivilege() {
    var groupId = $("#groupId").val();
    $.get(
        "/account/role/privilege",
        {"id": groupId},
        function(data) {
            $(".privilege").prop("checked", false);
            var list = data.data.privilegeList;
            for(var i in list) {
                $("#" + list[i]).prop("checked", true);
            }
            checkSubPrivAllChecked();
        }
    );
}

function checkSubPrivAllChecked() {
    $(".parent-privilege").each(function() {
        var id = $(this).attr("id");
        var allChecked = true;
        $("." + id).each(function() {
            var checked = $(this).is(":checked");
            allChecked = allChecked && checked;
        });
        $(this).prop("checked", allChecked);
    });
}

function saveManager() {
    var groupId = $("#groupId").val();
    var roleType = $("#roleCode").val();
    var username = $("#username").val();
    var password = $("#password").val();
    var name = $("#name").val();
    var phone = $("#phone").val();
    var qq = $("#qq").val();
    var enabled = $("input[name='enabled']:checked").val();
    var privilege = build_privilege();
    if(!checkDataValid("#userInfoDivId")) {
        return;
    }
    if(privilege.length == 0) {
        layer.msg("请至少选择一个权限！", {
            time: 2000
        });
        return;
    }
    var alarm = $('#alarm').is(':checked')?1:0;
    var pacify = $('#pacify').is(':checked')?1:0;
    $.ajax({
        type: "POST",
        url: "/account/addition",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "groupId": groupId,
            "roleType": roleType,
            "username": username,
            "password": MD5(password),
            "name": name,
            "phone": phone,
            "qq": qq,
            "enabled": enabled,
            "privilege": privilege,
            "alarm": alarm,
            "pacify": pacify
        }),
        success: function(res) {
            if(res.code !== "000000") {
                layer.msg("保存失败:" + res.message, {
                    time: 2000
                });
            }
            else {
                layer.msg("保存成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/setting/user.html";
                });
            }
        }
    });
}

function build_privilege() {
    var privileges = [];
    $(".privilege").each(function() {
        if($(this).is(":checked")) {
            privileges.push($(this).attr("id"));
        }
    });
    return privileges;
}