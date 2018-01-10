var pageLink = '<a href="javascript:goPage(#{pageNum});">#{pageNum}</a>';
var childTr =
    '<tr><td>#{username}</td><td>#{name}</td><td>#{phone}</td><td><span class="mr30"><a href="javascript:resetChildPwd(\'#{id}\')" class="edit">重置密码</a></span><a href="/client/edit.html?clientId=#{id}" class="edit">查看消费</a></td></tr>';
$(function() {
    getClientList(null, null, null, null, null, null, 1, $("#pageSize").val());
});

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

function batchChangeStatus(enabled) {
    var objs = $(".obj-checkbox");
    var clientIds = [];
    for(var o in objs) {
        if(objs[o].checked) {
            clientIds.push($(objs[o]).attr("data-id"));
        }
    }
    if(clientIds.length == 0) {
        layer.msg("请至少选择一项", {
            time: 2000
        });
    }
    else {
        var enabledVal = $("#accountEnabled" + clientIds[0]).text();
        layer.confirm('是否确定批量' + enabledVal + '？', {
            btn: ['确定', '取消'],
            yes: function() {
                $(this).click();
                $.ajax({
                    type: "POST",
                    url: "/client/changeStatus",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify({"id": clientIds, "enabled": enabled}),
                    success: function(data) {
                        if(data.errCode === '000000') {
                            goPage($("#pageNum").val());
                            layer.msg("账号已" + enabledVal, {
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

function batchDeleted() {
    var objs = $(".obj-checkbox");
    var clientIds = [];
    for(var o in objs) {
        if(objs[o].checked) {
            clientIds.push($(objs[o]).attr("data-id"));
        }
    }
    if(clientIds.length == 0) {
        layer.msg("请至少选择一项", {
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
                            goPage($("#pageNum").val());
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
    if(clientIds.length == 0) {
        layer.msg("请至少选择一项", {
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

function changeStatus(id) {
    var enabledVal = $("#accountEnabled" + id).text();
    layer.confirm('是否确定' + enabledVal + '？', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();
            $.ajax({
                type: "POST",
                url: "/client/changeStatus",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": [id]}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        var obj = data.dataMap;
                        if(obj.accountEnabled === 1) {
                            $("#accountEnabled" + id).text("冻结账号");
                            layer.msg("账号已解冻", {
                                time: 2000
                            });
                        }
                        else {
                            $("#accountEnabled" + id).text("解冻账号");
                            layer.msg("账号已冻结", {
                                time: 2000
                            });
                        }
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

function gotoPrev() {
    var pageNum = $("#pageNum").val();
    if(pageNum > 1) {
        var enabledVal = $("#enabled").val();
        var usernameVal = $("#username").val();
        var corpNameVal = $("#corpName").val();
        var shortNameVal = $("#shortName").val();
        var parentIndustryId = $("#parentIndustryId").val();
        var industryIdVal = $("#industryId").val();
        var pageNumVal = pageNum - 1;
        var pageSizeVal = $("#pageSize").val();
        getClientList(enabledVal, usernameVal, corpNameVal, shortNameVal, parentIndustryId, industryIdVal,
            pageNumVal, pageSizeVal);
    }
}

function gotoNext() {
    var pageNum = $("#pageNum").val();
    var totalPage = $("#totalPage").val();
    if(pageNum !== totalPage) {
        var enabledVal = $("#enabled").val();
        var usernameVal = $("#username").val();
        var corpNameVal = $("#corpName").val();
        var shortNameVal = $("#shortName").val();
        var parentIndustryId = $("#parentIndustryId").val();
        var industryIdVal = $("#industryId").val();
        var pageNumVal = parseInt(pageNum) + 1;
        var pageSizeVal = $("#pageSize").val();
        getClientList(enabledVal, usernameVal, corpNameVal, shortNameVal, parentIndustryId, industryIdVal,
            pageNumVal, pageSizeVal);
    }
}

function searchClientList() {
    var enabledVal = $("#enabled").val();
    var usernameVal = $("#username").val();
    var corpNameVal = $("#corpName").val();
    var shortNameVal = $("#shortName").val();
    var parentIndustryId = $("#parentIndustryId").val();
    var industryIdVal = $("#industryId").val();
    var pageNumVal = 1;
    var pageSizeVal = $("#pageSize").val();
    getClientList(enabledVal, usernameVal, corpNameVal, shortNameVal, parentIndustryId, industryIdVal, pageNumVal,
        pageSizeVal);
}

function goPage(pageNum) {
    var enabledVal = $("#enabled").val();
    var usernameVal = $("#username").val();
    var corpNameVal = $("#corpName").val();
    var shortNameVal = $("#shortName").val();
    var parentIndustryId = $("#parentIndustryId").val();
    var industryIdVal = $("#industryId").val();
    var pageNumVal = pageNum;
    var pageSizeVal = $("#pageSize").val();
    getClientList(enabledVal, usernameVal, corpNameVal, shortNameVal, parentIndustryId, industryIdVal, pageNumVal,
        pageSizeVal);
}

function gotoPage() {
    var enabledVal = $("#enabled").val();
    var usernameVal = $("#username").val();
    var corpNameVal = $("#corpName").val();
    var shortNameVal = $("#shortName").val();
    var parentIndustryId = $("#parentIndustryId").val();
    var industryIdVal = $("#industryId").val();
    var pageNumVal = $("#userPageNum").val();
    var pageSizeVal = $("#pageSize").val();
    getClientList(enabledVal, usernameVal, corpNameVal, shortNameVal, parentIndustryId, industryIdVal, pageNumVal,
        pageSizeVal);
}

// <![CDATA[
var tr1
    =
    '<tr><td><div class="layui-form"><input class="obj-checkbox" type="checkbox" name="checked" lay-skin="primary" lay-filter="choose" data-id="#{id}"/><div class="layui-unselect layui-form-checkbox" lay-skin="primary"><i class="layui-icon"></i></div></div></td>' +
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
    '<span class="mr30"><a id="accountEnabled#{id}" href="#" class="edit" onclick="changeStatus(\'#{id}\')">#{accountEnabled}</a></span>' +
    '<a href="#" class="del" onclick="dropClient(\'#{id}\')">停用</a>' +
    '</td></tr>';

function getClientList(enabledVal, usernameVal, corpNameVal, shortNameVal, parentIndustryId, industryIdVal,
                       pageNumVal, pageSizeVal) {
    $("#pageNum").val(pageNumVal);
    $.get(
        "/client/list",
        {
            enabled: enabledVal,
            username: usernameVal,
            corpName: corpNameVal,
            shortName: shortNameVal,
            industryId: industryIdVal,
            parentIndustryId: parentIndustryId,
            pageNum: pageNumVal,
            pageSize: pageSizeVal
        },
        function(data) {
            var list = data.list;
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
                .replace("#{accountEnabled}", list[d].userEnabled === 1 ? "冻结账号" : "解冻账号");
                $("#dataBody").append(dataTr);
            }
            $("#total").text("共 " + data.total + " 条");
            $("#pages").text("共 " + data.pages + " 页");
            $("#totalPage").val(data.pages);
            $("#currentPage").text(data.pageNum);
            if(data.pageNum === 1) {
                $("#prevPage").addClass("layui-disabled");
            }
            else {
                $("#prevPage").removeClass("layui-disabled");
            }
            if(data.pageNum === data.pages) {
                $("#nextPage").addClass("layui-disabled");
            }
            else {
                $("#nextPage").removeClass("layui-disabled");
            }
            refreshPageInfo($("#frontPages"), $("#nextPages"), data.pageNum, data.pages);
        }
    );
}

function refreshPageInfo(front, next, pageNum, totalPage) {
    front.empty();
    next.empty();
    if(pageNum > 1) {
        front.append(pageLink.replace(/#{pageNum}/g, 1));
    }
    if(pageNum === 3) {
        front.append(pageLink.replace(/#{pageNum}/g, 2));
    }
    else if(pageNum > 3) {
        front.append("...");
        front.append(pageLink.replace(/#{pageNum}/g, pageNum - 1));
    }
    if(totalPage === pageNum + 2) {
        next.append(pageLink.replace(/#{pageNum}/g, totalPage - 1));
    }
    else if(totalPage > pageNum + 2) {
        next.append(pageLink.replace(/#{pageNum}/g, pageNum + 1));
        next.append("...");
    }
    if(pageNum < totalPage) {
        next.append(pageLink.replace(/#{pageNum}/g, totalPage));
    }
}

// ]]>