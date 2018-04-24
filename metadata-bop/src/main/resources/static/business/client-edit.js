var message, form;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    form = layui.form;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({type: 'iframe'}).init();
    form.on('select(parent-industry)', function(data) {
        var industryOcx = $("#industry");
        $.get(
            "/m/dict/sub-industry",
            {"parentId": data.value},
            function(data) {
                industryOcx.empty();
                for(var d in data) {
                    industryOcx.append(new Option(data[d].name, data[d].id));
                }
                form.render('select');
            }
        );
    });
    form.on("checkbox(general)", function() {
        var id = $(this).data("id");
        var obj = {};
        var isNew = true;
        for(var t in edit_obj) {
            if(id === edit_obj[t].id) {
                isNew = false;
                edit_obj[t].name = $("#name-" + id).text();
                edit_obj[t].position = $("#position-" + id).text();
                edit_obj[t].phone = $("#phone-" + id).text();
                edit_obj[t].email = $("#email-" + id).text();
                edit_obj[t].general = $("#general-" + id).prop("checked") ? 1 : 0;
                break;
            }
        }
        if(isNew) {
            obj.id = id;
            obj.isAdd = false;
            obj.name = $("#name-" + id).text();
            obj.position = $("#position-" + id).text();
            obj.phone = $("#phone-" + id).text();
            obj.email = $("#email-" + id).text();
            obj.general = $("#general-" + id).prop("checked") ? 1 : 0;
            edit_obj.push(obj);
        }
        form.render();
    });
    $("#add-contact").on("click", function() {
        $(".tip").text("");
        $("#add-id").val("");
        $("#add-name").val("");
        $("#add-position").val("");
        $("#add-phone").val("");
        $("#add-email").val("");
        $("#add-general").prop("checked", false);
        layer.open({
            title: false,
            type: 1,
            content: $('#div-contact'),
            area: ['700px'],
            shadeClose: true
        });
    });
});

function numberOnly() {
    if(!(event.keyCode === 46) && !(event.keyCode === 8) && !(event.keyCode === 37) && !(event.keyCode === 39)) {
        if(!((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105))) {
            event.returnValue = false;
        }
    }
}

var del_obj = [], edit_obj = [], contact = $("#contact-list");
var contact_tr = "<tr id=\"#{id}\">" +
    "<td id=\"name-#{id}\">#{name}</td>" +
    "<td id=\"position-#{id}\">#{position}</td>" +
    "<td id=\"phone-#{id}\">#{phone}</td>" +
    "<td id=\"email-#{id}\">#{email}</td>" +
    "<td><span class=\"mr30\"><a href=\"#\" class=\"edit-contact\" data-id=\"#{id}\">编辑</a></span>" +
    "<span class=\"mr30\"><a href=\"#\" class=\"del-contact\" data-id=\"#{id}\">删除</a></span>";
