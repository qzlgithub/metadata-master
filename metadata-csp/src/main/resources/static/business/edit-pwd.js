var form;
layui.use('form', function() {
    form = layui.form;
});
$("#li-security-id").addClass("active");
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
$("#repeat-enc-password").keyup(function() {
    $("#repeat-dec-password").val($("#repeat-enc-password").val());
});
$("#repeat-dec-password").keyup(function() {
    $("#repeat-enc-password").val($("#repeat-dec-password").val());
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
$("#repeat-show").click(function() {
    $("#repeat-enc-pwd").hide();
    $("#repeat-dec-pwd").show();
});
$("#repeat-hide").click(function() {
    $("#repeat-enc-pwd").show();
    $("#repeat-dec-pwd").hide();
});
$("#change-pwd").click(function() {
    var orgPwd = $("#org-enc-password").val();
    var newPwd = $("#new-enc-password").val();
    var repeatPwd = $("#repeat-enc-password").val();
    if(orgPwd === '') {
        layer.msg("旧密码不能为空！");
        return;
    }
    if(newPwd === '') {
        layer.msg("新密码不能为空！");
        return;
    }
    if(repeatPwd === '') {
        layer.msg("重复密码不能为空！");
        return;
    }
    if(newPwd !== repeatPwd) {
        layer.msg("新密码与重复密码不一致！");
        return;
    }
    changePwd(orgPwd, newPwd, repeatPwd);
});

function changePwd(orgPwd, newPwd, repeatPwd) {
    if(!isPassword(newPwd)) {
        layer.msg("密码格式不匹配，必须6-20位字母数字组成！", {
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
            "newPassword": MD5(newPwd),
            "repeatPassword": MD5(repeatPwd)
        }),
        success: function(res) {
            if(res.code !== '000000') {
                layer.msg("修改失败:" + res.message, {
                    time: 2000
                });
            }
            else {
                localStorage.setItem('first_login', '0');
                layer.msg("修改成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/login.html";
                });
            }
        }
    })
}