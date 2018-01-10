$(function() {
});

function submitManager() {
    var roleId = $("#roleId").val();
    var username = $("#username").val();
    var password = $("#password").val();
    var name = $("#name").val();
    var phone = $("#phone").val();
    var enabled = $("input[name='enabled']:checked").val();
    console.log("roleId: " + roleId + "\nusername: " + username + "\npassword: " + password + "\nname: " + name
        + "\nphone: " + phone + "\nenabled: " + enabled);
    $.ajax({
        type: "POST",
        url: "/manager/addition",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "roleId": roleId,
            "username": username,
            "password": password,
            "name": name,
            "phone": phone,
            "enabled": enabled
        }),
        success: function(data) {
            if(data.errCode === "000000") {
                layer.msg("添加成功", {
                    time: 2000
                });
            }
            else {
                layer.msg("添加失败:" + data.errMsg, {
                    time: 2000
                });
            }
        }
    });
}