layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'laydate'], function() {
    var app = layui.app, form = layui.form, laydate = layui.laydate,
        $ = layui.jquery
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    laydate.render({
        elem: '#dates'
        , range: true,
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
    laydate.render({
        elem: '#contrastDate'
    });
    //对比时间
    $(document).ready(function() {
        $(".contrast-time2").change(function() {
            $(".time-input2").toggle();
        });
    });
    $("#selectType").on('click','span',function(){
        $("#selectType span").removeClass('active');
        $(this).addClass('active');
        chartBar();
    });
    initChart();
});

function initChart() {
    chartPie();
    chartBar();
}

var chartPieObj = echarts.init(document.getElementById('charPie'), 'dark');
var chartBarObj = echarts.init(document.getElementById('charBar'), 'dark');

function chartPie() {
    var scope = $("#selectType").find(".active").attr("typeData");
    var fromDate = $("#fromDate").val();
    var toDate = $("#toDate").val();
    var contrastDate = null;
    if($("#selectContrast").is(":checked")){
        contrastDate = $("#contrastDate").val();
    }

    var option = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            x: 'left',
            data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
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
                data: [
                    {value: 335, name: '直接访问'},
                    {value: 310, name: '邮件营销'},
                    {value: 234, name: '联盟广告'},
                    {value: 135, name: '视频广告'},
                    {value: 1548, name: '搜索引擎'}
                ]
            }
        ]
    };
    chartPieObj.setOption(option);
}

function chartBar() {
    var scope = $("#selectType").find(".active").attr("typeData");
    var fromDate = $("#fromDate").val();
    var toDate = $("#toDate").val();
    var contrastDate = null;
    if($("#selectContrast").is(":checked")){
        contrastDate = $("#contrastDate").val();
    }
    $.ajax({
        type: "post",
        url: "/diagram/recharge/bar",
        data: JSON.stringify({
            "scope": scope,
            "startDate": fromDate,
            "endDate": toDate,
            "compareFrom": contrastDate
        }),
        contentType: "application/json",
        success: function(res) {
            if(res.code !== '000000') {
                return;
            }
            chartBarObj.hideLoading();
            var obj = res.data;
            var list = obj.list;
            var legendData = [];
            var seriesData = [];
            for(var o in list) {
                legendData.push(list[o].name);
                var s = {type: 'bar'};
                s.name = list[o].name;
                s.stack = list[o].stack;
                s.data = list[o].data;
                seriesData.push(s);
            }
            chartBarObj.setOption({
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {type: 'shadow'}
                },
                legend: {data: legendData},
                yAxis: {type: 'value'},
                xAxis: {
                    type: 'category',
                    data: obj.xData
                },
                series: seriesData
            }, true);
        }
    });
    //var option = {
    //    tooltip: {
    //        trigger: 'axis',
    //        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
    //            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
    //        }
    //    },
    //    legend: {
    //        data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
    //    },
    //    grid: {
    //        left: '3%',
    //        right: '4%',
    //        bottom: '3%',
    //        containLabel: true
    //    },
    //    xAxis: {
    //        type: 'category',
    //        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    //    },
    //    yAxis: {
    //        type: 'value'
    //    },
    //    series: [
    //        {
    //            name: '直接访问',
    //            type: 'bar',
    //            stack: '总量',
    //            label: {
    //                normal: {
    //                    show: true,
    //                    position: 'insideRight'
    //                }
    //            },
    //            data: [320, 302, 301, 334, 390, 330, 320]
    //        },
    //        {
    //            name: '邮件营销',
    //            type: 'bar',
    //            stack: '总量',
    //            label: {
    //                normal: {
    //                    show: true,
    //                    position: 'insideRight'
    //                }
    //            },
    //            data: [120, 132, 101, 134, 90, 230, 210]
    //        },
    //        {
    //            name: '联盟广告',
    //            type: 'bar',
    //            stack: '总量',
    //            label: {
    //                normal: {
    //                    show: true,
    //                    position: 'insideRight'
    //                }
    //            },
    //            data: [220, 182, 191, 234, 290, 330, 310]
    //        },
    //        {
    //            name: '视频广告',
    //            type: 'bar',
    //            stack: '总量',
    //            label: {
    //                normal: {
    //                    show: true,
    //                    position: 'insideRight'
    //                }
    //            },
    //            data: [150, 212, 201, 154, 190, 330, 410]
    //        },
    //        {
    //            name: '搜索引擎',
    //            type: 'bar',
    //            stack: '总量',
    //            label: {
    //                normal: {
    //                    show: true,
    //                    position: 'insideRight'
    //                }
    //            },
    //            data: [820, 832, 901, 934, 1290, 1330, 1320]
    //        }
    //    ]
    //};
    //chartBarObj.setOption(option);
}

