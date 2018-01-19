$(function() {
    resetPrivilege();
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
        "/role/privilege",
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
        url: "/manager/addition",
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
        success: function(data) {
            if(data.errCode !== "000000") {
                layer.msg("保存失败:" + data.errMsg, {
                    time: 2000
                });
            }
            else {
                layer.msg("保存成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/manager/index.html";
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