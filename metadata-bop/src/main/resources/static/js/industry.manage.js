//<![CDATA[
$(".add-child").on('click', function() {
    var parentId = $(this).attr("data-id");
    $("#l2-parent-id").val(parentId);
    $("#l2-code").val("");
    $("#l2-name").val("");
    layer.open({
        title: false,
        type: 1,
        content: $('#child-industry'),
        area: ['500px'],
        shadeClose: true
    });
});
$(".edit-industry").on('click', function() {
    var id = $(this).attr("data-id");
    $.get(
        "/config/industryInfo",
        {"id": id},
        function(data) {
            if(data != null) {
                $("#edit-id").val(data.id);
                $("#edit-code").val(data.code);
                $("#edit-name").val(data.name);
                layer.open({
                    title: false,
                    type: 1,
                    content: $('#edit-industry'),
                    area: ['500px'],
                    shadeClose: true
                });
            }
        }
    );
});

function checkNewCode() {
    var code = $("#new-code").val();
    if(code !== "") {
        $.get(
            "/config/industry/checkCode",
            {"code": code},
            function(data) {
                if(data.exist === 1) {
                    $("#tip-new-code").text("该行业编号已被占用");
                }
                else {
                    $("#tip-new-code").text("");
                }
            }
        );
    }
    else {
        $("#tip-new-code").text("");
    }
}

function addIndustry(type) {
    if(type !== 1 && type !== 2) {
        return;
    }
    var id, code, name;
    if(type === 1) {
        code = $("#new-code").val();
        name = $("#new-name").val();
        if(code === "") {
            $("#tip-new-code").text("行业编号不能为空");
            return;
        }
        if(name === "") {
            $("#tip-new-name").text("行业名称不能为空");
            return;
        }
    }
    else {
        id = $("#l2-parent-id").val();
        code = $("#l2-code").val();
        name = $("#l2-name").val();
        if(code === "") {
            $("#tip-l2-code").text("行业编号不能为空");
            return;
        }
        if(name === "") {
            $("#tip-l2-name").text("行业名称不能为空");
            return;
        }
    }
    saveIndustry(id, code, name);
}

function saveIndustry(id, code, name) {
    $.ajax({
        type: "POST",
        url: "/config/industry/addition",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"id": id, "code": code, "name": name}),
        success: function(data) {
            if(data.errCode !== "000000") {
                alert(data.errMsg);
            }
            else {
                window.location.href = "/config/industry.html";
            }
        }
    });
}


//添加主行业和子行业
$(document).on("click", ".js_addIndustry", function() {
    var $frm = $("#industryForm");
    $.ajax({
        data: $frm.serialize(),
        dataType: "json",
        type: "post",
        url: "/industry/add",
        success: function(data) {
            alert("添加成功");
            window.location.href = "/industry/manage";
        },
        error: function(data) {
            alert("添加失败");
        }
    })
});
$(document).on("click", ".js_addChildIndustry", function() {
    var parId = $(this).data("name");
    var $frm = $("#childIndustry");
    $.ajax({
        data: $frm.serialize(),
        dataType: "json",
        type: "post",
        url: "/industry/add",
        success: function(data) {
            alert("添加成功");
            window.location.href = "/industry/manage";
        },
        error: function(data) {
            alert("添加失败");
        }
    })
})

function editIndustry() {
    var id = $("#edit-id").val();
    var code = $("#edit-code").val();
    var name = $("#edit-name").val();
    $.ajax({
        type: "POST",
        url: "/config/industry/modification",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"id": id, "code": code, "name": name}),
        success: function(data) {
            if(data.errCode !== "000000") {
                alert(data.errMsg);
            }
            window.location.href = "/config/industry.html";
        }
    });
}
//]]>

