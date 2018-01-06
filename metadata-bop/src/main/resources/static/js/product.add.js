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
    var categoryId = $("#selectId").val();
    alert(categoryId);
}