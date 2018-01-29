$(function() {
    accoConsumeListInit();
});

function accoConsumeListInit(){
    var obj = {
        pageNum: 1,
        pageSize: 10,
        clientId: $("#client-id").val(),
        startTime: $("#start-time").val(),
        endTime: $("#end-time").val()
    };
    getAccoConsumeList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getAccoConsumeList(obj);
            }
        })
    });
}

var
    rowTr = "<tr>&lt;!&ndash;<td>#{createTime}</td><td>#{tradeNo}</td><td>#{clientName}</td><td>#{shortName}</td><td>#{username}</td><td>#{purpose}</td><td>#{productName}</td><td>#{consume}</td><td>#{balance}</td>&ndash;&gt;</tr>";

function getAccoConsumeList(obj, pageFun) {
    $.get("/client/account/consumeList",
        {
            "clientId": obj['clientId'],
            "startTime": obj['startTime'],
            "endTime": obj['endTime'],
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize']
        }
        , function(data) {
            var list = data.list;
            var total = data.total;
            var pages = data.pages;
            $("#dataBody").empty();
            for(var d in list) {
                //alert(list[d].username);
                var tr = rowTr.replace("#{createTime}", list[d].createTime)
                .replace("#{tradeNo}", list[d].tradeNo)
                .replace("#{clientName}", list[d].clientName)
                .replace("#{shortName}", list[d].shortName)
                .replace("#{username}", list[d].username)
                .replace("#{purpose}", list[d].purpose)
                .replace("#{productName}", list[d].productName)
                .replace("#{consume}", list[d].consume)
                .replace("#{balance}", list[d].balance)
                $("#dataBody").append(tr);
            }
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
        });
}
