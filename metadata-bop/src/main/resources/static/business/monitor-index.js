$(function() {
    getLineChart(1);
    getProductPro();
    getWarningList();
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
                        smooth: true,
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

var productReqDiagram = echarts.init(document.getElementById('productReqDiagram'), 'dark');
var thirdReqDiagram = echarts.init(document.getElementById('thirdReqDiagram'), 'dark');
productReqDiagram.setOption({
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
thirdReqDiagram.setOption({
    tooltip: {trigger: 'axis', axisPointer: {type: 'cross'}},
    yAxis: {type: 'value', axisPointer: {snap: true}},
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ["00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:30", "08:00", "09:00",
            "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
            "20:00", "21:00", "22:00", "23:00"]
    },
    series: [{
        name: '请求次数',
        type: 'line',
        color: 'red',
        smooth: true,
        data: []
    }]
});

function getProductPro() {
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
            productReqDiagram.setOption({
                legend: {
                    data: legendData
                },
                series: series
            });
        }
    );
}

function getWarningList() {
    $.get(
        "/diagram/index/warning",
        {},
        function(data) {
            if(data.code === "000000") {
                var warningThirdList = data.data.warningThirdList;
                var warningProductList = data.data.warningProductList;
                var warningClientList = data.data.warningClientList;
                if(warningThirdList.length > 0) {
                    /*<tr>
                    <td>请求超时</td>
                    <td>身份验证</td>
                    <td>02/20 15:15:15</td>
                    </tr>*/
                    $("#third-list-id").empty();
                    var htmlStr = '';
                    for(var i in warningThirdList) {
                        var obj = warningThirdList[i];
                        htmlStr += '<tr>';
                        htmlStr += '<td>' + obj.warningName + '</td>';
                        htmlStr += '<td>' + obj.name + '</td>';
                        htmlStr += '<td>' + obj.warningAt + '</td>';
                        htmlStr += '</tr>';
                    }
                    $("#third-list-id").html(htmlStr);
                    $(".third-have-class").show();
                    if(warningThirdList.length > 6) {
                        $(".third-have-more-class").show();
                    }
                }
                else {
                    $(".third-none-class").show();
                }
                if(warningProductList.length > 0) {
                    /*<tr>
                    <td>请求超时</td>
                    <td>身份验证</td>
                    <td>02/20 15:15:15</td>
                    </tr>*/
                    $("#product-list-id").empty();
                    var htmlStr = '';
                    for(var i in warningProductList) {
                        var obj = warningProductList[i];
                        htmlStr += '<tr>';
                        htmlStr += '<td>' + obj.warningName + '</td>';
                        htmlStr += '<td>' + obj.productName + '</td>';
                        htmlStr += '<td>' + obj.warningAt + '</td>';
                        htmlStr += '</tr>';
                    }
                    $("#product-list-id").html(htmlStr);
                    $(".product-have-class").show();
                    if(warningProductList.length > 6) {
                        $(".product-have-more-class").show();
                    }
                }
                else {
                    $(".product-none-class").show();
                }
                if(warningClientList.length > 0) {
                    /* <tr>
                     <td>请求超时</td>
                     <td>身份验证</td>
                     <td>02/20 15:15:15</td>
                     </tr>*/
                    $("#client-list-id").empty();
                    var htmlStr = '';
                    for(var i in warningClientList) {
                        var obj = warningClientList[i];
                        htmlStr += '<tr>';
                        htmlStr += '<td>' + obj.warningName + '</td>';
                        htmlStr += '<td>' + obj.corpName + '</td>';
                        htmlStr += '<td>' + obj.warningAt + '</td>';
                        htmlStr += '</tr>';
                    }
                    $("#client-list-id").html(htmlStr);
                    $(".client-have-class").show();
                    if(warningClientList.length > 6) {
                        $(".client-have-more-class").show();
                    }
                }
                else {
                    $(".client-none-class").show();
                }
            }
        }
    );
}