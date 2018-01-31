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
});
$(function() {
    managerListInit();
});

function managerListInit(){
    var obj = {
        pageNum: 1,
        pageSize: 10,
        roleId: $("#roleId").val(),
        enabled: $("#enabled").val()
    };
    getManagerList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getManagerList(obj);
            }
        })
    });
}

var rowStr =
    '<tr><td>#{id}</td><td>#{username}</td><td>#{name}</td><td>#{phone}</td><td>#{roleName}</td><td id="enabled#{id}">#{enabled}</td><td>#{registerDate}</td><td><span class="mr30"><a href="/manager/edit.html?id=#{id}" class="edit">编辑</a></span><a href="#" id="statusAction#{id}" class="del" onclick="changeStatus(\'#{id}\')">#{statusAction}</a></td></tr>';

function getManagerList(obj, pageFun) {
    $.get(
        "/manager/list",
        {
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize'],
            "roleId": obj['roleId'],
            "enabled": obj['enabled']
        },
        function(data) {
            var list = data.list;
            var total = data.total;
            var pages = data.pages;
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
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
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
                            layer.msg("启用成功", {
                                time: 2000
                            });
                        }
                        else {
                            $("#statusAction" + id).text("启用");
                            $("#enabled" + id).text("已禁用");
                            layer.msg("禁用成功", {
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