var layer;
layui.use(['layer'], function() {
    layer = layui.layer;
});
$(document).keydown(function(e) {
    if(e.keyCode === 13) {
        login();
    }
});
$("#captchaImg").click(function() {
    refresh_code();
});
$("#loginBt").click(function() {
    login();
});

function refresh_code() {
    $.ajax({
        url: "/m/captcha/img",
        success: function(e) {
            $("#captchaImg").attr("src", e.image);
        }
    });
}

function login() {
    var username = $.trim($("#username").val());
    if(username == "") {
        layer.msg("用户名不能为空！");
        return;
    }
    var password = $("#password").val();
    if(password == "") {
        layer.msg("密码不能为空！");
        return;
    }
    var imageCode = $.trim($("#code").val());
    if(imageCode == "") {
        layer.msg("验证码不能为空！");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/user/login",
        contentType: "application/json",
        data: JSON.stringify({
            "username": $.trim($("#username").val()),
            "password": MD5($("#password").val()),
            "imageCode": $.trim($("#code").val())
        }),
        success: function(res) {
            if(res.code !== '000000') {
                refresh_code();
                $("#code").val("");
                layer.msg(res.message);
            }
            else {
                var da = res.data;
                localStorage.setItem("user_name", da.name);
                window.location.href = "/enter.html";
            }
        }
    });
}