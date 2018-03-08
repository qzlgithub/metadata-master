var form;
$(function() {
    layui.use('form', function() {
        form = layui.form;
    });
    getAccountList();
});
var rowStr = '<tr id="accountTrId#{id}"><td id="accountUserName#{id}">#{username}</td><td>#{name}</td><td>#{phone}</td>' +
    '<td><span class="mr30"><a class="edit cp" onclick="editAccount(\'#{id}\')">编辑</a></span>' +
    '<span class="mr30"><a class="edit cp" onclick="stopAccount(this)" id="statusAction#{id}" data-id="#{id}" data-status="#{enabled}">#{statusName}</a></span>' +
    '<span class="mr30"><a class="del cp" onclick="delAccount(\'#{id}\')">删除</a></span>' +
    '</td></tr>';

function getAccountList() {
    $.get(
        "/client/sub-account/list",
        {},
        function(data) {
            var list = data.list;
            $("#dataBody").empty();
            for(var d in list) {
                var row = rowStr.replace(/#{id}/g, list[d].userId).replace("#{username}", list[d].username)
                .replace("#{name}", list[d].name).replace("#{enabled}", list[d].enabled)
                .replace("#{phone}", list[d].phone);
                if(list[d].enabled === 1) {
                    row = row.replace("#{statusName}", "禁用");
                }
                else {
                    row = row.replace("#{statusName}", "启用");
                }
                $("#dataBody").append(row);
            }
            var allowedQty = data.data.allowedQty;
            $("#acountAll").text(list.length);
            $("#canAddNumber").text(allowedQty - list.length);
            if(allowedQty - list.length > 0) {
                $("#addaccount").show();
            }
        }
    );
}

function stopAccount(obj) {
    var obj_id = $(obj).data("id");
    var obj_status = $(obj).data("status");
    var tip, txt, status;
    if(obj_status === 1) {
        tip = "禁用";
        txt = "启用";
        status = 0;
    }
    else {
        tip = "启用";
        txt = "禁用";
        status = 1;
    }
    layer.confirm('是否确定' + tip + '该账号？', {
        btn: ['确定', '取消'],
        yes: function() {
            $.ajax({
                type: "POST",
                url: "/client/sub-account/status",
                contentType: "application/json",
                data: JSON.stringify({"clientUserId": obj_id, "status": status}),
                success: function(res) {
                    if(res.code === '000000') {
                        var elem = $("#statusAction" + obj_id);
                        elem.text(txt);
                        elem.data("status", status);
                        layer.msg(tip + "成功", {time: 2000});
                    }
                    else {
                        layer.msg("操作失败：" + res.message, {time: 2000});
                    }
                }
            });
            layer.closeAll();
        },
        no: function() {
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
                success: function(res) {
                    if(res.code === '000000') {
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
        no: function() {
            layer.closeAll();
        }
    });
}

function editAccount(id) {
    $.get(
        "/client/childAccountDetail",
        {"clientUserId": id},
        function(res) {
            if(res.code === '000000') {
                var obj = res.data;
                $("#edit-id").val(obj.clientUserId + "");
                $("#edit-username").val(obj.username);
                $("#edit-pwd").val("");
                $("#edit-name").val(obj.name);
                $("#edit-phone").val(obj.phone);
                $('input:radio[name="edit-enabled"]').prop("checked", false);
                $('#account-status-div-id').html('');
                var html = '';
                if(obj.enabled === 1) {
                    html += '<input type="radio" name="edit-enabled" id="edit-enabled-1" value="1" title="正常" checked/>';
                    html += '<input type="radio" name="edit-enabled" id="edit-enabled-0" value="0" title="禁用" />';
                }
                else {
                    html += '<input type="radio" name="edit-enabled" id="edit-enabled-1" value="1" title="正常" />';
                    html += '<input type="radio" name="edit-enabled" id="edit-enabled-0" value="0" title="禁用" checked/>';
                }
                $('#account-status-div-id').html(html);
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
                layer.msg("获取信息失败，" + res.message, {
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
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    if(!checkDataValid("#add-account")) {
        isSubmit = false;
        return;
    }
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
        data: JSON.stringify({
            "username": username,
            "password": MD5(pwd),
            "name": name,
            "phone": phone,
            "enabled": enabled
        }),
        success: function(res) {
            if(res.code === '000000') {
                layer.msg("新增成功", {
                    time: 2000
                }, function() {
                    isSubmit = false;
                    layer.closeAll();
                    getAccountList();
                });
            }
            else {
                layer.msg("新增失败，" + res.message, {
                    time: 2000
                }, function() {
                    isSubmit = false;
                });
            }
        }
    });
}

function editSubmitAccount() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    if(!checkDataValid("#edit-account")) {
        isSubmit = false;
        return;
    }
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
        success: function(res) {
            if(res.code === '000000') {
                layer.msg("修改成功", {
                    time: 2000
                }, function() {
                    isSubmit = false;
                    layer.closeAll();
                    getAccountList();
                });
            }
            else {
                layer.msg("修改失败，" + res.message, {
                    time: 2000
                }, function() {
                    isSubmit = false;
                });
            }
        }
    });
}