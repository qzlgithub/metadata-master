var message;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message', 'laydate'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    laydate = layui.laydate;
    //日期
    laydate.render({
        elem: '#date'
    });
    laydate.render({
        elem: '#date1'
    });
});
//对比时间
$(document).ready(function() {
    $(".contrast-time2").click(function() {
        $(".time-input2").toggle();
    });
});
//点击展开
$(document).ready(function() {
    $(".btn-slide").click(function() {
        $("#panel").slideToggle("slow");
        $(this).toggleClass("active");
        return false;
    });
});
$(function() {
    customerDataInit();
    showChart();
});
function showChart(){
    var myChart = echarts.init(document.getElementById('panel'));
    var scopeType = $('#scopeId').find('.active').attr('otherVal');
    $.get('/stats/client/clientListJson',
        {"scopeType": scopeType},
        function (data) {
            var data = eval("("+data+")");
            myChart.setOption(option = {
                tooltip: {
                    trigger: 'axis'
                },
                xAxis: {
                    data: data.map(function (item) {
                        return item[0];
                    })
                },
                yAxis: {
                    splitLine: {
                        show: false
                    }
                },
                toolbox: {
                    left: 'center',
                    feature: {
                        dataZoom: {
                            yAxisIndex: 'none'
                        },
                        restore: {},
                        saveAsImage: {}
                    }
                },
                dataZoom: [{
                    startValue: '2014-06-01'
                }, {
                    type: 'inside'
                }],
                series: {
                    type: 'line',
                    data: data.map(function (item) {
                        return item[1];
                    })
                }
            });
        });
}

function customerDataInit(){
    var obj = {
        pageNum: 1,
        pageSize: 10
    };
    getClientList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getClientList(obj);
            }
        })
    });
}
var rowStr = '<tr>' +
    '<td>#{registerDate}</td>' +
    '<td>#{corpName}</td>' +
    '<td>#{shortName}</td>' +
    '<td>#{username}</td>' +
    '<td>#{managerName}</td>' +
    '</tr>';

function getClientList(obj, pageFun) {
    var scopeType = $('#scopeId').find('.active').attr('otherVal');
    $.get(
        "/stats/client/clientList",
        {"pageNum": obj['pageNum'], "pageSize": obj['pageSize'], "scopeType": scopeType},
        function(data) {
            if(data.errCode === '000000') {
                var result = data.dataMap;
                var total = result.total;
                var pages = result.pages;
                var list = data.dataMap.list;
                $("#dataBody").empty();
                for(var d in list) {
                    var row = rowStr.replace("#{registerDate}", list[d].registerDate)
                    .replace("#{corpName}", list[d].corpName)
                    .replace("#{shortName}", list[d].shortName)
                    .replace("#{username}", list[d].username)
                    .replace("#{managerName}", list[d].managerName);
                    $("#dataBody").append(row);
                }
                if(typeof pageFun === 'function') {
                    $('#chartTitleId').text(result.title);
                    pageFun(obj,pages,total);
                }
            }
        }
    );
}

$('#scopeId span').click(function(){
    $('#scopeId span').each(function(){
        $(this).removeClass('active');
    });
    $(this).addClass('active');
    customerDataInit();
    showChart();
});

function clientOutPrint(){
    var scopeType = $('#scopeId').find('.active').attr('otherVal');
    location.href='/stats/client/clientList/export?scopeType='+scopeType;
}



