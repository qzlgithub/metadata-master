var form;
var arr = [];
layui.config({
    base: '../../static/build/js/'
}).use(['form', 'app'], function() {
    var app = layui.app,
        $ = layui.jquery;
    form = layui.form;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    setInterval(function() {
        var now = (new Date()).toLocaleString();
        $('#current-time').html(now);
    }, 1000);
    $(".choose-show").click(function() {
        $(".third-select").toggle();
    });
    $(".choose-hide").click(function() {
        $(".third-select").hide();
        arr = [];
        $("input[name='productName']:checked").each(function() {
            arr.push($(this).val());
        });
        changeSelectedProduct();
    });
    findClientAll();
    form.on('checkbox(productFilter)', function(data) {
        if(data.elem.checked) {
            var htmlStr = '<li class="layui-icon" data-id="' + data.value + '" id="selected-' + data.value + '">' + data.elem.title + '<i class="selected-delete">&#x1006;</i></li>';
            $("#selected-product").prepend(htmlStr);
        }
        else {
            $("#selected-" + data.value).remove();
        }
        refreshCheckbox();
    });
    $("#selected-product").on("click", ".selected-delete", function() {
        var id = $(this).parent().attr("data-id");
        $("#checkbox-" + id).prop("checked", false);
        $(this).parent().remove();
        refreshCheckbox();
    });
    initData();
    getLineChart();
});

function getLineChart() {
    var lineChart1 = echarts.init(document.getElementById('line-chart-1'), 'dark');
    var lineChart2 = echarts.init(document.getElementById('line-chart-2'), 'dark');
    var option = {
        xAxis: {
            show: false,
            type: 'category',
            boundaryGap: false,
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
            show: false,
            type: 'value'
        },
        grid: {
            right: 0,
            left: 0,
            top: 0,
            bottom: 0
        },
        series: [{
            data: [820, 932, 901, 934, 1290, 1330, 1320],
            type: 'line',
            areaStyle: {}
        }]
    };
    lineChart1.setOption(option);
    lineChart2.setOption(option);
}

var index = 0;

function initData() {
    $("input[name='productName']:checked").each(function() {
        arr.push($(this).val());
    });
    changeSelectedProduct();
    getScatterChart();
    setInterval(function() {
        changeSelectedProduct();
        getScatterChart();
        for(var i = 0; i < 20; i++) {
            $("#table-tbody").append('<tr><td>192.168.1.1=' + (index++) + '</td><td>身份验证</td><td>请求失败</td></tr>');
            if($("#table-tbody").children().length > 100) {
                $("#table-tbody").children().first().remove();
            }
        }
    }, 3000);
}

function refreshCheckbox() {
    var count = 0;
    $("input[name='productName']:checked").each(function() {
        count++;
    });
    if(count == 5) {
        $("input[name='productName']:not(:checked)").each(function() {
            $(this).attr("disabled", "true");
        });
    }
    else {
        $("input[name='productName']:not(:checked)").each(function() {
            $(this).removeAttr("disabled");
        });
    }
    form.render('checkbox');
}

function findClientAll() {
    $.get(
        "/monitoring/product/allProduct",
        {},
        function(data) {
            var list = data.list;
            var htmlStr = '';
            $("#product-all").empty();
            for(var i in list) {
                htmlStr += '<li>';
                htmlStr += '<input type="checkbox" name="productName" id="checkbox-' + list[i].productId + '" value="' + list[i].productId + '" lay-skin="primary" lay-filter="productFilter" title="' + list[i].name + '"  />';
                htmlStr += '</li>';
            }
            $("#product-all").html(htmlStr);
            form.render('checkbox');
        }
    );
}

var mainChart = echarts.init(document.getElementById('main-chart'), 'dark');
var scatterChart = echarts.init(document.getElementById('scatter-chart'), 'dark');

function changeSelectedProduct() {
    $.get(
        "/monitoring/product/request",
        {"productId": arr.join(",")},
        function(data) {
            var jsonStr = data.data.data;
            var json = JSON.parse(jsonStr);
            var legendData = json.legendData;
            var xAxisData = json.xAxisData;
            var seriesData = json.seriesData;
            var series = [];
            var tempMap;
            for(var i in legendData) {
                tempMap = {};
                tempMap['name'] = legendData[i];
                tempMap['type'] = 'line';
                tempMap['data'] = seriesData[i];
                series[i] = tempMap;
            }
            var option = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: legendData
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: xAxisData
                },
                yAxis: {
                    type: 'value'
                },
                series: series
            };
            mainChart.setOption(option, true);
        }
    );
}

function getScatterChart() {
    $.get(
        "/monitoring/product/ratio",
        {},
        function(data) {
            var jsonStr = data.data.data;
            var json = JSON.parse(jsonStr);
            var legendData = json.legendData;
            var seriesData = json.seriesData;
            var series = [];
            var obj;
            for(var i in seriesData) {
                series.push(obj = {
                    name: legendData[i],
                    data: seriesData[i],
                    type: 'scatter',
                    symbolSize: function(data) {
                        return Math.sqrt(data[2]) / 0.2;
                    },
                    label: {
                        emphasis: {
                            show: true,
                            formatter: function(param) {
                                return param.data[3];
                            },
                            position: 'top'
                        }
                    },
                    itemStyle: {
                        normal: {
                            shadowBlur: 10,
                            shadowColor: 'rgba(120, 36, 50, 0.5)',
                            shadowOffsetY: 5
                        }
                    }
                });
            }
            var option = {
                legend: {
                    right: 10,
                    data: legendData
                },
                xAxis: {
                    splitLine: {
                        lineStyle: {
                            type: 'dashed'
                        }
                    }
                },
                yAxis: {
                    splitLine: {
                        lineStyle: {
                            type: 'dashed'
                        }
                    },
                    scale: true
                },
                series: series
            };
            scatterChart.setOption(option, true);
        }
    );
}