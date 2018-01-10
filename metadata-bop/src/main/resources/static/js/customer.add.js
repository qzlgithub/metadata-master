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
    /*console.log("account: " + $("#username").val() + "\ncorpName: " + $("#corpName").val() + "\nshortName: " +
        $("#shortName").val() + "\nindustryId: " + $("#industryId").val() + "\nname: " + $("#name").val()
        + "\nphone: " + $("#phone").val() + "\nemail: " + $("#email").val() + "\nlicense: " + $("#license").val()
        + "\nenabled: " + $("input[name='enabled']:checked").val());*/
    /*if(checkUsername()==true && checkCorpName()==true && checkCorpName() == true &&  checkContact() == true && checkPhone() == true
        && checkEmail() == true && checkLicense() == true){}*/
    $.ajax({
        type: "POST",
        url: "/client/addition",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "username": $("#username").val(),
            "password": MD5($("#password").text()),
            "corpName": $("#corpName").val(),
            "shortName": $("#shortName").val(),
            "industryId": $("#industryId").val(),
            "name": $("#name").val(),
            "phone": $("#phone").val(),
            "email": $("#email").val(),
            "license": $("#license").val(),
            "enabled": $("input[name='enabled']:checked").val()
        }),
        success: function(data) {
            if(data.errCode != '000000') {
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
                    return false;
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
        return false;
    }
}

function checkCorpName() {
    var corpName = $("#corpName").val();
    if(corpName == "") {
        $("#corpNameTip").text("公司名称不能为空！");
        $("#corpNameTip").show();
        return false;
    }
    else {
        $("#corpNameTip").text("");//长度未限定：^[0-9a-zA-Z\u4e00-\u9fa5][a-zA-Z0-9\u4e00-\u9fa5]{2,20}$
        $("#corpNameTip").hide();
    }
}

function checkContact() {
    var contact = $("#name").val();
    if(contact == "") {
        $("#contactTip").text("联系人不能为空！");
        $("#contactTip").show();
        return false;
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
            return false;
        }
    }
    else {
        $("#phoneTip").text("联系电话不能为空！");
        $("#phoneTip").show();
        return false;
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
            return false;
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
            return false;
        }
    }
    else {
        $("#licenseTip").text("社会信用代码不能为空！");
        $("#licenseTip").show();
        return false;
    }
}