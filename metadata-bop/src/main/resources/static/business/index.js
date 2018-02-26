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
    $.ajax({
        type: "POST",
        url: "/user/login",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "username": $("#username").val(),
            "password": MD5($("#password").val()),
            "imageCode": $("#code").val()
        }),
        success: function(data) {
            if(data.errCode !== '000000') {
                refresh_code();
                $("#code").val("");
                layer.msg(data.errMsg);
            }
            else {
                window.location.href = "/setting/user.html";
            }
        }
    });
}