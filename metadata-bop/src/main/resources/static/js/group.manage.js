$(function() {
    getRoleList(1, $("#pageSize").val());
});

function changePageSize() {
    var pageSizeVal = $("#pageSize").val();
    getRoleList(1, pageSizeVal);
}

//<![CDATA[
function gotoPage() {
    var customPageNum = $("#userPageNum").val();
    var pages = $("#totalPage").val();
    if(parseInt(customPageNum) > parseInt(pages)) {
        customPageNum = pages;
    }
    getRoleList(customPageNum, $("#pageSize").val());
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
    var pageNumVal = pageNum;
    var pageSizeVal = $("#pageSize").val();
    getRoleList(pageNumVal, pageSizeVal);
}

function gotoPrev() {
    var pageNum = $("#pageNum").val();
    if(pageNum > 1) {
        var pageNumVal = pageNum - 1;
        var pageSizeVal = $("#pageSize").val();
        getRoleList(pageNumVal, pageSizeVal);
    }
}

function gotoNext() {
    var pageNum = $("#pageNum").val();
    var totalPage = $("#totalPage").val();
    if(parseInt(pageNum) < parseInt(totalPage)) {
        var pageNumVal = parseInt(pageNum) + 1;
        var pageSizeVal = $("#pageSize").val();
        getRoleList(pageNumVal, pageSizeVal);
    }
}

//]]>
var rowStr =
    '<tr><td>#{id}</td><td>#{name}</td><td>#{privilege}</td><td><span class="mr30"><a href="/role/edit.html?id=#{id}" class="edit">编辑</a></span><a href="#" class="del" id="enabled#{id}" onclick="changeStatus(\'#{id}\')">#{enabled}</a></td></tr>';

function getRoleList(pageNumVal, pageSizeVal) {
    $("#pageNum").val(pageNumVal);
    $.get(
        "/role/list",
        {
            "pageNum": pageNumVal,
            "pageSize": pageSizeVal
        },
        function(data) {
            var list = data.list;
            $("#dataBody").empty();
            for(var d in list) {
                var row = rowStr.replace(/#{id}/g, list[d].id).replace("#{name}", list[d].name)
                .replace("#{privilege}", list[d].privilege)
                .replace("#{enabled}", list[d].enabled === 1 ? "禁用" : "启用");
                $("#dataBody").append(row);
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
            refreshPageInfo($("#front"), $("#next"), data.pageNum, data.pages);
        }
    );
}

function changeStatus(id) {
    var txt = $("#enabled" + id).text();
    layer.confirm('是否确定' + txt + '？', {
        btn: ['确定', '取消'],
        yes: function() {
            $(this).click();

            $.ajax({
                type: "POST",
                url: "/role/changeStatus",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": id}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        var obj = data.dataMap;
                        if(obj.enabled === 1) {
                            $("#enabled" + id).text("禁用");
                        }
                        else {
                            $("#enabled" + id).text("启用");
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