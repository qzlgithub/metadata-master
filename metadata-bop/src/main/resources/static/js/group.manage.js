$(function() {
    roleListInit();
});

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
    $('#pay').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: '<img src="../../static/build/images/pay.png" />',
            area: ['500px', '250px'],
            shadeClose: true
        });
    });
});

function roleListInit(){
    var obj = {
        pageNum: 1,
        pageSize: 10
    };
    getRoleList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getRechargeList(obj);
            }
        })
    });
}

var rowStr =
    '<tr><td>#{id}</td><td>#{name}</td><td>#{privilege}</td><td><span class="mr30"><a href="/role/edit.html?id=#{id}" class="edit">编辑</a></span><a href="#" class="del" id="enabled#{id}" onclick="changeStatus(\'#{id}\')">#{enabled}</a></td></tr>';

function getRoleList(obj, pageFun) {
    $.get(
        "/role/list",
        {
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize']
        },
        function(data) {
            var list = data.list;
            var total = data.total;
            var pages = data.pages;
            $("#dataBody").empty();
            for(var d in list) {
                var row = rowStr.replace(/#{id}/g, list[d].id).replace("#{name}", list[d].name)
                .replace("#{privilege}", list[d].privilege)
                .replace("#{enabled}", list[d].enabled === 1 ? "禁用" : "启用");
                $("#dataBody").append(row);
            }
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
        }
    );
}

function changeStatus(id) {
    var txt=$("#enabled" + id).text();
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
                            layer.msg("启用成功",{
                                time: 2000
                            });
                        }
                        else {
                            $("#enabled" + id).text("启用");
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