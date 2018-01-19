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
$(function() {
    checkSubPrivAllChecked();
});

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

function saveRole() {
    var privilege = build_privilege();
    $.ajax({
        type: "POST",
        url: "/role/modification",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "id": $("#roleId").val(),
            "name": $("#roleName").val(),
            "privilege": privilege
        }),
        success: function(data) {
            if(data.errCode !== "000000") {
                layer.msg("修改失败:" + data.errMsg, {
                    time: 2000
                });
            }
            else {
                layer.msg("修改成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/role/index.html";
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