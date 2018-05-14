var form, message, table, main_table, layer;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message', 'layer'], function() {
    var app = layui.app;
    app.set({type: 'iframe'}).init();
    layer = layui.layer;
    message = layui.message;
    form = layui.form;
    table = layui.table;
});

function showNotice() {
    $("#remark").val("");
    layer.open({
        title: false,
        type: 1,
        content: $('#notice-warp'),
        area: ['500px'],
        shadeClose: true
    });
}

function showRemark() {
    layer.open({
        title: false,
        type: 1,
        content: $('#remark-div'),
        area: ['500px'],
        shadeClose: true
    });
}

var isSubmit = false;

function saveRemark() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    var remark = $.trim($("#remark").val());
    if(remark == '') {
        layer.msg("备注不能为空！", {
            time: 2000
        });
        isSubmit = false;
        return;
    }
    var pacifyId = $("#pacifyId").val();
    $.ajax({
        type: "POST",
        url: "/pacify/dispose",
        data: {"id": pacifyId, "remark": remark},
        success: function(res) {
            if(res.code === '000000') {
                layer.msg("保存成功", {
                    time: 1000
                }, function() {
                    layer.closeAll();
                    window.location.reload();
                });
            }
            else {
                layer.msg("保存失败", {
                    time: 2000
                }, function() {
                    layer.closeAll();
                });
                isSubmit = false;
            }
        }
    });
}