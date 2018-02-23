var message;
layui.config({
    base: '../build/js/'
}).use(['app', 'message'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    //app.set({type: 'iframe'}).init();
    $("#home-greeting").text(getGreeting());
    var first = sessionStorage.getItem("first_login");
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
$("#chg-init-pwd").click(function() {
    var orgPwd = $("#init-org-enc-password").val();
    var newPwd = $("#init-new-enc-password").val();
    if(orgPwd === '' || newPwd === '') {
        layer.msg("请填写正确的密码！");
        return;
    }
    changePwd(orgPwd, newPwd);
});