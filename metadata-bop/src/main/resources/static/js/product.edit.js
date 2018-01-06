function editProduct() {
    $.ajax({
        type: "POST",
        url: "/product/modification",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "typeId": $("#selectId").val(),
            "id": $("#productId").val(),
            "code": $("#codeId").val(),
            "name": $("#nameId").val(),
            "costAmt": $("#costAmtId").val(),
            "enabled": $("input[name='prodEnabled']:checked").val(),
            "remark": $("#remarkId").val(),
            "content": $("#contentId").val()
        }),
        success: function(data) {
            // alert(data.name);
            if(data.errCode !== '000000') {
                alert(data.errMsg);
            }
            else {
                window.location.href = "/product/index.html";
            }
        }
    });
}