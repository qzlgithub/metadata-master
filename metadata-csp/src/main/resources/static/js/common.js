$(function() {
    $("#greeting").text(getGreeting());
});
$("#org-enc-password").keyup(function() {
    $("#org-dec-password").val($("#org-enc-password").val());
});
$("#org-dec-password").keyup(function() {
    $("#org-enc-password").val($("#org-dec-password").val());
});
$("#new-enc-password").keyup(function() {
    $("#new-dec-password").val($("#new-enc-password").val());
});
$("#new-dec-password").keyup(function() {
    $("#new-enc-password").val($("#new-dec-password").val());
});
$("#org-show").click(function() {
    $("#org-enc-pwd").hide();
    $("#org-dec-pwd").show();
});
$("#org-hide").click(function() {
    $("#org-enc-pwd").show();
    $("#org-dec-pwd").hide();
});
$("#new-show").click(function() {
    $("#new-enc-pwd").hide();
    $("#new-dec-pwd").show();
});
$("#new-hide").click(function() {
    $("#new-enc-pwd").show();
    $("#new-dec-pwd").hide();
});
$('#chgPwd').on('click', function() {
    layer.open({
        title: false,
        type: 1,
        content: $('#change-password'),
        area: ['500px'],
        shadeClose: true
    });
});
$("#exit").click(function() {
    $.ajax({
        type: "post",
        url: "/client/user/logout",
        dataType: "json",
        contentType: "application/json",
        success: function() {
            window.location.href = "/index.html";
        }
    });
});
$("#change-pwd").click(function() {
    var orgPwd = $("#org-enc-password").val();
    var newPwd = $("#new-enc-password").val();
    if(orgPwd === '' || newPwd === '') {
        layer.msg("请填写正确的密码！");
        return;
    }
    changePwd(orgPwd, newPwd);
});

function getGreeting() {
    var hour = new Date().getHours();
    if(hour < 12) {
        return "上午好，" + sessionStorage.getItem("user_name");
    }
    else if(hour < 18) {
        return "下午好，" + sessionStorage.getItem("user_name");
    }
    else {
        return "晚上好，" + sessionStorage.getItem("user_name");
    }
}

function changePwd(orgPwd, newPwd) {
    $.ajax({
        type: "POST",
        url: "/client/user/password",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "orgPassword": MD5(orgPwd),
            "newPassword": MD5(newPwd)
        }),
        success: function(data) {
            if(data.errCode !== '000000') {
                layer.msg("修改失败:" + data.errMsg, {
                    time: 2000
                });
            }
            else {
                layer.msg("修改成功", {
                    time: 2000
                }, function() {
                    layer.closeAll();
                });
            }
        }
    })
}