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
        url: '/stats/client/rechargeList',
        where: {
            scopeType: $("#scopeType").val()
        },
        cols: [[
            {field: 'tradeTime', title: '充值时间'},
            {field: 'tradeNo', title: '充值单号'},
            {field: 'corpName', title: '公司名称'},
            {field: 'shortName', title: '公司简称'},
            {field: 'username', title: '账号'},
            {field: 'productName', title: '产品'},
            {field: 'amount', title: '金额（元）'},
            {field: 'rechargeType', title: '充值类型'},
            {field: 'balance', title: '账户余额（不包含服务）'},
            {field: 'managerName', title: '经手人'},
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
    var leftChart = echarts.init(document.getElementById('leftPanel'));
    var rightChart = echarts.init(document.getElementById('rightPanel'));
    var scopeType = $('#scopeLiId').find('.active').attr('otherVal');
    $.get('/stats/client/rechargeListJson',
        {"scopeType": scopeType},
        function(data) {
            var jsonObj = JSON.parse(data);
            leftChart.setOption({
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data: jsonObj.leftJson.legendData
                },
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '30',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data: jsonObj.leftJson.data
                    }
                ]
            });
            rightChart.setOption({
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow',
                        label: {
                            show: true
                        }
                    }
                },
                toolbox: {
                    show: true,
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                calculable: true,
                legend: {
                    data: jsonObj.leftJson.legendData,
                    itemGap: 5
                },
                grid: {
                    top: '12%',
                    left: '1%',
                    right: '10%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'category',
                        data: jsonObj.rightJson.names
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '',
                        axisLabel: {
                            formatter: function(a) {
                                a = +a;
                                return isFinite(a)
                                    ? echarts.format.addCommas(+a / 1000)
                                    : '';
                            }
                        }
                    }
                ],
                dataZoom: [
                    {
                        show: true,
                        start: 94,
                        end: 100
                    },
                    {
                        type: 'inside',
                        start: 94,
                        end: 100
                    },
                    {
                        show: true,
                        yAxisIndex: 0,
                        filterMode: 'empty',
                        width: 30,
                        height: '80%',
                        showDataShadow: false,
                        left: '93%'
                    }
                ],
                series: jsonObj.rightJson.series
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
    location.href = '/exp/stats/recharge?scopeType=' + scopeType;
}



