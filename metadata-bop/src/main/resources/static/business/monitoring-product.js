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
});

function initData() {
    $("input[name='productName']:checked").each(function() {
        arr.push($(this).val());
    });
    changeSelectedProduct();
    setInterval(function() {
        changeSelectedProduct();
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
            mainChart.setOption(option = {
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
            }, true);
        }
    );
}