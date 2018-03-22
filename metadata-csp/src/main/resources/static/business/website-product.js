$(function() {
    findProductTypeList();
});

function findProductTypeList() {
    $.get(
        "/api/product/type",
        {},
        function(res) {
            var list = res.data.list;
            var htmlStr = '<li class="active" id="all-type">不限</li>';
            for(var d in list) {
                htmlStr += '<li class="c-product-type" data-id="' + list[d].key + '">' + list[d].value + '</li>';
            }
            $("#blistCity").append(htmlStr);
            $("#all-type").click(function() {
                if(!$(this).hasClass("active")) {
                    $(".c-product-type").removeClass("active");
                    $(this).addClass("active");
                    do_change_parameter();
                }
            });
            $(".c-product-type").click(function() {
                $("#all-type").removeClass("active");
                if($(this).hasClass("active")) {
                    $(this).removeClass("active");
                    check_all_type();
                }
                else {
                    $(this).addClass("active");
                }
                do_change_parameter();
            });
            do_change_parameter();
        }
    );
}

function check_all_type() {
    var all_checked = true;
    $(".c-product-type").each(function() {
        if($(this).hasClass("active")) {
            all_checked = false;
        }
    });
    if(all_checked) {
        $("#all-type").addClass("active");
    }
    else {
        $("#all-type").removeClass("active");
    }
}

function do_change_parameter() {
    var obj = {
        pageNum: 1,
        pageSize: 8
    };
    fetch_product_list(obj, function(page_obj, pages, total) {
        $('#pagination').paging({
            initPageNo: page_obj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                page_obj['pageNum'] = currentPage;
                fetch_product_list(page_obj);
            }
        })
    });
}

function fetch_product_list(obj, page_function) {
    var product_id_list = [];
    $(".c-product-type").each(function() {
        if($(this).hasClass("active")) {
            product_id_list.push($(this).attr("data-id"));
        }
    });
    $.get(
        "/api/product/list",
        {
            "productType": product_id_list.join(","),
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize']
        },
        function(res) {
            var total = res.total;
            var pages = res.data.pages;
            var list = res.list;
            $("#productList").empty();
            /*<dl>
            <dt>
            <p class="fb">金融预期常欠客</p>
                <p class="product-style mt5">类别：金融借贷</p>
            </dt>
            <dd>
            <p>专业帮您区分哪些客户经常被拒贷，为您放贷提供依据</p>
            <p class="more mt10"><a href="#">更多</a></p>
            </dd>
            </dl>*/
            var htmlStr = '';
            for(var d in list) {
                htmlStr += '<dl>';
                htmlStr += '<dt>';
                htmlStr += '<p class="fb">' + list[d].name + '</p>';
                htmlStr += '<p class="product-style mt5">类别：' + list[d].type + '</p>';
                htmlStr += '</dt>';
                htmlStr += '<dd>';
                htmlStr += '<p>' + list[d].remark + '</p>';
                htmlStr += '<p class="more mt10"><a href="/product/introduce.html?id=' + list[d].id + '">更多</a></p>';
                htmlStr += '</dd>';
                htmlStr += '</dl>';
            }
            $("#productList").append(htmlStr);
            if(typeof page_function === 'function') {
                page_function(obj, pages, total);
            }
        }
    );
}