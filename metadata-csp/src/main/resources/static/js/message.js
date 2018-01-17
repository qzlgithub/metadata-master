$(function() {
    var obj = {
        pageNum: 1,
        pageSize: 10
    };
    getMessageListInit(obj);
});

var rowStr = '<tr><td>#{addAt}</td><td>#{typeName}</td><td>#{content}</td></tr>';

function getMessageListInit(obj){
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
                $('#pagination').paging({
                    initPageNo: pageNum, // 初始页码
                    totalPages: pages, //总页数
                    totalCount: '合计' + total + '条数据', // 条目总数
                    slideSpeed: 600, // 缓动速度。单位毫秒
                    jump: false, //是否支持跳转
                    callback: function(currentPage) { // 回调函数
                        obj['pageNum'] = currentPage;
                        getMessageList(obj);
                    }
                })
            }
        }
    );
}

function getMessageList(obj){
    $.get(
        "/client/message",
        {"pageNum": obj['pageNum'], "pageSize": obj['pageSize']},
        function(data) {
            if(data.errCode === '000000') {
                var list = data.dataMap.list;
                $("#dataBody").empty();
                for(var d in list) {
                    var row = rowStr.replace("#{addAt}", list[d].addAt)
                    .replace("#{typeName}", list[d].type)
                    .replace("#{content}", list[d].content);
                    $("#dataBody").append(row);
                }
            }
        }
    );
}