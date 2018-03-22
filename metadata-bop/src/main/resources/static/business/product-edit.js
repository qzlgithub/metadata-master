var editor;
KindEditor.ready(function(K) {
    editor = K.create('#product-txt', {
        minWidth: 800,
        minHeight: 350,
        items: ['source', 'fullscreen', 'preview', 'print', '|', 'undo', 'redo', '|',
            'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', '|',
            'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'lineheight', '|',
            'formatblock', 'fontname', 'fontsize', '|',
            'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough', '|',
            'table', 'hr', 'emoticons', 'link'],
        uploadJson: '../jsp/upload_json.jsp',
        fileManagerJson: '../jsp/file_manager_json.jsp',
        allowFileManager: true,
        afterCreate: function() {
            var self = this;
            K.ctrl(document, 13, function() {
                self.sync();
                document.forms['example'].submit();
            });
            K.ctrl(self.edit.doc, 13, function() {
                self.sync();
                document.forms['example'].submit();
            });
        }
    });
    prettyPrint();
});
var message;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
});
var isSubmit = false;

function editProduct() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    if(!checkDataValid("#data-div-id")) {
        isSubmit = false;
        return;
    }
    $.ajax({
        type: "POST",
        url: "/product",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "id": $("#product-id").val(),
            "code": $("#product-code").val(),
            "name": $("#product-name").val(),
            "costAmt": $("#product-cost").val(),
            "enabled": $("input[name='product-enabled']:checked").val(),
            "remark": $("#product-remark").val(),
            "content": editor.html()
        }),
        success: function(res) {
            if(res.code !== '000000') {
                layer.msg(res.message, {time: 2000});
                isSubmit = false;
            }
            else {
                layer.msg("修改成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/setting/product.html";
                });
            }
        }
    });
}