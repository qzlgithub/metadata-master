$(function() {
    getManagerList(null, null, 1, $("#pageSize").val())
});

function search() {
    getManagerList($("#roleId").val(), $("#enabled").val(), 1, $("#pageSize").val());
}

function resetPageSize() {
    getManagerList($("#roleId").val(), $("#enabled").val(), 1, $("#pageSize").val());
}

function gotoPrev() {
    var pageNum = $("#pageNum").val();
    if(pageNum > 1) {
        getManagerList($("#roleId").val(), $("#enabled").val(), parseInt(pageNum) - 1, $("#pageSize").val());
    }
}

//<![CDATA[
function gotoNext() {
    var pageNum = $("#pageNum").val();
    var totalPage = $("#pages").val();
    if(parseInt(pageNum) < parseInt(totalPage)) {
        getManagerList($("#roleId").val(), $("#enabled").val(), parseInt(pageNum) + 1, $("#pageSize").val());
    }
}

function gotoPage() {
    var userPageNum = $("#userPageNum").val();
    var pages = $("#pages").val();
    if(parseInt(userPageNum) > parseInt(pages)) {
        userPageNum = pages;
    }
    getManagerList($("#roleId").val(), $("#enabled").val(), userPageNum, $("#pageSize").val());
}

function refreshPageInfo(front, next, pageNum, totalPage) {
    var pageLink = '<a href="javascript:goPage(#{pageNum});">#{pageNum}</a>';
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

function goPage(pageNum) {
    var userPageNum = pageNum;
    var pageSizeVal = $("#pageSize").val();
    getManagerList($("#roleId").val(), $("#enabled").val(), userPageNum, pageSizeVal);
}

//]]>
var rowStr =
    '<tr><td>#{id}</td><td>#{username}</td><td>#{name}</td><td>#{phone}</td><td>#{roleName}</td><td id="enabled#{id}">#{enabled}</td><td>#{registerDate}</td><td><span class="mr30"><a href="/manager/edit.html?id=#{id}" class="edit">编辑</a></span><a href="#" id="statusAction#{id}" class="del" onclick="changeStatus(\'#{id}\')">#{statusAction}</a></td></tr>';

function getManagerList(roleId, enabled, pageNum, pageSize) {
    $.get(
        "/manager/list",
        {
            "roleId": roleId,
            "enabled": enabled,
            "pageNum": pageNum,
            "pageSize": pageSize
        },
        function(data) {
            var list = data.list;
            $("#dataBody").empty();
            for(var d in list) {
                var row = rowStr.replace(/#{id}/g, list[d].id).replace("#{username}", list[d].username)
                .replace("#{registerDate}", list[d].registerDate)
                .replace("#{enabled}", list[d].enabled === 1 ? '已启用' : '已禁用')
                .replace("#{roleName}", list[d].roleName).replace("#{name}", list[d].name)
                .replace("#{phone}", list[d].phone)
                .replace("#{statusAction}", list[d].enabled === 1 ? '禁用' : '启用');
                $("#dataBody").append(row);
            }
            $("#totalTxt").text("共 " + data.total + " 条");
            $("#pagesTxt").text("共 " + data.pages + " 页");
            $("#pages").val(data.pages);
            $("#pageNum").val(data.pageNum);
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
            refreshPageInfo($("#front"), $("#next"), data.pageNum, data.pages);
        }
    );
}

function changeStatus(id) {
    var txt = $("#statusAction" + id).text();
    layer.confirm('是否确定' + txt + '？', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();
            $.ajax({
                type: "POST",
                url: "/manager/changeStatus",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": id}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        var obj = data.dataMap;
                        if(obj.enabled === 1) {
                            $("#statusAction" + id).text("禁用");
                            $("#enabled" + id).text("已启用");
                        }
                        else {
                            $("#statusAction" + id).text("启用");
                            $("#enabled" + id).text("已禁用");
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