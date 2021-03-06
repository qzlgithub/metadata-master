var message, form;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message', 'form'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    form = layui.form;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    form.on('select(parent-industry)', function(data) {
        var industryOcx = $("#industryId");
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
    $('#checkcompany').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#check-company'),
            area: ['700px'],
            shadeClose: true
        });
    });
    $('#addcontacts').on('click', function() {
        $("#add-name").val("");
        $("#add-position").val("");
        $("#add-phone").val("");
        $("#add-email").val("");
        $("#add-chief").prop("checked", false);
        layer.open({
            title: false,
            type: 1,
            content: $('#addcontacts-modal'),
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

var sc_str = "<tr><td>#{corpName}</td>" +
    "<td>#{license}</td>" +
    "<td>#{managerName}</td>" +
    "<td>#{registerDate}</td></tr>";
var contact_base_tr = "<tr id=\"#{id}\">" +
    "<td id=\"name-#{id}\">#{name}</td>" +
    "<td id=\"position-#{id}\">#{position}</td>" +
    "<td id=\"phone-#{id}\">#{phone}</td>" +
    "<td id=\"email-#{id}\">#{email}</td>" +
    "<td><span class=\"mr30\"><a href=\"#\" class=\"edit-contact\" data-id=\"#{id}\">编辑</a></span>" +
    "<span class=\"mr30\"><a href=\"#\" class=\"del-contact\" data-id=\"#{id}\">删除</a></span>";
var contacts = [];
$("#add_contact").click(function() {
    if(!checkDataValid("#addcontacts-modal")) {
        return;
    }
    var contact = {};
    contact.id = Math.uuidFast();
    contact.name = $("#add-name").val();
    contact.position = $("#add-position").val();
    contact.phone = $("#add-phone").val();
    contact.email = $("#add-email").val();
    contact.general = $("#add-chief").prop("checked") ? 1 : 0;
    contacts.push(contact);
    var tr = contact_base_tr;
    if(contact.general) {
        tr = tr + "<span class=\"layui-form\"><input type=\"checkbox\" title=\"常用\" checked=\"\" id=\"general-#{id}\"/></span>";
    }
    else {
        tr = tr + "<span class=\"layui-form\"><input type=\"checkbox\" title=\"常用\" id=\"general-#{id}\"/></span>";
    }
    tr = tr + "</td></tr>";
    tr = tr.replace(/#{name}/g, contact.name).replace(/#{position}/g, contact.position)
    .replace(/#{phone}/g, contact.phone).replace(/#{email}/g, contact.email).replace(/#{id}/g, contact.id);
    $("#contact-list").append(tr);
    layer.closeAll();
    form.render();
});
$("#contact-list").on("click", ".edit-contact", function() {
    var id = $(this).data("id");
    $("#add-name").val($("#name-" + id).text());
    $("#add-position").val($("#position-" + id).text());
    $("#add-phone").val($("#phone-" + id).text());
    $("#add-email").val($("#email-" + id).text());
    $("#add-chief").prop("checked", $("#general-" + id).prop("checked"));
    form.render();
    layer.open({
        title: false,
        type: 1,
        content: $('#addcontacts-modal'),
        area: ['700px'],
        shadeClose: true
    });
});
$("#contact-list").on("click", ".del-contact", function() {
    var id = $(this).data("id");
    for(var o in contacts) {
        if(contacts[o].id === id) {
            contacts[o].deleted = true;
        }
    }
    $("#" + id).remove();
});
$("#sameCompany").click(function() {
    var corpName = $("#corpName").val();
    $("#thisCorpName").text(corpName);
    $.get(
        "/client/same",
        {
            "name": corpName.replace(/有限/g, "").replace(/公司/g, "").replace(/责任/g, "").replace(/股份/g, "")
        },
        function(res) {
            $("#sameCompanyBody").empty();
            var list = res.list;
            for(var i in list) {
                var tr = sc_str.replace("#{corpName}", list[i].corpName)
                .replace("#{license}", list[i].license).replace("#{managerName}", list[i].managerName)
                .replace("#{registerDate}", list[i].registerDate);
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

function createUser() {
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
    var username = $("#username").val();
    var password = MD5($("#password").text());
    var corpName = $("#corpName").val();
    var shortName = $("#shortName").val();
    var industryId = $("#industryId").val();
    var license = $("#license").val();
    var managerId = $("#managerId").val();
    var enabled = $("input[name='enabled']:checked").val();
    var contactList = [];
    for(var o in contacts) {
        if(!contacts[o].deleted) {
            contacts[o].id = null;
            contactList.push(contacts[o]);
        }
    }
    $.ajax({
        type: "put",
        url: "/client/addition",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "username": username,
            "password": password,
            "corpName": corpName,
            "shortName": shortName,
            "industryId": industryId,
            "contacts": contactList,
            "license": license,
            "accountTotalQty": accountTotalQty,
            "managerId": managerId,
            "enabled": enabled
        }),
        success: function(res) {
            if(res.code !== '000000') {
                layer.msg("添加失败:" + res.message, {
                    time: 2000
                });
                isSubmit = false;
            }
            else {
                layer.msg("添加成功!", {
                    time: 2000
                }, function() {
                    window.location.href = "/client/index.html";
                });
            }
        }
    });
}

function checkUsername() {
    $("#usernameTip").hide();
    if(!checkDataValid("#username-div-id")) {
        return;
    }
    var username = $("#username").val();
    if(username !== "") {
        $.get(
            "/client/check",
            {"username": username},
            function(data) {
                if(data.exist === 1) {
                    $("#usernameTip").text("该用户名已被注册！");
                    $("#usernameTip").show();
                }
                else {
                    $("#usernameTip").text("恭喜，该用户名可用！");
                    $("#usernameTip").show();
                }
            }
        );
    }
}
