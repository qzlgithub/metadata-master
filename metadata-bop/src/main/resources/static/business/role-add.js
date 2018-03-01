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
            content: '<img src="/static/build/images/pay.png" />',
            area: ['500px', '250px'],
            shadeClose: true
        });
    });
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
        type: "PUT",
        url: "/account/role/addition",
        contentType: "application/json",
        data: JSON.stringify({
            "name": $("#name").val(),
            "privilege": privilege
        }),
        success: function(res) {
            if(res.code !== '000000') {
                layer.msg("添加失败:" + res.message, {
                    time: 2000
                });
            }
            else {
                layer.msg("添加成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/setting/role.html";
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
            "/account/role/verification",
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