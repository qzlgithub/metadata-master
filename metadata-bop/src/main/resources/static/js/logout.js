$("#logout").click(function() {
    layer.confirm('确定退出系统？', {
        btn: ['确定', '取消'],
        yes: function() {
            window.location.href = "/user/logout";
        },
        no: function() {
            layer.closeAll();
        }
    });
});