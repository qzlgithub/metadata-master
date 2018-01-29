$(function() {
    prodConsumeListInit();
});

function prodConsumeListInit(){
    var obj = {
        pageNum: 1,
        pageSize: 10,
        productId: $("#product").val(),
        clientId: $("#client-id").val(),
        startTime: $("#start-time").val(),
        endTime: $("#end-time").val()
    };
    getProdConsumeList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getProdConsumeList(obj);
            }
        })
    });
}

var
    rowTr =
        "<tr>&lt;!&ndash;<td>#{createTime}</td><td>#{tradeNo}</td><td>#{clientName}</td><td>#{shortName}</td><td>#{username}</td><td>#{productName}</td><td>#{pillBlan}</td><td>#{enabled}</td><td>#{unitAmt}</td><td>#{amount}</td>&ndash;&gt;</tr>";

function getProdConsumeList(obj, pageFun) {
    $.get("/client/consumeList",
        {
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize'],
            "productId": obj['productId'],
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
                .replace("#{productName}", list[d].productName)
                .replace("#{pillBlan}", list[d].pillBlan)
                .replace("#{enabled}", list[d].enabled === '0' ? "是" : "否")
                .replace("#{unitAmt}", list[d].unitAmt)
                .replace("#{amount}", list[d].amount)
                $("#dataBody").append(tr);
            }
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
        });
}
