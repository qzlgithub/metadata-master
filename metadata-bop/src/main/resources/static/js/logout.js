$("#logout").click(function() {
    // 管理员登出
    $.post(
        "/manager/logout",
        function(data) {
            window.location.href = "/";
        }
    )
});