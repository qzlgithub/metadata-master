$(function() {
    customerDataInit();
    showChart();
});
function showChart(){
    var leftChart = echarts.init(document.getElementById('leftPanel'));
    var rightChart = echarts.init(document.getElementById('rightPanel'));
    var scopeType = $('#scopeId').find('.active').attr('otherVal');
    $.get('/stats/client/rechargeListJson',
        {"scopeType": scopeType},
        function (data) {
            var jsonObj = JSON.parse(data);
            leftChart.setOption({
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:jsonObj.leftJson.legendData
                },
                series: [
                    {
                        name:'访问来源',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '30',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:jsonObj.leftJson.data
                    }
                ]
            });
            rightChart.setOption({
                tooltip : {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow',
                        label: {
                            show: true
                        }
                    }
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                legend: {
                    data:['Growth', 'Budget 2011', 'Budget 2012'],
                    itemGap: 5
                },
                grid: {
                    top: '12%',
                    left: '1%',
                    right: '10%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type : 'category',
                        data : jsonObj.rightJson.names
                    }
                ],
                yAxis: [
                    {
                        type : 'value',
                        name : 'Budget (million USD)',
                        axisLabel: {
                            formatter: function (a) {
                                a = +a;
                                return isFinite(a)
                                    ? echarts.format.addCommas(+a / 1000)
                                    : '';
                            }
                        }
                    }
                ],
                dataZoom: [
                    {
                        show: true,
                        start: 94,
                        end: 100
                    },
                    {
                        type: 'inside',
                        start: 94,
                        end: 100
                    },
                    {
                        show: true,
                        yAxisIndex: 0,
                        filterMode: 'empty',
                        width: 30,
                        height: '80%',
                        showDataShadow: false,
                        left: '93%'
                    }
                ],
                series : jsonObj.rightJson.series
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
    '<td>#{tradeTime}</td>' +
    '<td>#{corpName}</td>' +
    '<td>#{shortName}</td>' +
    '<td>#{username}</td>' +
    '<td>#{productName}</td>' +
    '<td>#{amount}</td>' +
    '<td>#{rechargeType}</td>' +
    '<td>#{balance}</td>' +
    '<td>#{managerName}</td>' +
    '</tr>';

function getClientList(obj, pageFun) {
    var scopeType = $('#scopeId').find('.active').attr('otherVal');
    $.get(
        "/stats/client/rechargeList",
        {"pageNum": obj['pageNum'], "pageSize": obj['pageSize'], "scopeType": scopeType},
        function(data) {
            if(data.errCode === '000000') {
                var result = data.dataMap;
                var total = result.total;
                var pages = result.pages;
                var list = data.dataMap.list;
                $("#dataBody").empty();
                for(var d in list) {
                    var row = rowStr.replace("#{tradeTime}", list[d].tradeTime)
                    .replace("#{corpName}", list[d].corpName)
                    .replace("#{shortName}", list[d].shortName)
                    .replace("#{username}", list[d].username)
                    .replace("#{productName}", list[d].productName)
                    .replace("#{amount}", list[d].amount)
                    .replace("#{rechargeType}", list[d].rechargeType)
                    .replace("#{balance}", list[d].balance)
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
    location.href='/stats/client/rechargeList/export?scopeType='+scopeType;
}



