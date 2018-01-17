$(function() {
    var obj = {
        pageNum: 1,
        pageSize: 10
    };
    getClientListInit(obj, function(pageNum,pages,total) {
        $('#pagination').paging({
            initPageNo: pageNum, // 初始页码
            totalPages: pages, //总页数
            totalCount: '合计' + total + '条数据', // 条目总数
            slideSpeed: 600, // 缓动速度。单位毫秒
            jump: false, //是否支持跳转
            callback: function(currentPage) { // 回调函数
                obj['pageNum'] = currentPage;
                getClientListInit(obj);
            }
        })
    });
});
var rowStr = '<tr>' +
    '<td>#{registerDate}</td>' +
    '<td>#{corpName}</td>' +
    '<td>#{shortName}</td>' +
    '<td>#{username}</td>' +
    '<td>#{name}</td>' +
    '<td>#{phone}</td>' +
    '<td>#{email}</td>' +
    '<td>#{managerName}</td>' +
    '</tr>';

function getClientListInit(obj, fun) {
    var scopeType = $('#scopeId').find('.active').attr('otherVal');
    $.get(
        "/stats/client/clientList",
        {"pageNum": obj['pageNum'], "pageSize": obj['pageSize'], "scopeType": scopeType},
        function(data) {
            if(data.errCode === '000000') {
                var result = data.dataMap;
                var total = result.total;
                var pages = result.pages;
                var pageNum = result.pageNum;
                var list = data.dataMap.list;
                $("#dataBody").empty();
                for(var d in list) {
                    var row = rowStr.replace("#{registerDate}", list[d].registerDate)
                    .replace("#{corpName}", list[d].corpName)
                    .replace("#{shortName}", list[d].shortName)
                    .replace("#{username}", list[d].username)
                    .replace("#{name}", list[d].name)
                    .replace("#{phone}", list[d].phone)
                    .replace("#{email}", list[d].email)
                    .replace("#{managerName}", list[d].managerName);
                    $("#dataBody").append(row);
                }
                if(typeof fun === 'function') {
                    fun(pageNum,pages,total);
                }
            }
        }
    );
}

