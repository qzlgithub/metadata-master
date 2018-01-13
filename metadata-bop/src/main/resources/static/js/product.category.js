function showEditDiv() {
    layer.open({
        title: false,
        type: 1,
        content: $('#edit-class'),
        area: ['500px'],
        shadeClose: true
    });
}

$(function() {
    productCategoryList(1, $("#pageSize").val());
});

function refreshPageSize() {
    productCategoryList(1, $("#pageSize").val());
}

//<![CDATA[
function gotoPrev() {
    var pageNum = $("#pageNum").val();
    if(pageNum > 1) {
        productCategoryList(parseInt(pageNum) - 1, $("#pageSize").val());
    }
}

function gotoNext() {
    var pageNum = $("#pageNum").val();
    var totalPage = $("#pages").val();
    if(parseInt(pageNum) < parseInt(totalPage)) {
        productCategoryList(parseInt(pageNum) + 1, $("#pageSize").val());
    }
}

function gotoPage() {
    var customPageNum = $("#customPageNum").val();
    var pages = $("#pages").val();
    if(parseInt(customPageNum) > parseInt(pages)) {
        customPageNum = pages;
    }
    productCategoryList(customPageNum, $("#pageSize").val());
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
    productCategoryList(pageNumVal, pageSizeVal);
}

//]]>
var rowTr =
    //'<tr><td>#{id}</td><td>#{name}</td><td>#{remark}</td><td><span class="mr30"><a href="#" class="edit">编辑</a></span><a href="#" class="del">停用</a></td></tr>';
    '<tr><td>#{code}</td><td>#{name}</td><td>#{remark}</td><td><span class="mr30 edit cp editclass" id="editclass#{id}" onclick="findProductCategeoryById(\'#{id}\');">编辑</span><a href="#" id="enabledTxt#{id}" onclick="updateStatus(#{id});" class="del">#{enabledTxt}</a><input type="hidden" id="enabled#{id}" value="#{enabled}"/></td></tr>';

function productCategoryList(pageNum, pageSize) {
    $.get(
        "/product/productCategory/list",
        {"pageNum": pageNum, "pageSize": pageSize},
        function(data) {
            var list = data.list;
            $("#dataBody").empty();
            for(var i in list) {
                //alert(list[i].name);
                var row = rowTr.replace(/#{id}/g, list[i].id).replace("#{code}", list[i].code).replace("#{name}",
                    list[i].name)
                .replace("#{remark}", list[i].remark)
                .replace("#{enabled}", list[i].enabled)
                .replace("#{enabledTxt}", list[i].enabled === 1 ? "禁用" : "启用");
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
    )
}

/*添加产品类别*/
function addProductCategory() {
    var param = {
        "code": $("#productCode").val(),
        "name": $("#productName").val(),
        "remark": $("#productRemark").val()
    };
    $.ajax({
        type: "post",
        url: "/product/categoryAdd",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(param),
        success: function(data) {
            if(data.errCode !== '000000') {
                layer.msg(data.errMsg, {time: 2000});
            }
            else {
                layer.msg("添加成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/product/category/index.html";
                });
            }
        }
    });
}

/*初始化产品类别*/
function findProductCategeoryById(id) {
    var url = "/product/initProductCategory";
    // alert(id);
    var param = {
        "id": id
    };
    $.get(url, param, function(data) {
        $("#editId").val(data.id);
        $("#productCodeU").val(data.code);
        $("#productNameU").val(data.name);
        $("#productRemarkU").val(data.remark);
        showEditDiv();
    });
}

/*产品类别编辑*/
function updateProductCategory() {
    $.ajax({
        type: "POST",
        url: "/product/categoryUpdate",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "id": $("#editId").val(),
            "code": $("#productCodeU").val(),
            "name": $("#productNameU").val(),
            "remark": $("#productRemarkU").val()
        }),
        success: function(data) {
            if(data.errCode !== '000000') {
                layer.msg(data.errMsg, {time: 2000});
            }
            else {
                layer.msg("修改成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/product/category/index.html";
                });

            }
        }
    });
}

function updateStatus(id) {
    var enabled = $("#enabled" + id).val();
    var txt = ($("#enabledTxt" + id).text());
    layer.confirm('是否确定' + txt + '？', {
        btn: ['确定', '取消'],
        //Align: 'c',
        yes: function() {
            $(this).click();
            $.ajax({
                type: "post",
                url: "/product/updateCateStatus",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": id, "enabled": enabled}),
                success: function(data) {
                    if(data.errCode === '000000') {
                        if(enabled === "0") {
                            $("#enabledTxt" + id).text("禁用");
                            $("#enabled" + id).val(1);
                            layer.msg("启用成功",{
                                time: 2000
                            });
                        }
                        else {
                            $("#enabledTxt" + id).text("启用");
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