/*"<span class=\"layui-form\"><input type=\"checkbox\" title=\"常用\" checked=\"\" id=\"isGeneral-#{id}\"/></span>" +
"</td></tr>";*/
contact.on("click", ".edit-contact", function() {
    var id = $(this).data("id");
    $(".tip").text("");
    $("#add-id").val(id);
    $("#add-name").val($("#name-" + id).text());
    $("#add-position").val($("#position-" + id).text());
    $("#add-phone").val($("#phone-" + id).text());
    $("#add-email").val($("#email-" + id).text());
    $("#add-general").prop("checked", $("#general-" + id).prop("checked"));
    layer.open({
        title: false,
        type: 1,
        content: $('#div-contact'),
        area: ['700px'],
        shadeClose: true
    });
});
contact.on("click", ".del-contact", function() {
    var id = $(this).data("id");
    del_obj.push(id);
    $("#" + id).remove();
});
$("#save-contact").click(function() {
    if(!checkDataValid("#div-contact")) {
        return;
    }
    var name = $("#add-name").val();
    var position = $("#add-position").val();
    var phone = $("#add-phone").val();
    if(name === '' || position === '' || phone === '') {
        layer.msg("关键字段不能为空！", {
            time: 2000
        });
        return;
    }
    var id = $("#add-id").val();
    var obj = {};
    if(id === "") {
        obj.id = Math.uuidFast();
        obj.isAdd = true;
        obj.name = $("#add-name").val();
        obj.position = $("#add-position").val();
        obj.phone = $("#add-phone").val();
        obj.email = $("#add-email").val();
        obj.general = $("#add-general").prop("checked") ? 1 : 0;
        edit_obj.push(obj);
        var tr = contact_tr;
        if(obj.general === 1) {
            tr = tr + "<span class=\"layui-form\"><input type=\"checkbox\" title=\"常用\" checked=\"\" id=\"general-#{id}\"/></span>"
        }
        else {
            tr = tr + "<span class=\"layui-form\"><input type=\"checkbox\" title=\"常用\" id=\"general-#{id}\"/></span>"
        }
        tr = tr + "</td></tr>";
        tr = tr.replace(/#{id}/g, obj.id).replace(/#{name}/g, obj.name).replace(/#{position}/g, obj.position)
        .replace(/#{phone}/g, obj.phone).replace(/#{email}/g, obj.email);
        contact.append(tr);
    }
    else {
        var isNew = true;
        for(var t in edit_obj) {
            if(id === edit_obj[t].id) {
                isNew = false;
                edit_obj[t].name = $("#add-name").val();
                edit_obj[t].position = $("#add-position").val();
                edit_obj[t].phone = $("#add-phone").val();
                edit_obj[t].email = $("#add-email").val();
                edit_obj[t].general = $("#add-general").prop("checked") ? 1 : 0;
                break;
            }
        }
        if(isNew) {
            obj.id = id;
            obj.isAdd = false;
            obj.name = $("#add-name").val();
            obj.position = $("#add-position").val();
            obj.phone = $("#add-phone").val();
            obj.email = $("#add-email").val();
            obj.general = $("#add-general").prop("checked") ? 1 : 0;
            edit_obj.push(obj);
        }
        $("#name-" + id).text($("#add-name").val());
        $("#position-" + id).text($("#add-position").val());
        $("#phone-" + id).text($("#add-phone").val());
        $("#email-" + id).text($("#add-email").val());
        if($("#add-general").prop("checked")) {
            $("#general-" + id).prop("checked", true);
        }
        else {
            $("#general-" + id).prop("checked", false);
        }
    }
    layer.closeAll();
    form.render();
});
var sc_str = "<tr><td>#{corpName}</td>" +
    "<td>#{license}</td>" +
    "<td>#{managerName}</td>" +
    "<td>#{registerDate}</td></tr>";
$("#sameCompany").click(function() {
    var corpName = $("#corpName").val();
    $("#thisCorpName").text(corpName);
    $.get(
        "/client/same",
        {
            "name": corpName.replace(/有限/g, "").replace(/公司/g, "").replace(/责任/g, "").replace(/股份/g, ""),
            "id": $("#clientId").val()
        },
        function(res) {
            $("#sameCompanyBody").empty();
            var list = res.list;
            for(var i in list) {
                var tr = sc_str.replace("#{corpName}", list[i].corpName).replace("#{license}", list[i].license)
                .replace("#{managerName}", list[i].managerName).replace("#{registerDate}", list[i].registerDate);
                $("#sameCompanyBody").append(tr);
            }
            $("#sameTotal").text("共" + res.total + "家");
            layer.open({
                title: false,
                type: 1,
                content: $('#check-company'),
                area: ['700px'],
                shadeClose: true
            });
        }
    );
});
var isSubmit = false;

function editClient() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    if(!checkDataValid("#data-div-id")) {
        isSubmit = false;
        return;
    }
    var accountTotalQty = $("#accountTotalQty").val();
    var numRegex = /^[0-9]*$/;
    if(!numRegex.test(accountTotalQty) || accountTotalQty < 0 || accountTotalQty > 100) {
        layer.msg("子账号个数取值范围：[0, 100]");
        return;
    }
    var update = [];
    for(var o in edit_obj) {
        var obj = {};
        if(!edit_obj[o].isAdd) {
            obj.id = edit_obj[o].id;
        }
        obj.name = edit_obj[o].name;
        obj.position = edit_obj[o].position;
        obj.phone = edit_obj[o].phone;
        obj.email = edit_obj[o].email;
        obj.general = edit_obj[o].general;
        update.push(obj);
    }
    $.ajax({
        type: "POST",
        url: "/client/modification",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "clientId": $("#clientId").val(),
            "corpName": $("#corpName").val(),
            "shortName": $("#shortName").val(),
            "license": $("#license").val(),
            "industryId": $("#industry").val(),
            "managerId": $("#managerId").val(),
            "accountTotalQty": accountTotalQty,
            "contactDel": del_obj,
            "contacts": update,
            "enabled": $("input[name='clientEnabled']:checked").val()
        }),
        success: function(res) {
            if(res.code !== '000000') {
                layer.msg("修改失败:" + res.message);
                isSubmit = false;
            }
            else {
                layer.msg("修改成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/client/index.html";
                });
            }
        }
    });
}