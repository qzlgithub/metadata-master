var myChart = echarts.init(document.getElementById('clientChart'));
var rChart = echarts.init(document.getElementById('rChart'));
myChart.showLoading();
rChart.showLoading();

function get_date() {
    $.ajax({
        type: "post",
        url: "/client/chart",
        data: JSON.stringify({
            "days": $("#days").val(),
            "unit": $("#unit").val(),
            "startDate": $("#start1").val(),
            "endDate": $("#end1").val(),
            "compareFrom": $("#start2").val()
        }),
        contentType: "application/json",
        success: function(res) {
            if(res.code !== '000000') {
                return;
            }
            myChart.hideLoading();
            var obj = res.data;
            var list = obj.list;
            var legendData = [];
            var seriesData = [];
            for(var o in list) {
                legendData.push(list[o].name);
                var s = {type: 'line', areaStyle: {}};
                s.name = list[o].name;
                s.data = list[o].data;
                seriesData.push(s);
            }
            myChart.setOption({
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: obj.xData
                },
                legend: {
                    orient: 'vertical',
                    data: legendData
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        label: {
                            backgroundColor: '#6a7985'
                        }
                    }
                },
                yAxis: {
                    type: 'value'
                },
                series: seriesData
            }, true);
        }
    })
}

function get_recharge_date() {
    $.ajax({
        type: "post",
        url: "/recharge/chart",
        data: JSON.stringify({
            "days": $("#rdays").val(),
            "unit": $("#runit").val(),
            "startDate": $("#rstart1").val(),
            "endDate": $("#rend1").val(),
            "compareFrom": $("#rstart2").val()
        }),
        contentType: "application/json",
        success: function(res) {
            if(res.code !== '000000') {
                return;
            }
            rChart.hideLoading();
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
            rChart.setOption({
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
    })
}