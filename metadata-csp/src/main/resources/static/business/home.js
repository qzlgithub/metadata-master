$(function() {
    var insufficientBalanceQty = $("#insufficientBalance").val();
    var nearlyExpireQty = $("#nearlyExpire").val();
    var inArrearQty = $("#inArrear").val();
    var expiredQty = $("#expired").val();
    if(insufficientBalanceQty > 0 || nearlyExpireQty > 0 || inArrearQty > 0 || expiredQty > 0) {
        var msg = [];
        if(insufficientBalanceQty > 0) {
            msg.push(insufficientBalanceQty + '个余额不足');
        }
        if(nearlyExpireQty > 0) {
            msg.push(nearlyExpireQty + '个即将过期');
        }
        if(inArrearQty > 0) {
            msg.push(inArrearQty + '个已欠费');
        }
        if(expiredQty > 0) {
            msg.push(expiredQty + '个已过期');
        }
        $("#remindOcx").text('您的服务有' + msg.join("、") + '，请及时处理~');
    }
});
var message;
layui.config({
    base: '../build/js/'
}).use(['form', 'app', 'message'], function() {
    var form = layui.form;
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    //app.set({type: 'iframe'}).init();
    $("#home-greeting").text(getGreeting());
    var first = localStorage.getItem("first_login");
    if(first === "1") {
        layer.open({
            title: false,
            type: 1,
            content: $('#init-password'),
            area: ['500px'],
            shadeClose: true
        });
    }
});
$("#init-org-enc-password").keyup(function() {
    $("#init-org-dec-password").val($("#init-org-enc-password").val());
});
$("#init-org-dec-password").keyup(function() {
    $("#init-org-enc-password").val($("#init-org-dec-password").val());
});
$("#init-new-enc-password").keyup(function() {
    $("#init-new-dec-password").val($("#init-new-enc-password").val());
});
$("#init-new-dec-password").keyup(function() {
    $("#init-new-enc-password").val($("#init-new-dec-password").val());
});
$("#init-repeat-enc-password").keyup(function() {
    $("#init-repeat-dec-password").val($("#init-repeat-enc-password").val());
});
$("#init-repeat-dec-password").keyup(function() {
    $("#init-repeat-enc-password").val($("#init-repeat-dec-password").val());
});
$("#init-org-show").click(function() {
    $("#init-org-enc-pwd").hide();
    $("#init-org-dec-pwd").show();
});
$("#init-org-hide").click(function() {
    $("#init-org-enc-pwd").show();
    $("#init-org-dec-pwd").hide();
});
$("#init-new-show").click(function() {
    $("#init-new-enc-pwd").hide();
    $("#init-new-dec-pwd").show();
});
$("#init-new-hide").click(function() {
    $("#init-new-enc-pwd").show();
    $("#init-new-dec-pwd").hide();
});
$("#init-repeat-show").click(function() {
    $("#init-repeat-enc-pwd").hide();
    $("#init-repeat-dec-pwd").show();
});
$("#init-repeat-hide").click(function() {
    $("#init-repeat-enc-pwd").show();
    $("#init-repeat-dec-pwd").hide();
});
$("#chg-init-pwd").click(function() {
    var orgPwd = $("#init-org-enc-password").val();
    var newPwd = $("#init-new-enc-password").val();
    var repeatPwd = $("#init-repeat-enc-password").val();
    if(orgPwd === '') {
        layer.msg("旧密码不能为空！");
        return;
    }
    if(newPwd === '') {
        layer.msg("新密码不能为空！");
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
