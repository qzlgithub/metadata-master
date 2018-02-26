$(function() {
    $(".st_tree").SimpleTree({
        /* 可无视代码部分*/
        click: function(a) {
            if(!$(a).attr("hasChild")) {
                alert($(a).attr("ref"));
            }
        }
    });
});
var message;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message', 'form'], function() {
    var app = layui.app;
    var form = layui.form,
        $ = layui.jquery,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    $('.edit-manage').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#edit-manage'),
            area: ['500px'],
            shadeClose: true
        });
    });
    form.on('switch(switchTest)', function(data) {
        //subs handler
        var checked = this.checked;
        console.log("ckd: " + checked);
        var self = $(this).data('self');
        $(".switch-subs-" + self).each(function(index, sub) {
            $(sub).prop("checked", checked);
            var subself = $(sub).data('self');
            $(".switch-subs-" + subself).prop("checked", checked);
        });
        //parent handler
        var parent = $(this).data('parent');
        if(parent) {
            if(checked) {
                $(".switch-" + parent).prop("checked", checked);
                var superParent = $(".switch-" + parent).data('parent');
                if(superParent) {
                    $(".switch-" + superParent).prop("checked", checked);
                }
            }
            else {
                var flag = true;
                $(".switch-subs-" + parent).each(function(index, sub) {
                    if(checked != this.checked) {
                        flag = false;
                        return;
                    }
                });
                if(flag) {
                    $(".switch-" + parent).prop("checked", checked);
                }
            }
        }
        form.render('checkbox');
    });
});
$(".edit-manage").on('click', function() {
    var id = $(this).attr("data-id");
    var obj = $("#menu-id-"+id);
    var name = $(obj).find(".menu-name-class").text();
    $("#editId").val(id);
    $("#editNameId").val(name);
});

function editColumn() {
    var id = $("#editId").val();
    var name = $("#editNameId").val();
    $.ajax({
        type: "POST",
        url: "/setting/menu",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"id": id, "name": name}),
        success: function(data) {
            if(data.errCode !== "000000") {
                layer.msg("修改失败:" + data.errMsg, {
                    time: 2000
                });
            }
            else {
                window.location.href = "/setting/menu.html";
                /*    layer.msg("修改成功", {
                        time: 2000
                    }, function() {

                    });*/
            }
        }
    });
}

function changeStatus(modules, status) {
    if(modules.length !== 0 && (status === 0 || status === 1)) {
        $.ajax({
            type: "post",
            url: "/setting/menu/status",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({"module": modules, "status": status}),
            success: function(res) {
            }
        })
    }
}

