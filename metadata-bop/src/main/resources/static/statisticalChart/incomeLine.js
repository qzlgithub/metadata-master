// 路径配置
require.config({
    paths: {
        echarts: 'http://echarts.baidu.com/build/dist'
    }
});
// 使用
require(
    [
        'echarts',
        'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
    ],
    function(ec) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('incomeLine'));
        option = {
            tooltip: {
                trigger: 'axis'
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    name: '收入',
                    type: 'line',
                    stack: '总量',
                    itemStyle: {
                        normal: {
                            areaStyle: {
                                color: "#c7e1ff"
                            },
                            lineStyle: {
                                width: 3,  //连线粗细
                                color: "#3487ff"  //连线颜色
                            }
                        }
                    },
                    data: [10000, 132000, 101000, 134000, 90000, 230000, 210000, 200000, 132000,
                        101000, 134000, 90000, 230000, 210000]
                }
            ]
        };
        // 为echarts对象加载数据
        myChart.setOption(option);
    }
);