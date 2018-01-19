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
        url: "/role/addition",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "name": $("#name").val(),
            "privilege": privilege
        }),
        success: function(data) {
            if(data.errCode != '000000') {
                layer.msg("添加失败:" + data.errMsg, {
                    time: 2000
                });
            }
            else {
                layer.msg("添加成功", {
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

function checkName() {
    var name = $("#name").val();
    var tip = $("#nameTip");
    if(name !== "") {
        $.get(
            "/role/check",
            {"name": name},
            function(data) {
                if(data.exist === 1) {
                    tip.text("该分组名已被存在！");
                    tip.show();
                    return false;
                }
                else {
                    tip.text("该分组名可用！");
                    tip.show();
                }
            }
        );
    }
    else {
        tip.text("分组名不能为空");
        tip.show();
        return false;
    }
}