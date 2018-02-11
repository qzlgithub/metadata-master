var form, message, table, main_table;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'table', 'message'], function() {
    var app = layui.app;
    app.set({type: 'iframe'}).init();
    message = layui.message;
    form = layui.form;
    table = layui.table;
    main_table = table.render({
        elem: '#data-table',
        page: true,
        limit: 3,
        limits: [3, 15, 30, 50],
        url: '/manager/list',
        where: {
            roleId: $("#roleId").val(),
            enabled: $("#enabled").val()
        },
        cols: [[
            {field: 'id', title: '编号', width: '15%'},
            {field: 'username', title: '用户名', width: '10%'},
            {field: 'name', title: '姓名', width: '15%'},
            {field: 'phone', title: '联系电话', width: '10%'},
            {field: 'roleName', title: '角色', width: '15%'},
            {title: '状态', width: '10%',templet: "#status-tpl"},
            {field: 'registerDate', title: '注册日期', sort: true, width: '10%'},
            {title: '操作', align: 'center', toolbar: '#operation-bar', fixed: 'right'}
        ]],
        request: {
            pageName: 'pageNum', limitName: 'pageSize'
        },
        response: {
            statusName: 'code',
            statusCode: 0,
            msgName: 'message',
            countName: 'total',
            dataName: 'list'
        }
    });
    form.on('submit(search)', function(data) {
        var params = data.field;
        main_table.reload({
            where: {
                roleId: params["roleId"],
                enabled: params['enabled']
            },
            page: {
                curr: 1
            }
        });
    });
});

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



