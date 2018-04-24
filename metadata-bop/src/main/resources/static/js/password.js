$("#changePwd").click(function() {
    layer.open({
        title: false,
        type: 1,
        content: $('#editpwd-manage'),
        area: ['500px'],
        shadeClose: true
    });
    //$("#layui-layer1").show();
    //$("#layui-layer-shade1").show();
});
function isPasswordValidate(obj) {
    if(obj == null || (obj.length < 6 || obj.length > 20)) {
        return false;
    }
    var reg1 = new RegExp(/^[0-9A-Za-z]+$/);
    if (!reg1.test(obj)) {
        return false;
    }
    var reg = new RegExp(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/);
    if (reg.test(obj)) {
        return true;
    } else {
        return false;
    }
}
$("#savePwd").click(function() {
    var oldPwd = $("#oldPwd").val();
    var newPwd = $("#newPwd").val();
    var repPwd = $("#repeatPwd").val();
    if(newPwd == null || newPwd.length === 0 ) {
        layer.msg("新密码不能为空");
        return;
    }else{
        if(newPwd !== repPwd) {
            layer.msg("新密码不一致");
            return;
        }else {
            if(oldPwd === newPwd){
                layer.msg("新密码不能与旧密码相同");
                return;
            }else{
                if (!isPasswordValidate(newPwd)) {
                    layer.msg("密码格式不匹配，必须6-20位字母数字组成！", {
                        time: 2000
                    });
                    return;
                }
                $.ajax({
                    type: "POST",
                    url: "/user/password",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify({
                        "oldPwd": MD5($("#oldPwd").val()),
                        "newPwd": MD5(newPwd)
                    }),
                    success: function(res) {
                        if(res.code !== "000000") {
                            layer.msg("密码修改失败:" + res.message);
                        }
                        else {
                            layer.closeAll();
                            layer.msg("密码修改成功", {
                                time: 2000
                            });

                        }
                    }
                });
            }
        }
    }
});