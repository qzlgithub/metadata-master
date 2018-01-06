$(function() {
    $.get("/config/productTypeList", function(data) {
        for(var d in data) {
            $("#selectId").append('<option value="' + data[d].id + '">' + data[d].typeName + '</option>');
        }
    });
});

function addProduct() {
    $.ajax({
        type: "POST",
        url: "/product/add",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "id": $("#productId").val(),
            "typeId": $("#selectId").val(),
            "code": $("#codeId").val(),
            "name": $("#nameId").val(),
            "costAmt": $("#costAmtId").val(),
            "enabled": $("input[name='prodEnabled']:checked").val(),
            "remark": $("#remarkId").val(),
            "content": $("#contentId").val()
        }),
        success: function(data) {
            //alert(data.name);
            if(data.errCode != '000000') {
                alert(data.errMsg);
            }
            else {
                window.location.href = "/product/index.html";
            }
        }
    });
}

function checkProdNo() {
    var prodNoVal = $("#codeId").val();
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
    var prodNoVal = $("#nameId").val();
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
    var reg=new RegExp("/^(([1-9][0-9]*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$/");
    var price=$("#costAmtId").val();
    if(!reg.test(price)) {
        $("#costAmtTip").text("价格不正确");
        $("#costAmtTip").show();
    }
    else {
        $("#costAmtTip").text("");
        $("#costAmtTip").hide();
    }

}
