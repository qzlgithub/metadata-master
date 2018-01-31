var form;
$(function() {
    layui.use('form', function() {
        form = layui.form;
    });
    clientListInit();
});

function clientListInit(){
    var obj = {
        pageNum: 1,
        pageSize: 10,
        enabled: $("#enabled").val(),
        username: $("#username").val(),
        corpName: $("#corpName").val(),
        shortName: $("#shortName").val(),
        parentIndustryId: $("#parentIndustryId").val(),
        industryId: $("#industryId").val()
    };
    getClientList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getClientList(obj);
            }
        })
    });
}

var tr1
    =
    '<tr><td><div class="layui-form"><input class="obj-checkbox" type="checkbox" name="checked"  lay-skin="primary" id="checkbox-#{id}" lay-filter="choose" data-id="#{id}"/><div class="layui-unselect layui-form-checkbox" lay-skin="primary"><i class="layui-icon"></i></div></div></td>' +
    '<td>#{id}</td>' +
    '<td>#{username}</td>' +
    '<td>#{corpName}</td>' +
    '<td>#{shortName}</td>' +
    '<td>#{industry}</td>' +
    '<td>#{contact}</td>' +
    '<td>#{phone}</td>' +
    '<td>#{managerName}</td>';
var tr21 = '<td><span class="cp col3" onclick="showChild(\'#{id}\')">#{accountQty}</span></td>';
var tr22 = '<td>0</td>';
var tr3 = '<td>#{registerDate}</td>' +
    '<td><span class="mr30">' +
    '<a href="/client/edit.html?clientId=#{id}" class="edit">编辑</a></span>' +
    '<span class="mr30"><a href="/client/detail.html?clientId=#{id}" class="edit">查看</a></span>' +
    '<span class="mr30"><a href="#" id="accountEnabled#{id}" obj-enabled="#{enabled}" class="edit" onclick="changeStatus(\'#{id}\')">#{accountEnabled}</a></span>' +
    '<a href="#" class="del" onclick="dropClient(\'#{id}\')">停用</a>' +
    '</td></tr>';

