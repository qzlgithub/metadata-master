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
        url: '/stats/client/revenueList',
        where: {
            scopeType: $("#scopeType").val()
        },
        cols: [[
            {field: 'corpName', title: '公司全称'},
            {field: 'shortName', title: '公司简称'},
            {field: 'username', title: '公司账号'},
            {field: 'product', title: '产品服务'},
            {field: 'requestNumber', title: '请求次数'},
            {field: 'fee', title: '利润（元）'}
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
        },
        done: function(res, curr, count) {
            var result = res.data;
            $('#chartTitleId').text(result.totalFee);
        }
    });
    form.on('submit(search)', function(data) {
        showChart();
        var params = data.field;
        main_table.reload({
            where: {
                scopeType: params["scopeType"]
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
    $.get('/stats/client/revenueListJson',
        {"scopeType": scopeType},
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
                    type: 'bar',
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
            scopeType: $("#scopeType").val()
        }
        , page: {
            curr: 1
        }
    });
});

function clientOutPrint() {
    var scopeType = $('#scopeLiId').find('.active').attr('otherVal');
    location.href = '/exp/stats/revenue?scopeType=' + scopeType;
}



