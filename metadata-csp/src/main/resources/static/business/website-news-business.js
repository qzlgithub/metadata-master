$(function() {
    var obj = {
        pageNum: 1,
        pageSize: 4
    };
    getNewsList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                pageObj['pageNum'] = currentPage;
                getNewsList(pageObj);
            }
        })
    });
    $("#newsList").on("click", ".more-detail", function() {
        var dataId = $(this).attr("data-id");
        getNewsDetail(dataId);
    });
    $(".news-back").on("click",function(){
        $("#divDetail").hide();
        $("#divList").show();
    });
});

function getNewsDetail(id){
    $.get(
        "/api/news/detail",
        {"id": id},
        function(res) {
            var data = res.data;
            $("#newsTitle").html(data.title);
            $("#newsDate").html(data.publishTime + (data.author == ""?"":("&nbsp;&nbsp;"+data.author)));
            $("#newsContent").html(data.content);
            $("#divList").hide();
            $("#divDetail").show();
        }
    );
}

function getNewsList(obj, pageFun) {
    $.get(
        "/api/news/list",
        {"type": 1, "pageNum": obj['pageNum'], "pageSize": obj['pageSize']},
        function(res) {
            var total = res.total;
            var pages = res.data.pages;
            var list = res.list
            $("#newsList").empty();
            var htmlStr = '';
            for(var d in list) {
                htmlStr += '<li class="row">';
                htmlStr += '<div class="col-md-3"><img src="' + list[d].imagePath + '"/></div>';
                htmlStr += '<div class="col-md-9">';
                htmlStr += '<p class="title">' + list[d].title + '</p>';
                htmlStr += '<p class="time">发布时间：' + list[d].publishTime + '</p>';
                htmlStr += '<p>' + list[d].synopsis + '<a href="javascript:" class="more-detail" data-id="' + list[d].id + '">【更多】</a></p>';
                htmlStr += '</div>';
                htmlStr += '</li>';
            }
            $("#newsList").append(htmlStr);
            if(typeof pageFun === 'function') {
                pageFun(obj, pages, total);
            }
        }
    );
}