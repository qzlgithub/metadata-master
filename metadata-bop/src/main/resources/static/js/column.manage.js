$(".edit-manage").on('click', function() {
    var id = $(this).attr("data-id");
    $.get(
        "/config/columnInfo",
        {"id": id},
        function(data) {
            if(data != null) {
                $("#editId").val(data.id);
                $("#editNameId").val(data.name);
            }
        }
    );
});

function editColumn() {
    var id = $("#editId").val();
    var name = $("#editNameId").val();
    $.ajax({
        type: "POST",
        url: "/config/column/modification",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"id": id, "name": name}),
        success: function(data) {
            if(data.errCode !== "000000") {
                layer.msg("修改失败:" + data.errMsg, {
                    time: 2000
                });
            }
            else {
                window.location.href = "/privilege/index.html";
            /*    layer.msg("修改成功", {
                    time: 2000
                }, function() {

                });*/
            }
        }
    });

}


