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
    //app.set({
    //    type: 'iframe'
    //}).init();
    $('#appkid').on('click', function() {
        $("#mode_app_id_2").hide();
        $("#mode_app_id_1").show();
        layer.open({
            title: false,
            type: 1,
            content: $('#appkid-modal'),
            area: ['600px'],
            shadeClose: true
        });
    });
    $("#cp-app-id").on('click', function() {
    });
    $("#ref-app-key").click(function() {
        var uuid = Math.uuidFast();
        var uuid = uuid.toLocaleLowerCase();
        $("#app-key").val(uuid.replace(/-/g, ''));
    });
    $('#next-step').on('click', function() {
        var password = $("#password").val();
        if(password === '') {
            layer.msg("请输入您的登陆密码已完成验证！");
            return;
        }
        var productId = $(this).attr("obj-id");
        $.get(
            "/client/user/credential",
            {"productId": productId, "password": MD5(password)},
            function(res) {
                if(res.code === '000000') {
                    var data = res.data;
                    $("#app-id").text(data.appId);
                    $("#app-key").val(data.appKey);
                    $("#req-host").val(data.reqHost);
                    $("#mode_app_id_1").hide();
                    $("#mode_app_id_2").show();
                }
                else {
                    layer.msg("认证失败：" + res.message);
                }
            }
        );
    });
});
$("#save-credential").click(function() {
    var appKey = $("#app-key").val();
    var reqHost = $("#req-host").val();
    if(appKey === '') {
        layer.msg("请先生成产品密钥（AppKey）");
        return;
    }
    if(reqHost === '') {
        layer.msg("请配置允许请求接口的IP地址");
        return;
    }
    var productId = $(this).attr("obj-id");
    $.ajax({
        type: "POST",
        url: "/client/user/credential",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"productId": productId, "appKey": appKey, "reqHost": reqHost}),
        success: function(res) {
            if(res.code === '000000') {
                layer.msg("添加成功", {time: 2000}, function() {
                    layer.closeAll();
                });
            }
            else {
                layer.msg("添加失败：" + res.message);
            }
        }
    });
});