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
        var myChart = ec.init(document.getElementById('productBar'));
        option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            //color: ['#45C1BD', '#2CC35A', '#6AA5FC'],
            color: ['#42a4ff', '#1dc327', '#fc3bd2'],
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: ['金融逾期', '黑名单', '信用评级', '企业信用评分']
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '请求次数',
                    //barWidth:27,
                    type: 'bar',
                    //barGap:'10%',柱状图之间的距离
                    data: [3200000, 3320000, 3010000, 3340000]
                },
                {
                    name: '成本',
                    //barWidth:27,
                    type: 'bar',
                    data: [1200000, 1320000, 1010000, 1340000]
                },
                {
                    name: '收入',
                    //barWidth:27,
                    type: 'bar',
                    data: [2200000, 1820000, 1910000, 2340000]
                }
            ],
            legend: {
                selectedMode: false,
                left: "left",
                data: ['请求次数', '成本', '收入']
            }
        };
        // 为echarts对象加载数据
        myChart.setOption(option);
    }
);