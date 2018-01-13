function editProduct() {
    $.ajax({
        type: "POST",
        url: "/product/modification",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "id": $("#product-id").val(),
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
                layer.msg("修改成功", {
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
    var reg = new RegExp("^(0|[1-9][0-9]{0,9})(\\.[0-9]{1,2})?$"); // 匹配任意浮点型数据
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


