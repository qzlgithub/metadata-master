$(function() {
    $("#greeting").text(getGreeting());
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