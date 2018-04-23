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
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/client/list',
        where: {
            enabled: $("#enabled").val(),
            keyword: $("#keyword").val(),
            industryId: $("#industryId").val(),
            parentIndustryId: $("#parentIndustryId").val(),
            managerId: $("#manager").val()
        },
        cols: [[
            {type: 'checkbox', fixed: 'left', width: '5%'},
            {field: 'username', title: '用户名', width: '8%'},
            {field: 'corpName', title: '公司全称', width: '15%'},
            {field: 'shortName', title: '公司简称', width: '12%'},
            {field: 'industry', title: '行业', width: '15%'},
            {field: 'managerName', title: '商务经理'},
            {title: '子账号个数', sort: true, width: '8%', toolbar: '#sub-user-bar'},
            {field: 'registerDate', title: '注册日期', sort: true, width: '10%'},
            {title: '状态', width: '7%', templet: "#status-tpl"},
            {title: '操作', align: 'center', toolbar: '#operation-bar', fixed: 'right', width: '10.3%'}
        ]],
        request: {
            pageName: 'pageNum', limitName: 'pageSize'
        },
        response: {
            statusName: 'code',
            statusCode: '000000',
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                enabled: params['enabled'],
                keyword: params['keyword'],
                industryId: params['industry'],
                parentIndustryId: params['parent-industry'],
                managerId: params['manager-id']
            },
            page: {
                curr: 1
            }
        });
    });
    form.on('select(parent-industry)', function(obj) {
        var parent_industry_id = obj.value;
        var target = $("#industry");
        if(parent_industry_id !== "") {
            $.get(
                "/m/dict/sub-industry",
                {"parentId": parent_industry_id},
                function(data) {
                    target.empty();
                    target.append(new Option('全部', ''));
                    for(var d in data) {
                        target.append(new Option(data[d].name, data[d].id));
                    }
                    form.render('select');
                }
            )
        }
        else {
            target.empty();
            target.append(new Option('全部', ''));
            form.render('select');
        }
    });
});

function check_reason() {
    var reason = $("#ban-reason").val();
    var tip = $("#tip-reason");
    if(reason === '') {
        tip.text("原因不能为空");
        tip.show();
        return false;
    }
    else {
        tip.hide();
        return true;
    }
}

function change_status(status) {
    var selected = table.checkStatus('data-table');
    var rows = selected.data;
    if(rows.length === 0) {
        layer.msg("请至少选择一个客户", {time: 1000});
        return;
    }
    if(status === 1) {
        $("#ban-title").text("解冻账号");
        $("#ban-status").val(1);
    }
    else {
        $("#ban-title").text("冻结账号");
        $("#ban-status").val(0);
    }
    layer.open({
        title: false,
        type: 1,
        content: $('#ban-div'),
        area: ['700px'],
        shadeClose: true
    });
}

/**
 * 变更客户状态
 */
function post_client_status() {
    if(!check_reason()) {
        return;
    }
    var status = $("#ban-status").val();
    var selected = table.checkStatus('data-table');
    var rows = selected.data;
    if(rows.length > 0) {
        var to_deal_client = [];
        for(var i in rows) {
            to_deal_client.push(rows[i].id);
        }
        $.ajax({
            type: "POST",
            url: "/client/status",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({"id": to_deal_client, "status": status, "reason": $("#ban-reason").val()}),
            success: function(res) {
                if(res.code === '000000') {
                    layer.closeAll();
                    $("#ban-reason").val('');
                    $("#ban-status").val('');
                    main_table.reload();
                }
                else {
                    layer.msg("操作失败：" + res.message, {time: 2000});
                }
            }
        });
    }
    else {
        layer.closeAll();
    }
}

/**
 * 删除客户
 */
function del_client() {
    var selected = table.checkStatus('data-table');
    var rows = selected.data;
    if(rows.length === 0) {
        layer.msg("请至少选择一个客户", {time: 1000});
        return;
    }
    layer.confirm('是否确定停用所选定的客户？', {
        btn: ['确定', '取消'],
        yes: function() {
            var to_deal_client = [];
            for(var i in rows) {
                to_deal_client.push(rows[i].id);
            }
            $.ajax({
                type: "DELETE",
                url: "/client/deletion",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": to_deal_client}),
                success: function(res) {
                    if(res.code === '000000') {
                        main_table.reload();
                    }
                    else {
                        layer.msg("操作失败:" + res.message, {time: 2000});
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

/**
 * 重置主账号密码
 */
function reset_password() {
    var selected = table.checkStatus('data-table');
    var rows = selected.data;
    if(rows.length === 0) {
        layer.msg("请至少选择一个客户", {time: 1000});
        return;
    }
    layer.confirm('是否确定重置选定客户的主账号密码？', {
        btn: ['确定', '取消'],
        yes: function() {
            var to_deal_client = [];
            for(var i in rows) {
                to_deal_client.push(rows[i].id);
            }
            $.ajax({
                type: "POST",
                url: "/client/reset",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({"id": to_deal_client}),
                success: function(res) {
                    if(res.code === '000000') {
                        layer.msg("主账号密码已重置", {time: 2000});
                    }
                    else {
                        layer.msg("操作失败:" + res.message, {time: 2000});
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

/**
 * 显示指定客户的子账号列表
 */
function show_sub_user(id) {
    var obj = {
        clientId: id,
        pageNum: 1,
        pageSize: 5
    };
    getSubUserList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getSubUserList(pageObj);
            }
        })
    }, function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#child-account'),
            area: ['700px'],
            shadeClose: true
        });
    });
}

function getSubUserList(obj, pageFun, openLayerFun) {
    $.get(
        "/client/sub-user/list",
        {"id": obj['clientId'], "pageNum": obj['pageNum'], "pageSize": obj['pageSize']},
        function(data) {
            var htmlobj = $("#child-account-body");
            htmlobj.empty();
            var sub_acct_tr =
                '<tr><td>#{username}</td><td>#{name}</td><td>#{phone}</td>' +
                '<td><a href="/client/consumption.html?c=' + obj['clientId'] + '&u=#{id}" class="edit">查看消费</a></td></tr>';
            var total = data.total;
            var pages = Math.floor((total - 1) / parseInt(obj['pageSize'])) + 1;
            var list = data.list;
            for(var d in list) {
                var tr = sub_acct_tr.replace(/#{id}/g, list[d].id).replace(/#{username}/g, list[d].username)
                .replace(/#{name}/g, list[d].name).replace(/#{phone}/g, list[d].phone);
                htmlobj.append(tr);
            }
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
            if(typeof openLayerFun === 'function') {
                openLayerFun();
            }
        }
    );
}