var chart = echarts.init(document.getElementById('panel'), 'dark');
chart.setOption({
    legend: {orient: 'vertical', data: []},
    tooltip: {trigger: 'axis', axisPointer: {type: 'cross', label: {backgroundColor: '#6a7985'}}},
    xAxis: {type: 'category', boundaryGap: false, data: []},
    yAxis: {type: 'value'}
});
$('#scopeOcx span').click(function() {
    $('#scopeOcx span').each(function() {
        $(this).removeClass('active');
    });
    $(this).addClass('active');
    refresh_chart();
});
$('#unitOcx span').click(function() {
    $('#unitOcx span').each(function() {
        $(this).removeClass('active');
    });
    $(this).addClass('active');
    refresh_chart();
});
layui.use(['laydate'], function() {
    var laydate = layui.laydate;
    laydate.render({
        elem: '#date1',
        range: true,
        done: function(value, date) {
            if(value !== "") {
                var dates = value.split(" - ");
                $("#startDate").val(dates[0]);
                $("#endDate").val(dates[1]);
            }
            else {
                $("#startDate").val("");
                $("#endDate").val("");
            }
            var scope = $('#scopeOcx').find('.active').data("scope");
            if(scope === '0') {
                refresh_chart();
            }
        }
    });
    laydate.render({
        elem: '#date2',
        range: true,
        done: function(value, date) {
            if(value !== "") {
                var dates = value.split(" - ");
                $("#compareFrom").val(dates[0]);
            }
            else {
                $("#compareFrom").val("");
            }
            var scope = $('#scopeOcx').find('.active').data("scope");
            if(scope === '0') {
                refresh_chart();
            }
        }
    });
});
$(document).ready(function() {
    $(".contrast-time2").click(function() {
        $(".time-input2").toggle();
    });
});
$(function() {
    refresh_chart();
});

function refresh_chart() {
    var scope = $('#scopeOcx').find('.active').data("scope");
    var unit = $('#unitOcx').find('.active').data("unit");
    var start_date = $("#startDate").val();
    var end_date = $("#endDate").val();
    var compare_from = $("#compareFrom").val();
    console.log(scope + ", " + unit + ", " + start_date + ", " + end_date + ", " + compare_from);
    $.ajax({
        type: "post",
        url: "/diagram/client/increment",
        data: JSON.stringify({
            "scope": scope,
            "unit": unit,
            "startDate": start_date,
            "endDate": end_date,
            "compareFrom": compare_from
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
                var s = {type: 'line', areaStyle: {}};
                s.name = list[o].name;
                s.data = list[o].data;
                seriesData.push(s);
            }
            chart.setOption({
                xAxis: {data: obj.xData},
                legend: {data: legendData},
                series: seriesData
            });
        }
    });
}



