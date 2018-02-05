// $(".date").datepicker({dateFormat: "yy-mm-dd"});
var message;
layui.config({
    base: '../../static/build/js/'
}).use(['app', 'message', 'laydate'], function() {
    var app = layui.app,
        $ = layui.jquery,
        layer = layui.layer;
    laydate = layui.laydate;
    //将message设置为全局以便子页面调用
    message = layui.message;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    $('#pay').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: '<img src="../../static/build/images/pay.png"/>',
            area: ['500px', '250px'],
            shadeClose: true
        });
    });
    laydate.render({
        elem: '#start-time'
    });
    laydate.render({
        elem: '#end-time'
    });
});
var dateFormat = "yy-mm-dd",
    from = $("#start-time").datepicker({
        dateFormat: dateFormat,
        defaultDate: "+1w",
        changeMonth: true
    }).on("change", function() {
        to.datepicker("option", "minDate", getDate(this));
    }),
    to = $("#end-time").datepicker({
        dateFormat: dateFormat,
        defaultDate: "+1w",
        changeMonth: true
    })
    .on("change", function() {
        from.datepicker("option", "maxDate", getDate(this));
    });

function getDate(element) {
    var date;
    try {
        date = $.datepicker.parseDate(dateFormat, element.value);
    }
    catch(error) {
        date = null;
    }
    return date;
}

$(function() {
    prodRechargeListInit();
});

function prodRechargeListInit(){
    var obj = {
        pageNum: 1,
        pageSize: 10,
        clientId: $("#client-id").val(),
        productId: $("#product").val(),
        startTime: $("#start-time").val(),
        endTime: $("#end-time").val()
    };
    getProdRechargeList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getProdRechargeList(obj);
            }
        })
    });
}

var rowTr = "<tr>&lt;!&ndash;<td>#{createTime}</td><td>#{tradeNo}</td><td>#{clientName}</td><td>#{shortName}</td><td>#{username}</td><td>#{productName}</td><td>#{rechargeType}</td><td>#{amount}</td><td>#{balance}</td><td>#{manager}</td><td>#{contractNo}</td><td>#{remark}</td>&ndash;&gt;</tr>";

function getProdRechargeList(obj,pageFun) {
    $.get(
        "/client/rechargeList",
        {
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize'],
            "clientId": obj['clientId'],
            "productId": obj['productId'],
            "startTime": obj['startTime'] != ''? obj['startTime'] + " 00:00:00":'',
            "endTime": obj['endTime'] != ''? obj['endTime'] + " 23:59:59":''
        },
        function(data) {
            var list = data.list;
            var total = data.total;
            var pages = data.pages;
            $("#dataBody").empty();
            for(var d in list) {
                var tr = rowTr.replace("#{createTime}", list[d].tradeAt)
                .replace("#{tradeNo}", list[d].tradeNo)
                .replace("#{clientName}", list[d].corpName)
                .replace("#{shortName}", list[d].shortName)
                .replace("#{username}", list[d].username)
                .replace("#{productName}", list[d].productName)
                .replace("#{rechargeType}", list[d].rechargeType)
                .replace("#{amount}", list[d].amount)
                .replace("#{balance}", list[d].balance)
                .replace("#{manager}", list[d].managerName)
                .replace("#{contractNo}", list[d].contractNo)
                .replace("#{remark}", list[d].remark);
                $("#dataBody").append(tr);
            }
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
        });
}

function exportToExcel() {
    var clientId = $("#client-id").val();
    var productId = $("#product").val();
    var startTime = $("#start-time").val();
    var endTime = $("#end-time").val();
    location.href = "/client/product/recharge/export?clientId=" + clientId + "&productId=" + productId + "&startTime=" + startTime + "&endTime=" + endTime;
}