function getClientList(obj, pageFun) {
    $.get(
        "/client/list",
        {
            enabled: obj['enabled'],
            username: obj['username'],
            corpName: obj['corpName'],
            shortName: obj['shortName'],
            industryId: obj['industryId'],
            parentIndustryId: obj['parentIndustryId'],
            pageNum: obj['pageNum'],
            pageSize: obj['pageSize']
        },
        function(data) {
            var list = data.list;
            var total = data.total;
            var pages = data.pages;
            $("#dataBody").empty();
            for(var d in list) {
                var dataTr = tr1.replace(/#{id}/g, list[d].id).replace("#{username}", list[d].username)
                .replace("#{corpName}", list[d].corpName).replace("#{shortName}", list[d].shortName)
                .replace("#{industry}", list[d].industry).replace("#{contact}", list[d].name)
                .replace("#{phone}", list[d].phone).replace("#{managerName}", list[d].managerName);
                if(list[d].accountQty > 0) {
                    dataTr = dataTr + tr21.replace(/#{id}/g, list[d].id).replace("#{accountQty}", list[d].accountQty);
                }
                else {
                    dataTr = dataTr + tr22;
                }
                dataTr = dataTr + tr3.replace(/#{id}/g, list[d].id).replace("#{registerDate}", list[d].registerDate)
                .replace("#{enabled}", list[d].userEnabled)
                .replace("#{accountEnabled}", list[d].userEnabled === 1 ? "冻结账号" : "解冻账号");
                $("#dataBody").append(dataTr);
            }
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
            renderCheckbox();
        }
    );
}

function resetChildPwd(id) {
    $.ajax({
        type: "post",
        url: "/client/user/resetPwd",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"id": id}),
        success: function(data) {
            if(data.errCode === '000000') {
                layer.msg("账号密码已重置", {
                    time: 2000
                });
            }
            else {
                layer.msg("操作失败:" + data.errMsg, {
                    time: 2000
                });
            }
        }
    });
}

var childTr =
    '<tr><td>#{username}</td><td>#{name}</td><td>#{phone}</td><td><span class="mr30"><a href="javascript:resetChildPwd(\'#{id}\')" class="edit">重置密码</a></span><a href="/client/edit.html?clientId=#{id}" class="edit">查看消费</a></td></tr>';

function showChild(id) {
    $.get(
        "/client/subAccount/list",
        {"id": id},
        function(data) {
            $("#child-account-body").empty();
            for(var d in data) {
                var tr = childTr.replace(/#{id}/g, data[d].id).replace(/#{username}/g, data[d].username)
                .replace(/#{name}/g, data[d].name).replace(/#{phone}/g, data[d].phone);
                $("#child-account-body").append(tr);
            }
            layer.open({
                title: false,
                type: 1,
                content: $('#child-account'),
                area: ['700px'],
                shadeClose: true
            });
        }
    );
}

function changeStatus(id) {
    $("#ban-type").val("single");
    var obj = $("#accountEnabled" + id);
    var enabled = obj.attr("obj-enabled");
    if(enabled === "1") {
        $("#ban-title").text("冻结账号");
        $("#ban-client-enabled").val(0);
    }
    else {
        $("#ban-title").text("解冻账号");
        $("#ban-client-enabled").val(1);
    }
    $("#ban-client-id").val(id);
    layer.open({
        title: false,
        type: 1,
        content: $('#ban-div'),
        area: ['700px'],
        shadeClose: true
    });
}

function batchChangeStatus(enabled) {
    var objs = $(".obj-checkbox");
    var clientIds = [];
    for(var o in objs) {
        if(objs[o].checked) {
            clientIds.push($(objs[o]).attr("data-id"));
        }
    }
    if(clientIds.length === 0) {
        layer.msg("请至少选择一个客户", {
            time: 2000
        });
    }
    else {
        $("#ban-type").val("batch");
        if(enabled === 1) {
            $("#ban-title").text("解冻账号");
            $("#ban-client-enabled").val(1);
        }
        else {
            $("#ban-title").text("冻结账号");
            $("#ban-client-enabled").val(0);
        }
        layer.open({
            title: false,
            type: 1,
            content: $('#ban-div'),
            area: ['700px'],
            shadeClose: true
        });
    }
}

function banClient() {
    var reason = $("#ban-reason").val();
    if(reason === '') {
        layer.msg("原因不能为空", {time: 2000});
        return;
    }
    var banType = $("#ban-type").val();
    var clients = [];
    if(banType === 'batch') {
        var objs = $(".obj-checkbox");
        for(var o in objs) {
            if(objs[o].checked) {
                clients.push($(objs[o]).attr("data-id"));
            }
        }
    }
    else if(banType === 'single') {
        clients.push($("#ban-client-id").val());
    }
    else {
        return;
    }
    var enabled = $("#ban-client-enabled").val();
    $.ajax({
        type: "POST",
        url: "/client/changeStatus",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"id": clients, "enabled": enabled, "reason": reason}),
        success: function(res) {
            if(res.errCode === '000000') {
                $("#ban-reason").val('');
                layer.closeAll();
                for(var o in clients) {
                    var obj = $("#accountEnabled" + clients[o]);
                    obj.attr("obj-enabled", enabled);
                    obj.text(enabled === '1' ? "冻结账号" : "解冻账号");
                }
                $("div").removeClass('layui-form-checked');
                layer.msg(enabled === '1' ? "账号已解冻" : "账号已冻结", {time: 2000});
            }
            else {
                layer.msg("操作失败：" + res.errMsg, {time: 2000});
            }
        }
    });
}

function batchDeleted() {
    var objs = $(".obj-checkbox");
    var clientIds = [];
    for(var o in objs) {
        if(objs[o].checked) {
            clientIds.push($(objs[o]).attr("data-id"));
        }
    }
    if(clientIds.length === 0) {
        layer.msg("请至少选择一个客户", {
            time: 2000
        });
    }
    else {
        layer.confirm('是否确定批量停用？', {
            btn: ['确定', '取消'],
            yes: function() {
                $(this).click();
                $.ajax({
                    type: "POST",
                    url: "/client/deletion",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify({"id": clientIds}),
                    success: function(data) {
                        if(data.errCode === '000000') {
                            clientListInit();
                            // $("#allChoose").removeAttr("checked");
                            layer.msg("账号已停用", {
                                time: 2000
                            });
                        }
                        else {
                            layer.msg("操作失败:" + data.errMsg, {
                                time: 2000
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
}

function batchResetPwd() {
    var objs = $(".obj-checkbox");
    var clientIds = [];
    for(var o in objs) {
        if(objs[o].checked) {
            clientIds.push($(objs[o]).attr("data-id"));
        }
    }
    if(clientIds.length === 0) {
        layer.msg("请至少选择一个客户", {
            time: 2000
        });
    }
    else {
        layer.confirm('是否确定批量重置密码？', {
            btn: ['确定', '取消'],
            yes: function() {
                $(this).click();
                $.ajax({
                    type: "POST",
                    url: "/client/resetPwd",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify({"id": clientIds}),
                    success: function(data) {
                        if(data.errCode === '000000') {
                            layer.msg("账号密码已重置", {
                                time: 2000
                            });
                        }
                        else {
                            layer.msg("操作失败:" + data.errMsg, {
                                time: 2000
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
}

function dropClient(id) {
    layer.confirm('是否确定停用账户？', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();
            $.ajax({
                type: "POST",
                url: "/client/deletion",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": [id]}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        searchClientList();
                        layer.msg("账号已停用", {
                            time: 2000
                        });
                    }
                    else {
                        layer.msg("操作失败:" + data.errMsg, {
                            time: 2000
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

function getSubIndustry() {
    var parentId = $("#parentIndustryId").val();
    if(parentId !== "") {
        $.get(
            "/system/industry/childList",
            {"industryId": parentId},
            function(data) {
                $("#industryId").empty();
                $("#industryId").append('<option value="">全部</option>');
                for(var d in data) {
                    $("#industryId").append('<option value="' + data[d].id + '">' + data[d].name + '</option>');
                }
            }
        )
    }
    else {
        $("#industryId").empty();
        $("#industryId").append('<option value="">全部</option>');
    }
}

//重新渲染
function renderCheckbox() {
    //无需再执行layui.use()方法加载模块，直接使用即可
    form.render('checkbox');
}