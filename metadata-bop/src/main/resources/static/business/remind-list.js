var form, message, table, main_table;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message', 'laydate'], function() {
    var app = layui.app;
    app.set({type: 'iframe'}).init();
    message = layui.message;
    form = layui.form;
    table = layui.table;
    var laydate = layui.laydate;
    laydate.render({
        elem: '#dates',
        range: true,
        done: function(value, date) {
            if(value != "") {
                var dates = value.split(" - ");
                $("#fromDate").val(dates[0]);
                $("#toDate").val(dates[1]);
            }
            else {
                $("#fromDate").val("");
                $("#toDate").val("");
            }
        }
    });
    $(".item").click(function() {
        $(".item").removeClass("active");
        $(this).addClass("active");
    });
    getDateList();
});

function getDateList() {
    $("#tip").hide();
    main_table = table.render({
        elem: '#dataTable',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/remind/list/date',
        id:'table-main',
        where: {
            keyword: $.trim($("#keyword").val()),
            dispose: $("#dispose").val()
        },
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
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                keyword: params['keyword'],
                dispose: params['dispose']
            },
            page: {
                curr: 1
            }
        });
    });
}

function getTimeList() {
    $("#tip").show();
    main_table = table.render({
        elem: '#dataTable',
        page: true,
        limit: 10,
        limits: [10, 15, 30, 50],
        url: '/remind/list/time',
        id:'table-main',
        where: {
            keyword: $.trim($("#keyword").val()),
            dispose: $("#dispose").val()
        },
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
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                keyword: params['keyword'],
                dispose: params['dispose']
            },
            page: {
                curr: 1
            }
        });
    });
}

function showNotice(id) {
    $("#remindId").val(id);
    $("#remark").val("");
    layer.open({
        title: false,
        type: 1,
        content: $('#notice-warp'),
        area: ['500px'],
        shadeClose: true
    });
}

var isSubmit = false;

function saveRemark() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    var remark = $.trim($("#remark").val());
    if(remark == '') {
        layer.msg("备注不能为空！", {
            time: 2000
        });
        isSubmit = false;
        return;
    }
    var remindId = $("#remindId").val();
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
                    table.reload('table-main');
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