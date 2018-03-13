var form;
layui.use('form', function() {
    form = layui.form;
});
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
$("#include-opened").change(function() {
    do_change_parameter();
});
$(function() {
    do_change_parameter();
});

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

function fetch_product_list(obj, page_function) {
    var product_id_list = [];
    $(".c-product-type").each(function() {
        if($(this).hasClass("active")) {
            product_id_list.push($(this).attr("data-id"));
        }
    });
    var inc_opened = $("#include-opened").is(":checked");
    $.get(
        "/product/all",
        {
            "productType": product_id_list.join(","),
            "incOpened": inc_opened ? 1 : 0,
            "pageNum": obj['pageNum'],
            "pageSize": obj['pageSize']
        },
        function(res) {
            var total = res.total;
            var pages = res.data.pages;
            var list = res.list;
            $("#dataBody").empty();
            var htmlStr = '<ul class="row mr0 ml0" >';
            for(var d in list) {
                htmlStr += '<li>';
                if(typeof list[d].status === 'undefined') {
                    htmlStr += '<div class="serve-list-title">' + list[d].name + '<span class="fr">未开通</span></div>';
                    htmlStr += '<p class="col2 tl fz-14 p10 important" style="height: 89px; overflow: hidden;">' + list[d].remark + '</p>';
                    htmlStr += '<p class="tc mb15 mt10"><a target="_blank" class="setting-qq-class" href="javascript:"><i class="icon kefu mr5"></i>开通</a> ｜ <a href="/product/introduce.html?id=' + list[d].productId + '">查看详情</a></p>';
                }
                else {
                    htmlStr += '<div class="serve-list-title">' + list[d].name;
                    if(list[d].status === 0) {
                        htmlStr += '<span class="fr">服务中</span>';
                    }
                    else if(list[d].status === 1) {
                        htmlStr += '<i class="wks-icon icon" ></i>';
                    }
                    else if(list[d].status === 2) {
                        htmlStr += '<i class="jjgq-icon icon" ></i>';
                    }
                    else if(list[d].status === 3) {
                        htmlStr += '<i class="ygq-icon icon" ></i>';
                    }
                    else if(list[d].status === 4) {
                        htmlStr += '<i class="yebz-icon icon" ></i>';
                    }
                    else if(list[d].status === 5) {
                        htmlStr += '<i class="yqf-icon icon" ></i>';
                    }
                    htmlStr += '</div>'
                    if(list[d].billPlan === 1) {
                        htmlStr += '<p class="col2 tc fz-14 important">' + list[d].fromDate + ' - ' + list[d].toDate + '</p>';
                        htmlStr += '<p class="fz-20 col5 tc important">剩余' + list[d].remainDays + '天</p>';
                    }
                    else {
                        htmlStr += '<p class="col2 tc fz-14 important">当前余额：￥' + list[d].balance + '</p>';
                        htmlStr += '<p class="fz-20 col5 tc important">' + list[d].unitAmt + '元/次</p>';
                    }
                    htmlStr += '<p class="tc mb15 mt10"><a target="_blank" class="setting-qq-class" href="javascript:"><i class="icon kefu mr5"></i>续约</a> ｜ <a href="/product/detail.html?id=' + list[d].productId + '">查看服务</a>'
                }
                htmlStr += '</li>';
            }
            $("#dataBody").append(htmlStr);
            if(typeof page_function === 'function') {
                page_function(obj, pages, total);
            }
            settingQq();
        }
    );
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
