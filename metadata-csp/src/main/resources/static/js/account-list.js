$(function() {
    getAccountList();
});

var rowStr = '<tr><td id="accountUserName#{id}">#{username}</td><td>#{name}</td><td>#{phone}</td>' +
    '<td><span class="mr30"><a class="edit cp" href="javascript:" onclick="editAccount(\'#{id}\')">编辑</a></span>' +
    '<span class="mr30"><a href="javascript:" class="del" onclick="stopAccount(\'#{id}\')" id="statusAction#{id}">停用</a></span>' +
    '<span class="mr30"><a href="javascript:" class="del" onclick="delAccount(\'#{id}\')">删除</a></span>' +
    '<span class="mr30"><a href="javascript:" class="edit" onclick="requestAccount(\'#{id}\')">消费明细</a></span>' +
    '</td>' +
    '</tr>';

function getAccountList(){
    $.get(
        "/client/account/list",
        {},
        function(data) {
            if(data.errCode === '000000') {
                var list = data.dataMap.list;
                $("#dataBody").empty();
                for(var d in list) {
                    var row = rowStr.replace(/#{id}/g, list[d].id).replace("#{username}", list[d].username)
                    .replace("#{name}", list[d].name)
                    .replace("#{phone}", list[d].phone);
                    $("#dataBody").append(row);
                }
            }
        }
    );
}

function stopAccount(id){
    var txt = $("#accountUserName" + id).text();
    layer.confirm('是否确定停用' + txt + '？', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();
            $.ajax({
                type: "POST",
                url: "/client/changeStatus",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": id}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        var obj = data.dataMap;
                        if(obj.enabled === 1) {
                            $("#statusAction" + id).text("停用");
                            layer.msg("启用成功", {
                                time: 2000
                            });
                        }
                        else {
                            $("#statusAction" + id).text("启用");
                            layer.msg("停用成功", {
                                time: 2000
                            });
                        }
                    }
                }
            });
            layer.closeAll();
        },
        btn2: function() {
            layer.closeAll();
        }
    });
}

function delAccount(id){
    var txt = $("#accountUserName" + id).text();
    layer.confirm('是否确定删除' + txt + '？', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();
            $.ajax({
                type: "POST",
                url: "/client/user/deletion",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": id}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        layer.msg("删除成功", {
                            time: 2000
                        });
                    }
                }
            });
            layer.closeAll();
        },
        btn2: function() {
            layer.closeAll();
        }
    });
}

function editAccount(id){
    $.get(
        "/client/editChildAccount",
        {},
        function(data) {

        }
    );
    layer.open({
        title: false,
        type: 1,
        content: $('#edit-account'),
        area: ['500px'],
        shadeClose: true
    });
}

function addAccount(){
    layer.open({
        title: false,
        type: 1,
        content: $('#add-account'),
        area: ['500px'],
        shadeClose: true
    });
}

function requestAccount(){

}