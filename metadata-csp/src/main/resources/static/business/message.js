var form;
layui.use('form', function() {
    form = layui.form;
});
$(function() {
    localStorage.setItem("have_message", "0");
    $("#li-message-id").addClass("active");
    var obj = {
        pageNum: 1,
        pageSize: 10
    };
    getMessageList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getMessageList(pageObj);
            }
        })
    });
});
var rowStr = '<tr><td>#{addAt}</td><td>#{typeName}</td><td>#{content}</td></tr>';

function getMessageList(obj, pageFun) {
    $.get(
        "/client/message",
        {"pageNum": obj['pageNum'], "pageSize": obj['pageSize']},
        function(res) {
            var total = res.total;
            var pages = Math.floor((total - 1) / obj['pageSize']) + 1;
            var list = res.list;
            $("#dataBody").empty();
            for(var d in list) {
                var row = rowStr.replace("#{addAt}", list[d].addAt)
                .replace("#{typeName}", list[d].type)
                .replace("#{content}", list[d].content);
                $("#dataBody").append(row);
            }
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
        }
    );
}