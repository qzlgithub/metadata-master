$(function() {
    var obj = {
        pageNum: 1,
        pageSize: 3
    };
    getNewsList(obj);
});

function getNewsList(obj) {
    $.get(
        "/api/news/list",
        {"pageNum": obj['pageNum'], "pageSize": obj['pageSize']},
        function(res) {
            var list = res.list;
            $("#newsList").empty();
            var htmlStr = '';
            for(var d in list) {
                htmlStr += '<li>';
                htmlStr += '<p style="height:210px; overflow: hidden "><img src="' + list[d].imagePath + '"/></p>';
                htmlStr += '<p class="fz-16 col4 mt10">' + list[d].title + '</p>';
                htmlStr += '<p class="col2 mt5">' + list[d].synopsis + '</p>';
                htmlStr += '</li>';
            }
            $("#newsList").append(htmlStr);
        }
    );
}