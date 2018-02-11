var form, message, table, main_table;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message'], function() {
    var app = layui.app;
    app.set({type: 'iframe'}).init();
    message = layui.message;
    form = layui.form;
    table = layui.table;
    main_table = table.render({
        elem: '#data-table',
        page: true,
        limit: 3,
        limits: [3, 15, 30, 50],
        url: '/role/list',
        cols: [[
            {field: 'id', title: '编号', width: '15%'},
            {field: 'name', title: '分组名称', width: '15%'},
            {field: 'privilege', title: '权限类型'},
            {title: '操作', align: 'center', toolbar: '#operation-bar', fixed: 'right', width: '15%'}
        ]],
        request: {
            pageName: 'pageNum', limitName: 'pageSize'
        },
        response: {
            statusName: 'code',
            statusCode: 0,
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        }
    });
});

//$(function() {
//    roleListInit();
//});
//
//function roleListInit(){
//    var obj = {
//        pageNum: 1,
//        pageSize: 10
//    };
//    getRoleList(obj, function(pageObj, pages, total) {
//        $('#pagination').paging({
//            initPageNo: pageObj['pageNum'],
//            totalPages: pages,
//            totalCount: '合计' + total + '条数据',
//            slideSpeed: 600,
//            jump: false,
//            callback: function(currentPage) {
//                pageObj['pageNum'] = currentPage;
//                getRechargeList(obj);
//            }
//        })
//    });
//}
//
//var rowStr =
//    '<tr><td>#{id}</td><td>#{name}</td><td>#{privilege}</td><td><span class="mr30"><a href="/role/edit.html?id=#{id}" class="edit">编辑</a></span><a href="#" class="del" id="enabled#{id}" onclick="changeStatus(\'#{id}\')">#{enabled}</a></td></tr>';
//
//function getRoleList(obj, pageFun) {
//    $.get(
//        "/role/list",
//        {
//            "pageNum": obj['pageNum'],
//            "pageSize": obj['pageSize']
//        },
//        function(data) {
//            var list = data.list;
//            var total = data.total;
//            var pages = data.pages;
//            $("#dataBody").empty();
//            for(var d in list) {
//                var row = rowStr.replace(/#{id}/g, list[d].id).replace("#{name}", list[d].name)
//                .replace("#{privilege}", list[d].privilege)
//                .replace("#{enabled}", list[d].enabled === 1 ? "禁用" : "启用");
//                $("#dataBody").append(row);
//            }
//            if(typeof pageFun === 'function') {
//                pageFun(obj, pages, total);
//            }
//        }
//    );
//}

function changeStatus(id) {
    layer.confirm('是否确定？', {
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
                            layer.msg("启用成功",{
                                time: 2000
                            });
                        }
                        else {
                            layer.msg("禁用成功",{
                                time: 2000
                            });
                        }
                        main_table.reload();
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