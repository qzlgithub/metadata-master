var form, message, table, main_table;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message'], function() {
    var app = layui.app;
    app.set({type: 'iframe'}).init();
    message = layui.message;
    form = layui.form;
    table = layui.table;
    getDateList();
});
$(function() {
    $(".item").click(function() {
        $(".item").removeClass("active");
        $(this).addClass("active");
    });
});
var isSubmit = false;

function saveNotice() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    var remark = $.trim($("#remark").val());
    if(remark == '') {
        layer.msg("备注不能为空！", {
            time: 2000
        });
        return;
    }
    var remindId = $("#remind-id").val();
    $.ajax({
        type: "POST",
        url: "/client/remind",
        data: {"id": remindId, "remark": remark},
        success: function(res) {
            if(res.code === '000000') {
                layer.msg("保存成功", {
                    time: 1000
                }, function() {
                    layer.closeAll();
                    window.location.reload();
                });
            }
            else {
                layer.msg("保存失败", {
                    time: 2000
                }, function() {
                    layer.closeAll();
                });
                isSubmit = false;
            }
        }
    });
}

function showNotice(id) {
    $("#remind-id").val(id);
    $("#remark").val("");
    layer.open({
        title: false,
        type: 1,
        content: $('#notice-warp'),
        area: ['500px'],
        shadeClose: true
    });
}

function getDateList() {
    $("#tip").hide();
    main_table = table.render({
        elem: '#dataTable',
        page: false,
        url: '/home/list/date',
        cols: [[
            {field: 'corpName', title: '公司名称', width: '20%'},
            {field: 'linkName', title: '联系人', width: '15%'},
            {field: 'linkPhone', title: '联系方式', width: '10%'},
            {field: 'productName', title: '产品名称', width: '15%'},
            {field: 'day', title: '剩余天数', width: '15%'},
            {title: '状态', width: '10%', templet: "#status-tpl"},
            {title: '操作', align: 'center', toolbar: '#operation-bar', fixed: 'right'}
        ]],
        response: {
            statusName: 'code',
            statusCode: '000000',
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        },
        done: function(res, curr, count) {
            var total = res.total;
            if(total - 5 > 0) {
                $("#more-remind").html("还有" + (total - 5) + "个未查看，去查看>>");
            }
        }
    });
}

function getTimeList() {
    $("#tip").show();
    main_table = table.render({
        elem: '#dataTable',
        page: false,
        url: '/home/list/time',
        cols: [[
            {field: 'corpName', title: '公司名称', width: '20%'},
            {field: 'linkName', title: '联系人', width: '20%'},
            {field: 'linkPhone', title: '联系方式', width: '15%'},
            {field: 'productName', title: '产品名称', width: '20%'},
            {title: '状态', width: '10%', templet: "#status-tpl"},
            {title: '操作', align: 'center', toolbar: '#operation-bar', fixed: 'right'}
        ]],
        response: {
            statusName: 'code',
            statusCode: '000000',
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        },
        done: function(res, curr, count) {
            var total = res.total;
            if(total - 5 > 0) {
                $("#more-remind").html("还有" + (total - 5) + "个未通知，去查看>>");
            }
        }
    });
}