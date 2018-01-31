var message;
layui.config({
    base: '../../build/js/'
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
});
$(function() {
    productListInit();
});

function productListInit(){
    var obj = {
        pageNum: 1,
        pageSize: 10
    };
    getProductList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getProductList(obj);
            }
        })
    });
}

var rowTr
    =
    '<tr><td>#{code}</td><td>#{type}</td><td>#{name}</td><td id="enabledCheck#{id}">#{enabled}</td><td>#{remark}</td><td><span class="mr30"><a href="/product/edit.html?id=#{id}" class="edit">编辑</a></span> <a href="#" id="enabledTxt#{id}" onclick="changeStatus(\'#{id}\');" class="del">#{enabledTxt}</a><input type="hidden" id="enabled#{id}" value="#{enabledVal}"/></td></tr>';

function getProductList(obj, pageFun) {
    $.get(
        "/product/product/management",
        {
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize']
        },
        function(data) {
            var list = data.list;
            var total = data.total;
            var pages = data.pages;
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
            }
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
        });
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
