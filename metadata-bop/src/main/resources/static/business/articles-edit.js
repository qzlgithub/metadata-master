var message;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message', 'laydate'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    var laydate = layui.laydate;
    laydate.render({
        elem: '#publishTime'
    });
});
var isSubmit = false;

function saveData() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    if(!checkDataValid("#data-div-id")) {
        isSubmit = false;
        return;
    }
    if(ue.getContent() === "") {
        isSubmit = false;
        layer.msg("内容不能为空！", {time: 2000});
        return;
    }
    var options = {
        url: '/articles/modification',
        success: showReply,
        clearForm: false,
        resetForm: false,
        type: 'post',
        timeout: 3000
    };
    $('#formData').ajaxSubmit(options);
}

function showReply(res) {
    if(res.code !== '000000') {
        layer.msg(res.message, {time: 2000});
        isSubmit = false;
    }
    else {
        layer.msg("保存成功", {
            time: 2000
        }, function() {
            window.location.href = "/setting/articles.html";
        });
    }
}