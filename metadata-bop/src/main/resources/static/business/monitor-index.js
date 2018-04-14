var thirdReqChart = echarts.init(document.getElementById('thirdReqDiagram'), 'dark');
thirdReqChart.showLoading();
var productReqChart = echarts.init(document.getElementById('productReqDiagram'), 'dark');
productReqChart.showLoading();
$.get(
    "/stats/request/third",
    function(res) {
        if(res.code === '000000') {
            var data = res.data;
            thirdReqChart.hideLoading();
            thirdReqChart.setOption({
                tooltip: {trigger: 'axis', axisPointer: {type: 'cross'}},
                yAxis: {type: 'value', axisPointer: {snap: true}},
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: data.xAxisData
                },
                series: [{
                    name: '请求次数',
                    type: 'line',
                    color: 'red',
                    smooth: true,
                    data: data.seriesData
                }]
            });
        }
    }
);
$.get(
    "/stats/request/product",
    function(res) {
        if(res.code === '000000') {
            var legendData = [];
            var series = [];
            var data = res.data.data;
            for(var i in data) {
                legendData.push(data[i].productType);
                var _chartData = [];
                var product = data[i].product;
                for(var j in product) {
                    _chartData.push([product[j].request, product[j].ratio, product[j].client, product[j].name, data[i].productType]);
                }
                series.push({
                    name: data[i].productType,
                    data: _chartData,
                    type: 'scatter',
                    symbolSize: function(data) {
                        return Math.sqrt(data[2]) * 2;
                    },
                    label: {
                        emphasis: {
                            show: true,
                            formatter: function(param) {
                                return param.data[3];
                            },
                            position: 'inside'
                        }
                    }
                });
            }
            productReqChart.hideLoading();
            productReqChart.setOption({
                legend: {right: 10, data: legendData},
                xAxis: {splitLine: {lineStyle: {type: 'dashed'}}},
                yAxis: {splitLine: {lineStyle: {type: 'dashed'}}, scale: true, min: 0},
                series: series
            });
        }
    }
);