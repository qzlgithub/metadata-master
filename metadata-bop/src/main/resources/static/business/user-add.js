var message;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    $('#pay').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: '<img src="../../static/build/images/pay.png" />',
            area: ['500px', '250px'],
            shadeClose: true
        });
    });
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
        console.log(checked);
        allChecked = allChecked && checked;
        if(!allChecked) {
            return false;
        }
    });
    $("#" + parentId).prop("checked", allChecked);
});

function resetPrivilege() {
    var roleId = $("#roleId").val();
    $.get(
        "/account/role/privilege",
        {"roleId": roleId},
        function(data) {
            $(".privilege").prop("checked", false);
            for(var i in data) {
                $("#" + data[i]).prop("checked", true);
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
    var roleId = $("#roleId").val();
    var username = $("#username").val();
    var password = $("#password").val();
    var name = $("#name").val();
    var phone = $("#phone").val();
    var qq = $("#qq").val();
    var enabled = $("input[name='enabled']:checked").val();
    var privilege = build_privilege();
    console.log("roleId: " + roleId + "\nusername: " + username + "\npassword: " + password + "\nname: " + name
        + "\nphone: " + phone + "\nqq: " + qq + "\nenabled: " + enabled);
    $.ajax({
        type: "POST",
        url: "/account/addition",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "roleId": roleId,
            "username": username,
            "password": MD5(password),
            "name": name,
            "phone": phone,
            "qq": qq,
            "enabled": enabled,
            "privilege": privilege
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