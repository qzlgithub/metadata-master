layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'laydate'], function() {
    var app = layui.app, form = layui.form, laydate = layui.laydate,
        $ = layui.jquery;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
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
            refreshChart();
        }
    });
    laydate.render({
        elem: '#contrastDate',
        done: function(value, date) {
            refreshChart();
        }
    });
    //对比时间
    $(".contrast-time2").change(function() {
        $(".time-input2").toggle();
    });
    $("#selectType").on('click', 'span', function() {
        $("#selectType span").removeClass('active');
        $(this).addClass('active');
        refreshChart();
    });
    setInterval(function() {
        var now = (new Date()).toLocaleString();
        $('#current-time').html(now);
    }, 1000);
    refreshChart();
});

function refreshChart() {
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
    if($("#selectContrast").is(":checked")) {
        contrastDate = $("#contrastDate").val();
    }
    $.ajax({
        type: "post",
        url: "/diagram/recharge/pie",
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
            var obj = res.data;
            var list = obj.list;
            var legendData = obj.legendData;
            var seriesData = [];
            for(var o in list) {
                var s = {
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
                    }
                };
                s.name = list[o].name;
                s.data = list[o].data;
                seriesData.push(s);
            }
            chartPieObj.setOption({
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data: legendData
                },
                series: seriesData
            }, true);
        }
    });
}

function chartBar() {
    var scope = $("#selectType").find(".active").attr("typeData");
    var fromDate = $("#fromDate").val();
    var toDate = $("#toDate").val();
    var contrastDate = null;
    if($("#selectContrast").is(":checked")) {
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
                dataZoom: [{
                    type: 'slider',
                    show: true
                }],
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
}
