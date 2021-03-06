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
    $("#warning-icon").on("click", ".icon3-close", function() {
        if(alarmed_warning_code != "") {
            if($("#" + alarmed_warning_code).length > 0) {
                var audio = document.getElementById(alarmed_warning_code);
                if(audio !== null) {
                    if(!audio.paused) {
                        audio.pause();
                    }
                }
            }
        }
        $(this).removeClass("icon3-close");
        $(this).addClass("icon3");
    });
    initData();
    getLineChart(1);
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
    $("#product-name-0").html('');
    $("#product-name-1").html('');
    $.get(
        "/monitoring/product/traffic",
        {"pageNum": page, "pageSize": 2},
        function(data) {
            var pages = data.data.pages;
            totalPage = pages;
            var xAxisData = data.data.xAxisData;
            var productData = data.data.productData;
            var seriesData = data.data.seriesData;
            for(var i in productData) {
                $("#product-name-" + i).text(productData[i]);
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

var index = 0;

function initData() {
    $("input[name='productName']:checked").each(function() {
        arr.push($(this).val());
    });
    changeSelectedProduct();
    getRequestList();
    getScatterChart();
    timeReady();
    getWarningOutList();
    getWarningList();
}

function timeReady() {
    setInterval(function() {
        try {
            changeSelectedProduct();
        }
        catch(e) {
        }
        try {
            getRequestList();
        }
        catch(e) {
        }
        try {
            getScatterChart();
        }
        catch(e) {
        }
        try {
            getWarningOutList();
        }
        catch(e) {
        }
        try {
            getWarningList();
        }
        catch(e) {
        }
    }, 3000);
    setInterval(function() {
        try {
            getWarningSetting();
        }
        catch(e) {
        }
    }, 60000);
}

function getWarningSetting() {
    $.get(
        "/monitoring/product/setting",
        {},
        function(data) {
            var settingList = data.data.settingList;
            for(var i in settingList) {
                $("#play-" + settingList[i].warningCode).val(settingList[i].play);
            }
        }
    );
}

function getRequestList() {
    $.get(
        "/monitoring/product/detail",
        {},
        function(data) {
            var list = data.list;
            for(var i in list) {
                $("#table-tbody").append('<tr><td>' + list[i].host + '</td><td>' + list[i].productName + '</td><td>' + list[i].msg + '</td></tr>');
                if($("#table-tbody").children().length > 100) {
                    $("#table-tbody").children().first().remove();
                }
            }
        }
    );
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
mainChart.setOption({
    tooltip: {trigger: 'axis'},
    grid: {left: '3%', right: '4%', bottom: '3%', containLabel: true},
    xAxis: {type: 'category', boundaryGap: false, data: []},
    yAxis: {type: 'value'},
    series: []
});
var scatterChart = echarts.init(document.getElementById('scatter-chart'), 'dark');
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

function changeSelectedProduct() {
    $.get(
        "/monitoring/product/request",
        {"productId": arr.join(",")},
        function(res) {
            if(res.code === '000000') {
                var data = res.data;
                var legendData = [];
                var series = [];
                var xAxisData = data.xAxisData;
                var lines = data.lineData;
                for(var i in lines) {
                    legendData.push(lines[i].name);
                    series.push({
                        "type": "line",
                        "name": lines[i].name,
                        "data": lines[i].series,
                        "smooth": true
                    })
                }
                mainChart.setOption({
                    legend: {
                        data: legendData
                    },
                    xAxis: {
                        data: xAxisData
                    },
                    series: series
                });
            }
        }
    );
}

function getScatterChart() {
    $.get(
        "/monitoring/product/ratio",
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

function getWarningOutList() {
    $.get(
        "/monitoring/product/out",
        {},
        function(data) {
            if(data.code == "000000") {
                var list = data.list;
                /*<tr>
                <td>请求超时</td>
                <td>身份验证</td>
                <td><span class="general">一般</span></td>
                <td>20次</td>
                <td>02/20 15:15:15</td>
                </tr>*/
                if(list.length > 0) {
                    $("#product-ready-data").empty();
                    var htmlStr = '';
                    for(var i in list) {
                        htmlStr += '<tr>';
                        htmlStr += '<td>' + list[i].warningName + '</td>';
                        htmlStr += '<td>' + list[i].productName + '</td>';
                        if(list[i].level == 1) {
                            htmlStr += '<td><span class="general">一般</span></td>';
                        }
                        else if(list[i].level == 2) {
                            htmlStr += '<td><span class="serious">严重</span></td>';
                        }
                        htmlStr += '<td>' + list[i].count + '</td>';
                        htmlStr += '<td>' + list[i].lastTime + '</td>';
                        htmlStr += '</tr>';
                    }
                    $("#product-ready-data").html(htmlStr);
                    $(".product-ready-class").show();
                    $(".product-ready-none-class").hide();
                }
                else {
                    $(".product-ready-class").hide();
                    $(".product-ready-none-class").show();
                }
            }
        }
    );
}

var alarmed_warning_id = "";
var alarmed_warning_code = "";

function getWarningList() {
    $.get(
        "/monitoring/product/warning",
        {},
        function(data) {
            if(data.code == "000000") {
                var list = data.list;
                /*<tr>
                    <td>请求超时</td>
                    <td>身份验证</td>
                    <td>02/20 15:15:15</td>
                </tr>*/
                if(list.length > 0) {
                    $("#product-data").empty();
                    var htmlStr = '';
                    for(var i in list) {
                        if(i == 0) {
                            if(alarmed_warning_id != list[i].id) {
                                alarmed_warning_id = list[i].id;
                                var warningCode = list[i].warningCode;
                                if(alarmed_warning_code != warningCode) {
                                    if(alarmed_warning_code != "") {
                                        if($("#" + alarmed_warning_code).length > 0) {
                                            var audio = document.getElementById(alarmed_warning_code);
                                            if(audio !== null) {
                                                if(!audio.paused) {
                                                    audio.pause();
                                                }
                                            }
                                        }
                                    }
                                    alarmed_warning_code = warningCode;
                                }
                                if($("#" + warningCode).length > 0) {
                                    var audio = document.getElementById(warningCode);
                                    if(audio !== null) {
                                        if(audio.paused && $("#play-" + warningCode).val() == 1) {
                                            $("#warningLabel").removeClass("icon3");
                                            $("#warningLabel").addClass("icon3-close");
                                            audio.play();
                                        }
                                    }
                                }
                            }
                        }
                        htmlStr += '<tr>';
                        htmlStr += '<td>' + list[i].warningName + '</td>';
                        htmlStr += '<td>' + list[i].name + '</td>';
                        htmlStr += '<td>' + list[i].warningAt + '</td>';
                        htmlStr += '</tr>';
                    }
                    $("#product-data").html(htmlStr);
                    $(".product-class").show();
                    $(".product-none-class").hide();
                }
                else {
                    $(".product-class").hide();
                    $(".product-none-class").show();
                    if(alarmed_warning_code != "") {
                        if($("#" + alarmed_warning_code).length > 0) {
                            var audio = document.getElementById(alarmed_warning_code);
                            if(audio !== null) {
                                if(!audio.paused) {
                                    audio.pause();
                                }
                            }
                        }
                        alarmed_warning_code = "";
                    }
                }
            }
        }
    );
}
