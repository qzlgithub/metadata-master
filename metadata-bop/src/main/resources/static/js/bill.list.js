$(function() {
    var startDate = $("#startDate");
    var endDate = $("#endDate");
    new pickerDateRange('date1', {
        isTodayValid : true,
        startDate : startDate.val(),
        endDate : endDate.val(),
        needCompare : false,
        defaultText : ' 至 ',
        autoSubmit : true,
        inputTrigger : 'input_trigger1',
        theme : 'ta',
        success : function(obj) {
            startDate.val(obj.startDate);
            endDate.val(obj.endDate);
        }
    });
    billListInit();
});

function billListInit() {
    var obj = {
        pageNum: 1,
        pageSize: 10,
        shortName: $("#shortName").val().trim(),
        rechargeType: $("#rechargeType").val().trim(),
        productId: $("#productId").val().trim(),
        startDate: $("#startDate").val().trim(),
        endDate: $("#endDate").val().trim()
    };
    getBillList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getBillList(obj);
            }
        })
    });
}

var rowStr = '<tr>' +
    '<td>#{createTime}</td>' +
    '<td>#{tradeNo}</td>' +
    '<td>#{clientName}</td>' +
    '<td>#{shortName}</td>' +
    '<td>#{username}</td>' +
    '<td>#{productName}</td>' +
    '<td>#{billPlan}</td>' +
    '<td>#{hit}</td>' +
    '<td>#{unitAmt}</td>' +
    '<td>#{balance}</td>' +
    '</tr>';

function getBillList(obj, pageFun) {
    $.get(
        "/finance/billList",
        {
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize'],
            "shortName": obj['shortName'],
            "typeId": obj['rechargeType'],
            "productId": obj['productId'],
            "startDate": obj['startDate'] == '' ? '' : obj['startDate'] + " 00:00:00",
            "endDate": obj['endDate'] == '' ? '' : obj['endDate'] + " 23:59:59"
        },
        function(data) {
            if(data.errCode === '000000') {
                $("#showStats").text('');
                var result = data.dataMap;
                var total = result.total;
                var pages = result.pages;
                var list = data.dataMap.list;
                $("#dataBody").empty();
                for(var d in list) {
                    var row = rowStr.replace("#{createTime}", list[d].tradeAt)
                    .replace("#{tradeNo}", list[d].tradeNo)
                    .replace("#{clientName}", list[d].corpName)
                    .replace("#{shortName}", list[d].shortName)
                    .replace("#{username}", list[d].username)
                    .replace("#{productName}", list[d].productName)
                    .replace("#{billPlan}", list[d].billPlan)
                    .replace("#{hit}", list[d].hit)
                    .replace("#{unitAmt}", list[d].unitAmt)
                    .replace("#{balance}", list[d].balance);
                    $("#dataBody").append(row);
                }
                if(typeof pageFun === 'function') {
                    $('#chartTitleId').text(result.title);
                    pageFun(obj, pages, total);
                }
                if(obj['shortName'] != '') {
                    $("#showStats").text(data.dataMap.showStats);
                }
            }
        }
    );
}

function clientOutPrint() {
    var shortName = $("#shortName").val().trim();
    var rechargeType = $("#rechargeType").val().trim();
    var productId = $("#productId").val().trim();
    var startDate = $("#startDate").val().trim();
    var endDate = $("#endDate").val().trim();
    var url = '/finance/billList/export?shortName=' + shortName + "&rechargeType=" + rechargeType
        + "&productId=" + productId
        + "&startDate=" + (startDate == '' ? '' : startDate + " 00:00:00")
        + "&endDate=" + (endDate == '' ? '' : endDate + " 23:59:59");
    location.href = encodeURI(url);
}



