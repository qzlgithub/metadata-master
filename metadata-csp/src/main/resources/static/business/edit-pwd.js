var form;
layui.use('form', function() {
    form = layui.form;
});
$("#org-enc-password").keyup(function() {
    $("#org-dec-password").val($("#org-enc-password").val());
});
$("#org-dec-password").keyup(function() {
    $("#org-enc-password").val($("#org-dec-password").val());
});
$("#new-enc-password").keyup(function() {
    $("#new-dec-password").val($("#new-enc-password").val());
});
$("#new-dec-password").keyup(function() {
    $("#new-enc-password").val($("#new-dec-password").val());
});
$("#org-show").click(function() {
    $("#org-enc-pwd").hide();
    $("#org-dec-pwd").show();
});
$("#org-hide").click(function() {
    $("#org-enc-pwd").show();
    $("#org-dec-pwd").hide();
});
$("#new-show").click(function() {
    $("#new-enc-pwd").hide();
    $("#new-dec-pwd").show();
});
$("#new-hide").click(function() {
    $("#new-enc-pwd").show();
    $("#new-dec-pwd").hide();
});
$("#change-pwd").click(function() {
    var orgPwd = $("#org-enc-password").val();
    var newPwd = $("#new-enc-password").val();
    if(orgPwd === '') {
        layer.msg("旧密码不能为空！");
        return;
    }
    if(newPwd === '') {
        layer.msg("新密码不能为空！");
        return;
    }
    changePwd(orgPwd, newPwd);
});

function changePwd(orgPwd, newPwd) {
    var reg = /^[A-Za-z0-9]{6,20}$/;
    if(!reg.test(newPwd)) {
        layer.msg("密码格式不匹配，必须6-20位字母数字！", {
            time: 2000
        });
        return;
    }
    $.ajax({
        type: "POST",
        url: "/client/user/password",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "orgPassword": MD5(orgPwd),
            "newPassword": MD5(newPwd)
        }),
        success: function(res) {
            if(res.code !== '000000') {
                layer.msg("修改失败:" + res.message, {
                    time: 2000
                });
            }
            else {
                sessionStorage.setItem('first_login', '0');
                layer.msg("修改成功", {
                    time: 2000
                }, function() {
                    layer.closeAll();
                });
            }
        }
    })
}