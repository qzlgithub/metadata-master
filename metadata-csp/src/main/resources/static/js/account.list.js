var form;
$(function() {
    layui.use('form', function() {
        form = layui.form;
    });
    getAccountList();
});
var rowStr = '<tr id="accountTrId#{id}"><td id="accountUserName#{id}">#{username}</td><td>#{name}</td><td>#{phone}</td>' +
    '<td><span class="mr30"><a class="edit cp" href="javascript:" onclick="editAccount(\'#{id}\')">编辑</a></span>' +
    '<span class="mr30"><a href="javascript:" class="del" onclick="stopAccount(\'#{id}\')" id="statusAction#{id}">#{statusName}</a></span>' +
    '<span class="mr30"><a href="javascript:" class="del" onclick="delAccount(\'#{id}\')">删除</a></span>' +
    '</td>' +
    '</tr>';

function getAccountList() {
    $.get(
        "/client/sub-account/list",
        {},
        function(data) {
            if(data.errCode === '000000') {
                var list = data.dataMap.list;
                $("#dataBody").empty();
                for(var d in list) {
                    var row = rowStr.replace(/#{id}/g, list[d].userId).replace("#{username}", list[d].username)
                    .replace("#{name}", list[d].name)
                    .replace("#{phone}", list[d].phone);
                    if(list[d].enabled === 1) {
                        row = row.replace("#{statusName}", "禁用");
                    }
                    else {
                        row = row.replace("#{statusName}", "启用");
                    }
                    $("#dataBody").append(row);
                }
                var allowedQty = data.dataMap.allowedQty;
                $("#acountAll").text(list.length);
                $("#canAddNumber").text(allowedQty - list.length);
            }
        }
    );
}

function stopAccount(id) {
    var txt = $("#accountUserName" + id).text();
    layer.confirm('是否确定操作' + txt + '？', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();
            $.ajax({
                type: "POST",
                url: "/client/changeStatus",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"clientUserId": id}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        var obj = data.dataMap;
                        if(obj.enabled === 1) {
                            $("#statusAction" + id).text("禁用");
                            layer.msg("启用成功", {
                                time: 2000
                            });
                        }
                        else {
                            $("#statusAction" + id).text("启用");
                            layer.msg("禁用成功", {
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

function delAccount(id) {
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
                data: JSON.stringify({"clientUserId": id}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        layer.msg("删除成功", {
                            time: 1000
                        }, function() {
                            layer.closeAll();
                            getAccountList();
                        });
                    }
                    else {
                        layer.msg("删除失败", {
                            time: 2000
                        }, function() {
                            layer.closeAll();
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

function editAccount(id) {
    $.get(
        "/client/childAccountDetail",
        {"clientUserId": id},
        function(data) {
            if(data.errCode === '000000') {
                var obj = data.dataMap;
                $("#edit-id").val(obj.clientUserId + "");
                $("#edit-username").val(obj.username);
                $("#edit-pwd").val("");
                $("#edit-name").val(obj.name);
                $("#edit-phone").val(obj.phone);
                $('input:radio[name="edit-enabled"]').prop("checked", false);
                $('#account-status-div-id').html('');
                var html = '';
                if(obj.enabled === 1) {
                    html+='<input type="radio" name="edit-enabled" id="edit-enabled-1" value="1" title="正常" checked/>';
                    html+='<input type="radio" name="edit-enabled" id="edit-enabled-0" value="0" title="禁用" />';
                }
                else {
                    html+='<input type="radio" name="edit-enabled" id="edit-enabled-1" value="1" title="正常" />';
                    html+='<input type="radio" name="edit-enabled" id="edit-enabled-0" value="0" title="禁用" checked/>';
                }
                $('#account-status-div-id').append(html);
                form.render('radio');
                layer.open({
                    title: false,
                    type: 1,
                    content: $('#edit-account'),
                    area: ['500px'],
                    shadeClose: true
                });
            }
            else {
                layer.msg("获取信息失败，" + data.errMsg, {
                    time: 2000
                });
            }
        }
    );
}

function addAccount() {
    layer.open({
        title: false,
        type: 1,
        content: $('#add-account'),
        area: ['500px'],
        shadeClose: true
    });
}

var isSubmit = false;

function addSubmitAccount() {
    if(!isSubmit) {
        isSubmit = true;
        var username = $("#add-username").val();
        var pwd = $("#add-pwd").val();
        var name = $("#add-name").val();
        var phone = $("#add-phone").val();
        var enabled = $('input:radio[name="add-enabled"]:checked').val();
        $.ajax({
            type: "POST",
            url: "/client/account/addition",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({"username": username, "password": MD5(pwd), "name": name, "phone": phone, "enabled": enabled}),
            success: function(data) {
                if(data.errCode === '000000') {
                    layer.msg("新增成功", {
                        time: 2000
                    }, function() {
                        isSubmit = false;
                        layer.closeAll();
                        getAccountList();
                    });
                }
                else {
                    layer.msg("新增失败，" + data.errMsg, {
                        time: 2000
                    }, function() {
                        isSubmit = false;
                    });
                }
            }
        });
    }
}

function editSubmitAccount() {
    if(!isSubmit) {
        isSubmit = true;
        var id = $("#edit-id").val();
        var username = $("#edit-username").val();
        var pwd = $("#edit-pwd").val();
        var name = $("#edit-name").val();
        var phone = $("#edit-phone").val();
        var enabled = $('input:radio[name="edit-enabled"]:checked').val();
        $.ajax({
            type: "POST",
            url: "/client/editChildAccount",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "clientUserId": id,
                "username": username,
                "password": MD5(pwd),
                "name": name,
                "phone": phone,
                "enabled": enabled
            }),
            success: function(data) {
                if(data.errCode === '000000') {
                    var obj = data.dataMap;
                    layer.msg("修改成功", {
                        time: 2000
                    }, function() {
                        isSubmit = false;
                        layer.closeAll();
                        getAccountList();
                    });
                }
                else {
                    layer.msg("修改失败，" + data.errMsg, {
                        time: 2000
                    }, function() {
                        isSubmit = false;
                    });
                }
            }
        });
    }
}

function requestAccount() {
    window.location.href = "/product/request.html";
}