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
        url: '/stats/client/clientList',
        where: {
            scopeType: $("#scopeType").val()
        },
        cols: [[
            {field: 'registerDate', title: '时间'},
            {field: 'corpName', title: '公司名称'},
            {field: 'shortName', title: '公司简称'},
            {field: 'username', title: '账号'},
            {field: 'managerName', title: '商务经理'},
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
            $('#chartTitleId').text(result.title);
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                roleId: params["roleId"],
                enabled: params['enabled']
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
    $.get('/stats/client/clientListJson',
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
                    zoomOnMouseWheel:false
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
            scopeType: $("#scopeType").val()
        }
        , page: {
            curr: 1
        }
    });
});

function clientOutPrint() {
    var scopeType = $('#scopeLiId').find('.active').attr('otherVal');
    location.href = '/exp/stats/client?scopeType=' + scopeType;
}



