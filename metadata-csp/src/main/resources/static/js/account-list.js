$(function() {
    getAccountList();
});

var rowStr = '<tr><td>#{username}</td><td>#{name}</td><td>#{phone}</td>' +
    '<td><span class="mr30"><a class="edit cp" href="javascript:" onclick="editAccount(\'#{id}\')">编辑</a></span>' +
    '<span class="mr30"><a href="javascript:" class="del" onclick="editAccount(\'#{id}\')">停用</a></span>' +
    '<span class="mr30"><a href="javascript:" class="del" onclick="editAccount(\'#{id}\')">删除</a></span>' +
    '</td>' +
    '</tr>';

function getAccountList(){
    $.get(
        "/client/account/list",
        {},
        function(data) {
            var list = data.list;
            $("#dataBody").empty();
            for(var d in list) {
                var row = rowStr.replace(/#{id}/g, list[d].id).replace("#{username}", list[d].username)
                .replace("#{name}", list[d].name)
                .replace("#{phone}", list[d].phone);
                $("#dataBody").append(row);
            }
        }
    );
}

function editAccount(){
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