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
        url: '/stats/client/requestList',
        where: {
            scopeType: $("#scopeType").val(),
            name: $.trim($("#name").val()),
            productId: $("#product").val()
        },
        cols: [[
            {field: 'requestAt', title: '请求时间'},
            {field: 'tradeNo', title: '消费单号'},
            {field: 'corpName', title: '公司全称'},
            {field: 'shortName', title: '公司简称'},
            {field: 'username', title: '公司账号'},
            {field: 'product', title: '产品服务'},
            {field: 'billPlan', title: '计费方式'},
            {title: '是否击中', templet: "#hittpl"},
            {field: 'fee', title: '利润（元）'}
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
        },
        done: function(res, curr, count) {
            var result = res.data;
            $('#chartTitleId').text(result.title);
            $('#miss-number').text(result.missCount);
            $('#query-number').text(count);
        }
    });
    form.on('submit(search)', function(data) {
        showChart();
        var params = data.field;
        main_table.reload({
            where: {
                scopeType: params["scopeType"],
                name: $.trim(params["name"]),
                productId: params["product"]
            },
            page: {
                curr: 1
            }
        });
    });
});
//点击展开
$(document).ready(function() {
    $(".btn-slide").click(function() {
        $("#panel").slideToggle("slow");
        $(this).toggleClass("active");
        return false;
    });
});
$(function() {
    showChart();
});

function showChart() {
    var myChart = echarts.init(document.getElementById('panel'));
    var scopeType = $('#scopeLiId').find('.active').attr('otherVal');
    var name = $.trim($("#name").val());
    var productId = $("#product").val();
    $.get('/stats/client/requestListJson',
        {"scopeType": scopeType, "name": name, "productId": productId},
        function(data) {
            var data = eval("(" + data + ")");
            myChart.setOption(option = {
                tooltip: {
                    trigger: 'axis'
                },
                xAxis: {
                    data: data.map(function(item) {
                        return item[0];
                    })
                },
                yAxis: {
                    splitLine: {
                        show: false
                    }
                },
                toolbox: {
                    left: 'center',
                    feature: {
                        dataZoom: {
                            yAxisIndex: 'none'
                        },
                        restore: {},
                        saveAsImage: {}
                    }
                },
                dataZoom: [{
                    startValue: data[0][0]
                }, {
                    type: 'inside',
                    zoomOnMouseWheel: false
                }],
                series: {
                    type: 'line',
                    data: data.map(function(item) {
                        return item[1];
                    })
                }
            });
        });
}

$('#scopeLiId span').click(function() {
    $('#scopeLiId span').each(function() {
        $(this).removeClass('active');
    });
    $(this).addClass('active');
    var scopeType = $('#scopeLiId').find('.active').attr('otherVal');
    $("#scopeType").val(scopeType);
    showChart();
    main_table.reload({
        where: {
            scopeType: $("#scopeType").val(),
            name: $.trim($("#name").val()),
            productId: $("#product").val()
        }
        , page: {
            curr: 1
        }
    });
});

function clientOutPrint() {
    var scopeType = $('#scopeLiId').find('.active').attr('otherVal');
    var name = $.trim($("#name").val());
    var productId = $("#product").val();
    location.href = '/exp/stats/request?scopeType=' + scopeType + '&name=' + name + '&productId=' + productId;
}



