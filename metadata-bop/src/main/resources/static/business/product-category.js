var message;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    $('#productclass').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#product-class'),
            area: ['500px'],
            shadeClose: true
        });
    });
    /* $('.editclass').on('click', function() {
         layer.open({
             title: false,
             type: 1,
             content: $('#edit-class'),
             area: ['500px'],
             shadeClose: true
         });
     });*/
});
var $ = layui.jquery;
function showEditDiv() {
    layer.open({
        title: false,
        type: 1,
        content: $('#edit-class'),
        area: ['500px'],
        shadeClose: true
    });
}

$(function(){
    productCategoryListInit();
});

function productCategoryListInit(){
    var obj = {
        pageNum: 1,
        pageSize: 10
    };
    getProductCategoryList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getProductCategoryList(obj);
            }
        })
    });
}

var rowTr =
    //'<tr><td>#{id}</td><td>#{name}</td><td>#{remark}</td><td><span class="mr30"><a href="#" class="edit">编辑</a></span><a href="#" class="del">停用</a></td></tr>';
    '<tr><td>#{code}</td><td>#{name}</td><td>#{remark}</td><td><span class="mr30 edit cp editclass" id="editclass#{id}" onclick="findProductCategeoryById(\'#{id}\');">编辑</span><a href="#" id="enabledTxt#{id}" onclick="updateStatus(#{id});" class="del">#{enabledTxt}</a><input type="hidden" id="enabled#{id}" value="#{enabled}"/></td></tr>';

function getProductCategoryList(obj, pageFun) {
    $.get(
        "/product/productCategory/list",
        {"pageNum": obj['pageNum'], "pageSize": obj['pageSize']},
        function(data) {
            var list = data.list;
            var total = data.total;
            var pages = data.pages;
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
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
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
                    window.location.href = "/setting/product/category.html";
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
                    window.location.href = "/setting/product/category.html";
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