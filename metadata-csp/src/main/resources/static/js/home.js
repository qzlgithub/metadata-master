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
    app.set({type: 'iframe'}).init();
    $('#chgPwd').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#edit-pwd'),
            area: ['500px'],
            shadeClose: true
        });
    });
    $("#home-greeting").text(getGreeting());
    var first = sessionStorage.getItem("first_login");
    if(first !== "1") {
        layer.open({
            title: false,
            type: 1,
            content: $('#edit-pwd'),
            area: ['500px'],
            shadeClose: true
        });
    }
});
$(document).ready(function() {
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
});