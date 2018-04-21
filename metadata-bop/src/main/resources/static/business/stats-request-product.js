var form;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'laydate'], function() {
    var app = layui.app, laydate = layui.laydate,
        $ = layui.jquery;
    form = layui.form;
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
    form.on('checkbox(allChoose)', function(data) {
        var child = $('#pro_type').find('li input[type="checkbox"]:not([name="show"])');
        child.each(function(index, item) {
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });
    form.on("checkbox(choose)", function(data) {
        var childCount = $("#pro_type li").length;
        var childChecked = $(data.elem).parents('#pro_type').find('li input[type="checkbox"]:checked');
        if(childChecked.length == childCount) {
            $(data.elem).parents('.product-nav1').find('span input#allChoose').get(0).checked = true;
        }
        else {
            $(data.elem).parents('.product-nav1').find('span input#allChoose').get(0).checked = false;
        }
        form.render('checkbox');
    });
    $("#selectType span").on('click', function() {
        $("#selectType span").removeClass('active');
        $(this).addClass('active');
        refreshChart();
    });
    $('#unitOcx span').on('click', function() {
        $('#unitOcx span').removeClass('active');
        $(this).addClass('active');
        refreshChart();
    });
    refreshChart();
    findProductAll();
});
//统计产品 点击下拉
$(document).ready(function() {
    $(".product-title1").click(function() {
        $(".product-tjlist1").toggle();
    });
    $(".nav-close1").click(function() {
        $(".product-tjlist1").hide();
    })
});
$(document).ready(function() {
    $(".product-title2").click(function() {
        $(".product-tjlist2").toggle();
    });
    $(".nav-close2").click(function() {
        $(".product-tjlist2").hide();
    })
});
var chartLine = echarts.init(document.getElementById('chart-line'), 'dark');
var chartPie = echarts.init(document.getElementById('chart-pie'), 'dark');
var chartBar = echarts.init(document.getElementById('chart-bar'), 'dark');

function refreshChart() {
    var scope = $("#selectType").find(".active").attr("typeData");
    var unit = $('#unitOcx').find('.active').data("unit");
    var fromDate = $("#fromDate").val();
    var toDate = $("#toDate").val();
    var contrastDate = null;
    if($("#selectContrast").is(":checked")) {
        contrastDate = $("#contrastDate").val();
    }
    var clientName = $.trim($("#clientName").val());
    var arr = [];
    $("input[name='productIds']:checked").each(function() {
        arr.push($(this).val());
    });
    $.ajax({
        type: "post",
        url: "/diagram/request/line",
        data: JSON.stringify({
            "scope": scope,
            "unit": unit,
            "startDate": fromDate,
            "endDate": toDate,
            "compareFrom": contrastDate,
            "clientName": clientName,
            "productIds": arr
        }),
        contentType: "application/json",
        success: function(res) {
            if(res.code !== '000000') {
                return;
            }
            var legendData = res.data.legendData;
            var xData = res.data.xData;
            var list = res.data.list;
            var seriesData = [];
            for(var o in list) {
                var s = {type: 'line', areaStyle: {}};
                s.name = list[o].name;
                s.data = list[o].data;
                seriesData.push(s);
            }
            chartLine.setOption({
                dataZoom: [{
                    type: 'slider',
                    show: true
                }],
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: xData
                },
                yAxis: {
                    type: 'value'
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
                legend: {
                    data: legendData
                },
                series: seriesData
            }, true);
        }
    });
    $.ajax({
        type: "post",
        url: "/diagram/request/pie",
        data: JSON.stringify({
            "scope": scope,
            "unit": unit,
            "startDate": fromDate,
            "endDate": toDate,
            "compareFrom": contrastDate,
            "clientName": clientName,
            "productIds": arr
        }),
        contentType: "application/json",
        success: function(res) {
            if(res.code !== '000000') {
                return;
            }
            var legendData = res.data.legendData;
            var list = res.data.list;
            var seriesData = [];
            for(var o in list) {
                var s = {
                    type: 'pie',
                    radius: '55%',
                    center: ['40%', '50%'],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                };
                s.name = list[o].name;
                s.data = list[o].data;
                seriesData.push(s);
            }
            chartPie.setOption({
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    type: 'scroll',
                    orient: 'vertical',
                    right: 10,
                    top: 20,
                    bottom: 20,
                    data: legendData
                },
                series: seriesData
            }, true);
        }
    });
    $.ajax({
        type: "post",
        url: "/diagram/request/bar",
        data: JSON.stringify({
            "scope": scope,
            "unit": unit,
            "startDate": fromDate,
            "endDate": toDate,
            "compareFrom": contrastDate,
            "clientName": clientName,
            "productIds": arr
        }),
        contentType: "application/json",
        success: function(res) {
            if(res.code !== '000000') {
                return;
            }
            var legendData = res.data.legendData;
            var xData = res.data.xData;
            var list = res.data.list;
            var seriesData = [];
            for(var o in list) {
                var s = {type: 'bar', areaStyle: {}};
                s.name = list[o].name;
                s.data = list[o].data;
                seriesData.push(s);
            }
            chartBar.setOption({
                tooltip: {
                    trigger: 'axis'
                },
                dataZoom: [{
                    type: 'slider',
                    show: true
                }],
                legend: {
                    data: legendData
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        data: xData
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: seriesData
            });
        }
    });
}

function findProductAll() {
    $.get(
        "/monitoring/product/type",
        {},
        function(data) {
            var list = data.data.list;
            var htmlStr = '';
            $("#pro_type").empty();
            for(var i in list) {
                htmlStr += '<div class="mt10">';
                htmlStr += '<div class="col5 mt10 pl10">' + list[i].typeName + '</div>';
                htmlStr += '<ul class="layui-form choose-check-list clearfix pb5  col5 important">';
                for(var j in list[i].list) {
                    var obj = list[i].list[j];
                    htmlStr += '<li>';
                    htmlStr += '<input type="checkbox" name="productIds" value="' + obj.productId + '" lay-skin="primary" lay-filter="choose" title="' + obj.name + '"/>';
                    htmlStr += '</li>';
                }
                htmlStr += '</ul>';
                htmlStr += '</div>';
            }
            $("#pro_type").html(htmlStr);
            form.render('checkbox');
        }
    );
}
