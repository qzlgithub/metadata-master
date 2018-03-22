$(function() {
    changeLeft();
    $("#firstLeft").on("click", "li", function() {
        $(".new-list-child").removeClass("active");
        $(this).addClass("active");
        var text = $(this).find("a").text();
        $(".head-title").html(text);
        $("#divDetail").hide();
        $("#divList").show();
        changeLeft();
    });
    $("#secondLeft").on("click", "li", function() {
        $(".new-list-child").removeClass("active");
        $(this).addClass("active");
        var text = $(this).find("a").text();
        $(".head-title").html(text);
        $("#divDetail").hide();
        $("#divList").show();
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

function changeLeft() {
    var type = $("#firstLeft").find(".active").attr("data-id");
    var obj = {
        type: type,
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
}

function getNewsList(obj, pageFun) {
    $.get(
        "/api/news/list",
        {"type": obj['type'], "pageNum": obj['pageNum'], "pageSize": obj['pageSize']},
        function(res) {
            var total = res.total;
            var pages = res.data.pages;
            var list = res.list
            $("#newsList").empty();
            var htmlStr = '';
            /*<li class="row">
                <div class="col-md-3"><img src="/static/website/images/new-pic.png"/></div>
                <div class="col-md-9">
                <p class="title">比尔·盖茨：我讲述一个关于如何取得商业成功的真实故事</p>
            <p class="time">发布时间：2018-05-22</p>
            <p>
            在我读过的企业家传记里，许多都遵循了一种常规的故事情节，同时也是一种在我看来会
            误导大众的故事情节。它大概是这样的：一位敏锐的创业者想出一个改变世界的创意，制
            定了清晰的商业策略，招聘了一流的合作伙伴<a href="">【更多】</a>
            </p>
            </div>
            </li>*/
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