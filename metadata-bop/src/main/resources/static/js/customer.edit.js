var sc_str = "<tr><td>#{corpName}</td>" +
    "<td>#{name}</td>" +
    "<td>#{phone}</td>" +
    "<td>#{email}</td>" +
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

function fetchIndustry() {
    $.get(
        "/config/industry",
        {"parentId": $("#parentIndustry").val()},
        function(data) {
            $("#industry").empty();
            for(var d in data) {
                $("#industry").append('<option value="' + data[d].id + '">' + data[d].name + '</option>');
            }
        }
    )
}

function editClient() {
    console.log("corpName: " + $("#corpName").val() + "\nshortName: " + $("#shortName").val() + "\nindustryId: "
        + $("#industry").val() + "\nname: " + $("#name").val() + "\nphone: " + $("#phone").val()
        + "\nemail: " + $("#email").val() + "\nlicense: " + $("#license").val()
        + "\nuserEnabled: " + $("input[name='clientEnabled']:checked").val()
        + "\naccountEnabled: " + $("input[name='accountEnabled']:checked").val());
    $.ajax({
        type: "POST",
        url: "/client/modification",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "clientId": $("#clientId").val(),
            "corpName": $("#corpName").val(),
            "shortName": $("#shortName").val(),
            "industryId": $("#industry").val(),
            "name": $("#name").val(),
            "phone": $("#phone").val(),
            "email": $("#email").val(),
            "license": $("#license").val(),
            "userEnabled": $("input[name='clientEnabled']:checked").val(),
            "accountEnabled": $("input[name='accountEnabled']:checked").val()
        }),
        success: function(data) {
            if(data.errCode != '000000') {
                layer.msg("修改失败:" + data.errMsg);
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

function checkCorpName() {
    var corpName = $("#corpName").val();
    if(corpName == "") {
        $("#corpNameTip").text("公司名称不能为空！");
        $("#corpNameTip").show();
    }
    else {
        $("#corpNameTip").text("");
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
        $("#contactTip").text("");
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