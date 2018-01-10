$(function() {
    getProductList(1, $("#pageSize").val());
});
var rowTr
    =
    '<tr><td>#{code}</td><td>#{type}</td><td>#{name}</td><td id="enabledCheck#{id}">#{enabled}</td><td>#{remark}</td><td><span class="mr30"><a href="/product/edit.html?id=#{id}" class="edit">编辑</a></span> <a href="#" id="enabledTxt#{id}" onclick="changeStatus(\'#{id}\');" class="del">#{enabledTxt}</a><input type="hidden" id="enabled#{id}" value="#{enabledVal}"/></td></tr>';

function getProductList(pageNum, pageSize) {
    $.get(
        "/product/product/management",
        {
            "pageNum": pageNum,
            "pageSize": pageSize
        },
        function(data) {
            var list = data.list;
            $("#dataBody").empty();
            for(var i in list) {
                var row = rowTr.replace("#{code}", list[i].code)
                .replace("#{enabledVal}", list[i].enabled)
                .replace(/#{id}/g, list[i].id)
                .replace("#{type}", list[i].typeName)
                .replace("#{name}", list[i].name)
                .replace(/#{enabled}/g, list[i].enabled == 1 ? "否" : "是")
                .replace("#{remark}", list[i].remark)
                .replace("#{enabledTxt}", list[i].enabled == 1 ? "禁用" : "启用");
                $("#dataBody").append(row);
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
        });
}
function refreshPageSize() {
    getProductList(1, $("#pageSize").val());
}
//<![CDATA[
function gotoPrev() {
    var pageNum = $("#pageNum").val();
    if(pageNum > 1) {
        getProductList(parseInt(pageNum) - 1, $("#pageSize").val());
    }
}

function gotoNext() {
    var pageNum = $("#pageNum").val();
    var totalPage = $("#pages").val();
    if(parseInt(pageNum) < parseInt(totalPage)) {
        getProductList(parseInt(pageNum) + 1, $("#pageSize").val());
    }
}

function gotoPage() {
    var prodPageNum = $("#prodPageNum").val();
    var pages = $("#pages").val();
    if(parseInt(prodPageNum) > parseInt(pages)) {
        prodPageNum = pages;
    }
    getProductList(prodPageNum, $("#pageSize").val());
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

//]]>
function goPage(pageNum) {
    var pageNumVal = pageNum;
    var pageSizeVal = $("#pageSize").val();
    getProductList(pageNumVal, pageSizeVal);
}

function changeStatus(id) {
    //alert(id);
    var enabled = $("#enabled" + id).val();
    var txt = ($("#enabledTxt" + id).text());
    layer.confirm('是否确定' + txt + '？', {
        btn: ['确定', '取消'],
        //Align: 'c',
        yes: function() {
            $(this).click();
            $.ajax({
                type: "post",
                url: "/product/updateProdStatus",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": id, "enabled": enabled}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        if(enabled === "0") {

                            $("#enabledTxt" + id).text("禁用");
                            $("#enabledCheck" + id).text("否");
                            $("#enabled" + id).val(1);
                            layer.msg("启用成功",{
                                time: 2000
                            });
                        }
                        else {
                            $(this).click();
                            $("#enabledTxt" + id).text("启用");
                            $("#enabledCheck" + id).text("是");
                            $("#enabled" + id).val(0);
                            layer.msg("禁用成功",{
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
