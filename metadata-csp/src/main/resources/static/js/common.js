$(function() {
    var greeting = $("#greeting");
    var hour = new Date().getHours();
    if(hour < 12) {
        greeting.text("上午好，" + sessionStorage.getItem("user_name"));
    }
    else if(hour < 18) {
        greeting.text("下午好，" + sessionStorage.getItem("user_name"));
    }
    else {
        greeting.text("晚上好，" + sessionStorage.getItem("user_name"));
    }
});