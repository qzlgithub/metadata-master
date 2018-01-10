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
                layer.msg(data.errMsg);
            }
            else {
                window.location.href = "/product/index.html";
            }
        }
    });
}