//var form, message, table, main_table;
//layui.config({
//    base: '../../static/build/js/'
//}).use(['app', 'form', 'table', 'message'], function() {
//    var app = layui.app;
//    app.set({type: 'iframe'}).init();
//    message = layui.message;
//    form = layui.form;
//    table = layui.table;
//    main_table = table.render({
//        elem: '#data-table',
//        page: true,
//        limit: 10,
//        limits: [10, 15, 30, 50],
//        url: '/stats/client/rechargeList',
//        where: {
//            scopeType: $("#scopeType").val()
//        },
//        cols: [[
//            {field: 'tradeTime', title: '充值时间'},
//            {field: 'tradeNo', title: '充值单号'},
//            {field: 'corpName', title: '公司名称'},
//            {field: 'shortName', title: '公司简称'},
//            {field: 'username', title: '账号'},
//            {field: 'productName', title: '产品'},
//            {field: 'amount', title: '金额（元）'},
//            {field: 'rechargeType', title: '充值类型'},
//            {field: 'balance', title: '账户余额（不包含服务）'},
//            {field: 'managerName', title: '经手人'}
//        ]],
//        request: {
//            pageName: 'pageNum', limitName: 'pageSize'
//        },
//        response: {
//            statusName: 'code',
//            statusCode: '000000',
//            msgName: 'message',
//            countName: 'total',
//            dataName: 'list'
//        },
//        done: function(res, curr, count) {
//            var result = res.data;
//            $('#chartTitleId').text(result.title);
//        }
//    });
//    form.on('submit(search)', function(data) {
//        var params = data.field;
//        main_table.reload({
//            where: {
//                roleId: params["roleId"],
//                enabled: params['enabled']
//            },
//            page: {
//                curr: 1
//            }
//        });
//    });
//});
////点击展开
//$(document).ready(function() {
//    $(".btn-slide").click(function() {
//        $("#panel").slideToggle("slow");
//        $(this).toggleClass("active");
//        return false;
//    });
//});
//$(function() {
//    showChart();
//});
//
//function showChart() {
//    var leftChart = echarts.init(document.getElementById('leftPanel'));
//    var rightChart = echarts.init(document.getElementById('rightPanel'));
//    var scopeType = $('#scopeLiId').find('.active').attr('otherVal');
//    $.get('/stats/client/rechargeListJson',
//        {"scopeType": scopeType},
//        function(data) {
//            var jsonObj = JSON.parse(data);
//            leftChart.setOption({
//                tooltip: {
//                    trigger: 'item',
//                    formatter: "{a} <br/>{b}: {c} ({d}%)"
//                },
//                legend: {
//                    orient: 'vertical',
//                    x: 'left',
//                    data: jsonObj.leftJson.legendData
//                },
//                series: [
//                    {
//                        name: '访问来源',
//                        type: 'pie',
//                        radius: ['50%', '70%'],
//                        avoidLabelOverlap: false,
//                        label: {
//                            normal: {
//                                show: false,
//                                position: 'center'
//                            },
//                            emphasis: {
//                                show: true,
//                                textStyle: {
//                                    fontSize: '30',
//                                    fontWeight: 'bold'
//                                }
//                            }
//                        },
//                        labelLine: {
//                            normal: {
//                                show: false
//                            }
//                        },
//                        data: jsonObj.leftJson.data
//                    }
//                ]
//            });
//            rightChart.setOption({
//                tooltip: {
//                    trigger: 'axis',
//                    axisPointer: {
//                        type: 'shadow',
//                        label: {
//                            show: true
//                        }
//                    }
//                },
//                toolbox: {
//                    show: true,
//                    feature: {
//                        mark: {show: true},
//                        dataView: {show: true, readOnly: false},
//                        magicType: {show: true, type: ['line', 'bar']},
//                        restore: {show: true},
//                        saveAsImage: {show: true}
//                    }
//                },
//                calculable: true,
//                legend: {
//                    data: jsonObj.leftJson.legendData,
//                    itemGap: 5
//                },
//                grid: {
//                    top: '12%',
//                    left: '1%',
//                    right: '10%',
//                    containLabel: true
//                },
//                xAxis: [
//                    {
//                        type: 'category',
//                        data: jsonObj.rightJson.names
//                    }
//                ],
//                yAxis: [
//                    {
//                        type: 'value',
//                        name: '',
//                        axisLabel: {
//                            formatter: function(a) {
//                                a = +a;
//                                return isFinite(a)
//                                    ? echarts.format.addCommas(+a / 1000)
//                                    : '';
//                            }
//                        }
//                    }
//                ],
//                dataZoom: [
//                    {
//                        show: true,
//                        start: 94,
//                        end: 100
//                    },
//                    {
//                        type: 'inside',
//                        start: 94,
//                        end: 100
//                    },
//                    {
//                        show: true,
//                        yAxisIndex: 0,
//                        filterMode: 'empty',
//                        width: 30,
//                        height: '80%',
//                        showDataShadow: false,
//                        left: '93%'
//                    }
//                ],
//                series: jsonObj.rightJson.series
//            });
//        });
//}
//
//$('#scopeLiId span').click(function() {
//    $('#scopeLiId span').each(function() {
//        $(this).removeClass('active');
//    });
//    $(this).addClass('active');
//    var scopeType = $('#scopeLiId').find('.active').attr('otherVal');
//    $("#scopeType").val(scopeType);
//    showChart();
//    main_table.reload({
//        where: {
//            scopeType: $("#scopeType").val()
//        }
//        , page: {
//            curr: 1
//        }
//    });
//});
//
//function clientOutPrint() {
//    var scopeType = $('#scopeLiId').find('.active').attr('otherVal');
//    location.href = '/exp/stats/recharge?scopeType=' + scopeType;
//}
//
//
//
