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
        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
    ],
    function(ec) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('rechargeBar'));
        option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                selectedMode: false,
                data: ['代充', '垫付', '赔偿', "返还"]
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    splitLine: {show: false},
                    type: 'category',
                    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
                }
            ],
            yAxis: [
                {
                    splitLine: {show: false},//网格线出现与否
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '代充',
                    type: 'bar',
                    stack: '广告',
                    barWidth: 30,
                    data: [120, 132, 101, 134, 90, 230, 210]
                },
                {
                    name: '垫付',
                    type: 'bar',
                    stack: '广告',
                    barWidth: 30,
                    data: [220, 182, 191, 234, 290, 330, 310]
                },
                {
                    name: '赔偿',
                    type: 'bar',
                    stack: '广告',
                    barWidth: 30,
                    data: [220, 182, 191, 234, 290, 330, 310]
                },
                {
                    name: '返还',
                    type: 'bar',
                    stack: '广告',
                    barWidth: 30,
                    data: [150, 232, 201, 154, 190, 330, 410]
                }
            ]
        };
        // 为echarts对象加载数据
        myChart.setOption(option);
    }
);