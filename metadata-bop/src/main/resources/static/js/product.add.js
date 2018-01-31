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
            'image', 'table', 'hr', 'emoticons', 'link'],
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
    $('#pay').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: '<img src="../../static/build/images/pay.png" />',
            area: ['500px', '250px'],
            shadeClose: true
        });
    });
});
function addProduct() {
    $.ajax({
        type: "POST",
        url: "/product/addition",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "productType": $("#product-type").val(),
            "code": $("#product-code").val(),
            "name": $("#product-name").val(),
            "costAmt": $("#product-cost").val(),
            "enabled": $("input[name='product-enabled']:checked").val(),
            "remark": $("#product-remark").val(),
            "content": editor.html()
        }),
        success: function(data) {
            if(data.errCode !== '000000') {
                layer.msg(data.errMsg, {time: 2000});
            }
            else {
                layer.msg("添加成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/product/index.html";
                });

            }
        }
    });
}

function checkProdNo() {
    var prodNoVal = $("#product-code").val();
    if(prodNoVal === null || prodNoVal == '') {
        $("#prodNoTip").text("产品编号不能为空");
        $("#prodNoTip").show();
    }
    else {
        $("#prodNoTip").text("");
        $("#prodNoTip").hide();
    }
}

function checkProdName() {
    var prodNoVal = $("#product-name").val();
    if(prodNoVal === null || prodNoVal == '') {
        $("#prodNameTip").text("产品名不能为空");
        $("#prodNameTip").show();
    }
    else {
        $("#prodNameTip").text("");
        $("#prodNameTip").hide();
    }
}

function checkCostAmt() {
    var unitPrice = $("#product-cost").val();
    var reg = new RegExp("^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$");
    if(unitPrice !== "") {
        if(reg.test(unitPrice)) {
            $("#costAmtTip").text("");
            $("#costAmtTip").hide();
        }
        else {
            $("#costAmtTip").text("单价格式错误！");
            $("#costAmtTip").show();
        }
    }
    else {
        $("#costAmtTip").text("请填写单价！");
        $("#costAmtTip").show();
    }
}
