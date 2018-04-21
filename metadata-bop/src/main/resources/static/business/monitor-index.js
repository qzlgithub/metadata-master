$(function(){
    getLineChart(1);
    getProductPro();
});
var isLock = false;
var currPage = 1;
var totalPage = 0;

function previousPage() {
    if(isLock) {
        return;
    }
    isLock = true;
    if(currPage <= 1) {
        isLock = false;
        return;
    }
    getLineChart(--currPage);
}

function nextPage() {
    if(isLock) {
        return;
    }
    isLock = true;
    if(currPage >= totalPage) {
        isLock = false;
        return;
    }
    getLineChart(++currPage);
}

function getLineChart(page) {
    $("#line-chart-0").html('');
    $("#line-chart-0").removeAttr('_echarts_instance_');
    $("#line-chart-1").html('');
    $("#line-chart-1").removeAttr('_echarts_instance_');
    $("#line-chart-2").html('');
    $("#line-chart-2").removeAttr('_echarts_instance_');
    $("#line-chart-3").html('');
    $("#line-chart-3").removeAttr('_echarts_instance_');
    $("#product-name-0").html('');
    $("#product-name-1").html('');
    $("#product-name-2").html('');
    $("#product-name-3").html('');
    $.get(
        "/diagram/index/client",
        {"pageNum": page, "pageSize": 4},
        function(data) {
            var pages = data.data.pages;
            totalPage = pages;
            var xAxisData = data.data.xAxisData;
            var clientData = data.data.clientData;
            var seriesData = data.data.seriesData;
            for(var i in clientData) {
                $("#product-name-" + i).text(clientData[i]);
                var lineChart = echarts.init(document.getElementById('line-chart-' + i), 'dark');
                var option = {
                    tooltip: {
                        trigger: 'axis',
                        formatter: "{b} : {c}"
                    },
                    xAxis: {
                        show: false,
                        type: 'category',
                        boundaryGap: false,
                        data: xAxisData
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
                        data: seriesData[i],
                        type: 'line',
                        areaStyle: {}
                    }]
                };
                lineChart.setOption(option);
            }
            $("#page-div").show();
            $("#page").text(page + "/" + pages);
            isLock = false;
        }
    );
}
var scatterChart = echarts.init(document.getElementById('productReqDiagram'), 'dark');
scatterChart.setOption({
    legend: {
        right: 10
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
    series: []
});
function getProductPro(){
    $.get(
        "/diagram/index/product",
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
            scatterChart.setOption({
                legend: {
                    data: legendData
                },
                series: series
            });
        }
    );
}


//$.get(
//    "/stats/request/third",
//    function(res) {
//        if(res.code === '000000') {
//            var data = res.data;
//            thirdReqChart.hideLoading();
//            thirdReqChart.setOption({
//                tooltip: {trigger: 'axis', axisPointer: {type: 'cross'}},
//                yAxis: {type: 'value', axisPointer: {snap: true}},
//                xAxis: {
//                    type: 'category',
//                    boundaryGap: false,
//                    data: data.xAxisData
//                },
//                series: [{
//                    name: '请求次数',
//                    type: 'line',
//                    color: 'red',
//                    smooth: true,
//                    data: data.seriesData
//                }]
//            });
//        }
//    }
//);
//$.get(
//    "/stats/request/product",
//    function(res) {
//        if(res.code === '000000') {
//            var legendData = [];
//            var series = [];
//            var data = res.data.data;
//            for(var i in data) {
//                legendData.push(data[i].productType);
//                var _chartData = [];
//                var product = data[i].product;
//                for(var j in product) {
//                    _chartData.push([product[j].request, product[j].ratio, product[j].client, product[j].name, data[i].productType]);
//                }
//                series.push({
//                    name: data[i].productType,
//                    data: _chartData,
//                    type: 'scatter',
//                    symbolSize: function(data) {
//                        return Math.sqrt(data[2]) * 2;
//                    },
//                    label: {
//                        emphasis: {
//                            show: true,
//                            formatter: function(param) {
//                                return param.data[3];
//                            },
//                            position: 'inside'
//                        }
//                    }
//                });
//            }
//            productReqChart.hideLoading();
//            productReqChart.setOption({
//                legend: {right: 10, data: legendData},
//                xAxis: {splitLine: {lineStyle: {type: 'dashed'}}},
//                yAxis: {splitLine: {lineStyle: {type: 'dashed'}}, scale: true, min: 0},
//                series: series
//            });
//        }
//    }
//);