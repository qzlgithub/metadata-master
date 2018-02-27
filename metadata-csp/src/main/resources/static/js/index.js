var message,app,layer;
layui.config({
    base: '/static/build/js/'
}).use(['app', 'message'], function() {
    app = layui.app;
    layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    //app.set({
    //    type: 'iframe'
    //}).init();
});
$(document).keydown(function(e) {
    if(e.keyCode === 13) {
        userLogin();
    }
});
$("#user-login").click(function() {
    userLogin();
});
$("#dec-passcode").keyup(function() {
    $("#enc-passcode").val($("#dec-passcode").val());
});
$("#enc-passcode").keyup(function() {
    $("#dec-passcode").val($("#enc-passcode").val());
});
$("#hide-pass").click(function() {
    $("#pwd-show").hide();
    $("#pwd-hide").show();
});
$("#show-pass").click(function() {
    $("#pwd-hide").hide();
    $("#pwd-show").show();
});

/**
 * 刷新验证码图片
 */
function changeImage() {
    $.get(
        "/captcha",
        function(res) {
            if(res.code === '000000') {
                var data = res.dataMap;
                $("#captcha").attr("src", data.imageCaptcha);
            }
        }
    );
}

function userLogin() {
    var username = $("#username").val();
    var password = $("#enc-passcode").val();
    var code = $("#captcha-code").val();
    if(username === '') {
        layer.msg("请填写正确的用户名！");
        return;
    }
    if(password === '') {
        layer.msg("请填写您的密码！");
        return;
    }
    if(code === '') {
        layer.msg("请填写验证码！");
        return;
    }
    $.ajax({
        type: "post",
        url: "/client/user/login",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"username": username, "password": MD5(password), "code": code}),
        success: function(res) {
            if(res.code === '000000') {
                var data = res.dataMap;
                sessionStorage.setItem("user_name", data.name);
                sessionStorage.setItem("manager_qq", data.managerQq);
                sessionStorage.setItem("first_login", data.firstLogin);
                window.location.href = "/home.html";
            }
            else {
                $("#captcha-code").val("");
                changeImage();
                layer.msg("登陆失败，" + res.message);
            }
        }
    })
}