$(function() {
    accoRechargeListInit();
});

function accoRechargeListInit(){
    var obj = {
        pageNum: 1,
        pageSize: 10,
        clientId: $("#client-id").val(),
        startTime: $("#start-time").val(),
        endTime: $("#end-time").val()
    };
    getAccoRechargeList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getAccoRechargeList(obj);
            }
        })
    });
}

var
    rowTr = "<tr>&lt;!&ndash;<td>#{createTime}</td><td>#{tradeNo}</td><td>#{clientName}</td><td>#{shortName}</td><td>#{username}</td><td>#{amount}</td><td>#{rechargeType}</td><td>#{balance}</td><td>#{manager}</td><td>#{remark}</td>&ndash;&gt;</tr>";

function getAccoRechargeList(obj, pageFun) {
    $.get("/client/account/rechargeList",
        {
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize'],
            "clientId": obj['clientId'],
            "startTime": obj['startTime'],
            "endTime": obj['endTime']
        }
        , function(data) {
            var list = data.list;
            var total = data.total;
            var pages = data.pages;
            $("#dataBody").empty();
            for(var d in list) {
                var tr = rowTr.replace("#{createTime}", list[d].createTime)
                .replace("#{tradeNo}", list[d].tradeNo)
                .replace("#{clientName}", list[d].clientName)
                .replace("#{shortName}", list[d].shortName)
                .replace("#{username}", list[d].username)
                .replace("#{amount}", list[d].amount)
                .replace("#{rechargeType}", list[d].rechargeType)
                .replace("#{balance}", list[d].balance)
                .replace("#{manager}", list[d].manager)
                .replace("#{remark}", list[d].remark)
                $("#dataBody").append(tr);
            }
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
        });
}