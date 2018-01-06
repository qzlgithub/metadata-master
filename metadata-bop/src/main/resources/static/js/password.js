$("#changePwd").click(function() {
    layer.open({
        title: false,
        type: 1,
        content: $('#editpwd-manage'),
        area: ['500px'],
        shadeClose: true
    });
    $("#layui-layer1").show();
    $("#layui-layer-shade1").show();
});
$("#savePwd").click(function() {
    var newPwd = $("#newPwd").val();
    var repPwd = $("#repeatPwd").val();
    if(newPwd == null || newPwd.length === 0 ) {
        layer.msg("新密码不能为空");
        return;
    }
    if(newPwd !== repPwd) {
        //layer.msg("新密码不一致");
        layer.msg('大部分参数都是可以公用的<br/>合理搭配，展示不一样的风格', {
            btn: ['明白了', '知道了'],
            yes: function(){
                $(that).click();
            },
            btn2: function(){
                layer.closeAll();
            }
        });
        layer.open({
            type: 2, //此处以iframe举例
            title: '提示',
            area: ['290px', '160px'],
            shade: 0,
            maxmin: true,
            layer.msg('大部分参数都是可以公用的<br>合理搭配，展示不一样的风格', {
                btn: ['明白了', '知道了'],
                yes: function(){
                    $(that).click();
                },
                btn2: function(){
                    layer.closeAll();
                }
            });
        });
        return;
    }
    $.ajax({
        type: "POST",
        url: "/changePwd",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "oldPwd": MD5($("#oldPwd").val()),
            "newPwd": MD5(newPwd)
        }),
        success: function(data) {
            if(data.errCode !== "000000") {
                layer.msg(data.errMsg);
            }
            else {
                layer.closeAll();
                //layer.msg("密码修改成功！");
                layer.msg('密码修改成功', {
                    time: 1000, //1s后自动关闭
                });

            }
        }
    });
});