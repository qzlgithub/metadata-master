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
                layer.msg(data.errMsg);
            }
            else {
                window.location.href = "/product/index.html";
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
    var reg1 = new RegExp("^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"); // 匹配任意浮点型数据
    var reg2 = new RegExp("^[1-9]\\d*$"); // 匹配任意正整数
    if(unitPrice !== "") {
        if(reg1.test(unitPrice) || reg2.test(unitPrice)) {
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
