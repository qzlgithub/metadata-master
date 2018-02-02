var sc_str = "<tr><td>#{corpName}</td>" +
    "<td>#{name}</td>" +
    "<td>#{phone}</td>" +
    "<td>#{email}</td>" +
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
    var name = $("#add-name").val();
    var position = $("#add-position").val();
    var phone = $("#add-phone").val();
    if(name == '' || position == '' || phone == ''){
        layer.msg("关键字段不能为空！", {
            time: 2000
        });
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
        tr = tr + "<span class=\"layui-form\"><input type=\"checkbox\" title=\"常用联系人\" checked=\"\" id=\"general-#{id}\"/></span>";
    }
    else {
        tr = tr + "<span class=\"layui-form\"><input type=\"checkbox\" title=\"常用联系人\" id=\"general-#{id}\"/></span>";
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
        function(data) {
            $("#sameCompanyBody").empty();
            var list = data.list;
            for(var i in list) {
                var tr = sc_str.replace("#{corpName}", list[i].corpName).replace("#{name}", list[i].name)
                .replace("#{phone}", list[i].phone).replace("#{email}", list[i].email)
                .replace("#{registerDate}", list[i].registerDate);
                $("#sameCompanyBody").append(tr);
            }
            $("#sameTotal").text("共" + data.total + "家");
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

function getSubIndustry() {
    var parentId = $("#parentIndustryId").val();
    if(parentId !== "") {
        $.get(
            "/system/industry/childList",
            {"industryId": parentId},
            function(data) {
                $("#industryId").empty();
                for(var d in data) {
                    $("#industryId").append('<option value="' + data[d].id + '">' + data[d].name + '</option>');
                }
            }
        )
    }
    else {
        $("#industryId").empty();
    }
}

function createUser() {
    var username = $("#username").val();
    var password = MD5($("#password").text());
    var corpName = $("#corpName").val();
    var shortName = $("#shortName").val();
    var industryId = $("#industryId").val();
    var license = $("#license").val();
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
            "enabled": enabled
        }),
        success: function(data) {
            if(data.errCode !== '000000') {
                layer.msg("添加失败:" + data.errMsg, {
                    time: 2000
                });
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
                    $("#usernameTip").text("恭喜，该用户名可用！");//长度未限定
                    $("#usernameTip").show();
                }
            }
        );
    }
    else {
        $("#usernameTip").text("用户名不能为空");
        $("#usernameTip").show();
    }
}

function checkCorpName() {
    var corpName = $("#corpName").val();
    if(corpName == "") {
        $("#corpNameTip").text("公司名称不能为空！");
        $("#corpNameTip").show();
    }
    else {
        $("#corpNameTip").text("");//长度未限定：^[0-9a-zA-Z\u4e00-\u9fa5][a-zA-Z0-9\u4e00-\u9fa5]{2,32}$
        $("#corpNameTip").hide();
    }
}

function checkContact() {
    var contact = $("#name").val();
    if(contact == "") {
        $("#contactTip").text("联系人不能为空！");
        $("#contactTip").show();
    }
    else {
        $("#contactTip").text("");//长度未限定
        $("#contactTip").hide();
    }
}

function checkPhone() {
    var phone = $("#phone").val();
    var reg1 = new RegExp("^([0-9]{3,4}-)?[0-9]{7,8}$");//检测固话
    var reg2 = new RegExp("^1[34578]\\d{9}$");//检测移动电话
    if(phone !== "") {
        if(reg1.test(phone) || reg2.test(phone)) {
            $("#phoneTip").text("");
            $("#phoneTip").hide();
        }
        else {
            $("#phoneTip").text("联系电话格式错误！");
            $("#phoneTip").show();
        }
    }
    else {
        $("#phoneTip").text("联系电话不能为空！");
        $("#phoneTip").show();
    }
}

function checkEmail() {
    var email = $("#email").val();
    var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
    if(email !== "") {
        if(reg.test(email)) {
            $("#emailTip").text("");
            $("#emailTip").hide();
        }
        else {
            $("#emailTip").text("邮箱格式不正确！");
            $("#emailTip").show();
        }
    }
}

function checkLicense() {
    var email = $("#license").val();
    var reg = new RegExp("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");
    if(email !== "") {
        if(reg.test(email)) {
            $("#licenseTip").text("");
            $("#licenseTip").hide();
        }
        else {
            $("#licenseTip").text("社会信用代码格式不正确！");
            $("#licenseTip").show();
        }
    }
    else {
        $("#licenseTip").text("社会信用代码不能为空！");
        $("#licenseTip").show();
    }
}
