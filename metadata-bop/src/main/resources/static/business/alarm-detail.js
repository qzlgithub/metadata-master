var table, layer;
layui.config({
    base: '../../static/build/js/'
}).use(['laydate', 'laypage', 'layer', 'table'], function() {
    var laydate = layui.laydate; //日期
    table = layui.table;//表格
    getWarningDetailList();
    $('#addclass').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#add-manage'),
            area: ['500px'],
            shadeClose: true
        });
    });
});

function getWarningDetailList() {
    var warningType = $("#warningType").val();
    var colsItem = [];
    if(warningType == 1) {
        colsItem.push({field: 'errorName', title: '异常类型'});
        colsItem.push({field: 'corpName', title: '客户'});
        colsItem.push({field: 'username', title: '用户帐号'});
        colsItem.push({field: 'host', title: '请求ip'});
        colsItem.push({field: 'warningAt', title: '发生时间'});
    }
    else if(warningType == 2) {
        colsItem.push({field: 'username', title: '用户帐号'});
        colsItem.push({field: 'host', title: '请求ip'});
        colsItem.push({field: 'warningAt', title: '发生时间'});
    }
    else {
    }
    table.render({
        elem: '#data-table',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/alarm/detail/list',
        where: {
            id: $("#manageId").val()
        },
        cols: [colsItem],
        request: {
            pageName: 'pageNum', limitName: 'pageSize'
        },
        response: {
            statusName: 'code',
            statusCode: '000000',
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        },
        done: function(res) {
            var total = res.total;
            $("#total").html("共" + total + "条");
        }
    });
}

var isSubmit = false;

function doDispose() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    var id = $("#manageId").val();
    var remark = $.trim($("#remark").val());
    if(remark == "") {
        layer.msg("备注说明不能为空！");
        isSubmit = false;
        return;
    }
    $.ajax({
        type: "POST",
        url: "/alarm/dispose",
        data: {"id": id, "remark": remark},
        success: function(res) {
            if(res.code !== "000000") {
                layer.msg("保存失败:" + res.message);
                isSubmit = false;
            }
            else {
                layer.closeAll();
                layer.msg("保存成功", {
                    time: 1000
                }, function() {
                    window.location.href = "/alarm/wait.html";
                });
            }
        }
    });
}