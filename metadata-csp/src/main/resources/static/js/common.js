$(function() {
    $("#greeting").text(getGreeting());
    settingQq();
    var haveMessage = localStorage.getItem("have_message");
    if(haveMessage === "1") {
        $("#message-right-id").append('<em class="email-icon"></em>');
    }
});

function settingQq() {
    var managerQq = localStorage.getItem("manager_qq");
    var serviceQq = localStorage.getItem("service_qq");
    $(".setting-qq-class").attr("href", "http://wpa.qq.com/msgrd?v=3&uin=" + managerQq + "&site=qq&menu=yes");
    $(".service-qq-class").attr("href", "http://wpa.qq.com/msgrd?v=3&uin=" + serviceQq + "&site=qq&menu=yes");
}

$("#exit").click(function() {
    layer.confirm('确定退出系统？', {
        btn: ['确定', '取消'],
        yes: function() {
            $.ajax({
                type: "post",
                url: "/client/user/logout",
                dataType: "json",
                contentType: "application/json",
                success: function() {
                    window.location.href = "/";
                }
            });
        },
        no: function() {
            layer.closeAll();
        }
    });
});

function getGreeting() {
    var hour = new Date().getHours();
    if(hour < 12) {
        return localStorage.getItem("user_name");
    }
    else if(hour < 18) {
        return localStorage.getItem("user_name");
    }
    else {
        return localStorage.getItem("user_name");
    }
}
