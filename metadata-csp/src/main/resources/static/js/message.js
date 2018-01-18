$(function() {
    var obj = {
        pageNum: 1,
        pageSize: 10,
        init : true
    };
    getMessageList(obj,function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                if(pageObj['init']){
                    pageObj['init'] = false;
                }else {
                    pageObj['pageNum'] = currentPage;
                    getMessageList(pageObj);
                }
            }
        })
    });
});

var rowStr = '<tr><td>#{addAt}</td><td>#{typeName}</td><td>#{content}</td></tr>';

function getMessageList(obj, pageFun){
    $.get(
        "/client/message",
        {"pageNum": obj['pageNum'], "pageSize": obj['pageSize']},
        function(data) {
            if(data.errCode === '000000') {
                var result = data.dataMap;
                var total = result.total;
                var pages = result.pages;
                var pageNum = result.pageNum;
                var list = data.dataMap.list;
                $("#dataBody").empty();
                for(var d in list) {
                    var row = rowStr.replace("#{addAt}", list[d].addAt)
                    .replace("#{typeName}", list[d].type)
                    .replace("#{content}", list[d].content);
                    $("#dataBody").append(row);
                }
                if(typeof pageFun === 'function') {
                    pageFun(obj,pages,total);
                }
            }
        }
    );
}