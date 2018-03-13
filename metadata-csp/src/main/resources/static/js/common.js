$(function() {
    $("#greeting").text(getGreeting());
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

function getGreeting() {
    var hour = new Date().getHours();
    if(hour < 12) {
        return sessionStorage.getItem("user_name");
    }
    else if(hour < 18) {
        return sessionStorage.getItem("user_name");
    }
    else {
        return sessionStorage.getItem("user_name");
    }
}
