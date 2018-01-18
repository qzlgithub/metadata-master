$(function() {
    doChangeParameter();
});

function getAllProductList(obj, pageFun) {
    $.get(
        "/product/allList",
        {"selectedType": obj['selectedType'],"isOpen": obj['isOpen'],"pageNum": obj['pageNum'], "pageSize": obj['pageSize']},
        function(data) {
            if(data.errCode === '000000') {
                var result = data.dataMap;
                var total = result.total;
                var pages = result.pages;
                var list = data.dataMap.list;
                $("#dataBody").empty();
                var htmlStr = '<ul class="row mr0 ml0" >';
                for(var d in list) {
                    console.log(list[d]);
                    htmlStr += '<li>';
                    if(typeof list[d].status === 'undefined'){
                        htmlStr += '<div class="serve-list-title">'+list[d].name+'<span class="fr">未开通</span></div>';
                        htmlStr += '<p class="col2 tl fz-14 p10 important">'+list[d].remark+'</p>';
                        htmlStr += '<p class="tc mb15 mt10"><a href=""><i class="icon kefu mr5"></i>开通</a> ｜ <a href="">查看详情</a></p>';
                    }else{
                        htmlStr += '<div class="serve-list-title">'+list[d].name;
                        if(list[d].status === 0){
                            htmlStr += '<span class="fr">服务中</span>';
                        }else if(list[d].status === 1){
                            htmlStr += '<i class="wks-icon icon" ></i>';
                        }else if(list[d].status === 2){
                            htmlStr += '<i class="jjgq-icon icon" ></i>';
                        }else if(list[d].status === 3){
                            htmlStr += '<i class="ygq-icon icon" ></i>';
                        }else if(list[d].status === 4){
                            htmlStr += '<i class="yebz-icon icon" ></i>';
                        }else if(list[d].status === 5){
                            htmlStr += '<i class="yqf-icon icon" ></i>';
                        }
                        htmlStr += '</div>'
                        if(list[d].billPlan === 1){
                            htmlStr += '<p class="col2 tc fz-14 important">'+list[d].fromDate+' - '+list[d].toDate+'</p>';
                            htmlStr += '<p class="fz-20 col5 tc important">剩余'+list[d].remainDays+'天</p>';
                        }else{
                            htmlStr += '<p class="col2 tc fz-14 important">当前余额：￥'+list[d].balance+'</p>';
                            htmlStr += '<p class="fz-20 col5 tc important">'+list[d].unitAmt+'元/次</p>';
                        }
                        htmlStr += '<p class="tc mb15 mt10"><a href=""><i class="icon kefu mr5"></i>续约</a> ｜ <a href="">查看服务</a>'
                    }
                    htmlStr += '</li>';
                }
                $("#dataBody").append(htmlStr);
                if(typeof pageFun === 'function') {
                    pageFun(obj, pages, total);
                }
            }
        }
    );
}

$("li").click(function() {
    if($(this).hasClass('active')) {
        $(this).removeClass('active');
        doChangeParameter();
    }
    else {
        $(this).addClass('active');
        doChangeParameter();
    }
});

function doChangeParameter() {
    var selectedType = "";
    $('.productTypeClass').each(function() {
        if($(this).hasClass('active')) {
            if(selectedType === ""){
                selectedType += $(this).find('.productTypeCodeClass').val();
            }else{
                selectedType += "," + $(this).find('.productTypeCodeClass').val();
            }
        }
    });
    var isOpen = 0;
    if($('#isOpenId').is(':checked')) {
        isOpen = 1;
    }
    var obj = {
        selectedType : selectedType,
        isOpen : isOpen,
        pageNum: 1,
        pageSize: 8,
        init : true
    };
    getAllProductList(obj, function(pageObj, pages, total) {
        $('#pagination').paging({
            initPageNo: pageObj['pageNum'],
            totalPages: pages,
            totalCount: '合计' + total + '条数据',
            slideSpeed: 600,
            jump: false,
            callback: function(currentPage) {
                if(pageObj['init']){
                    pageObj['init'] = false;
                }else{
                    pageObj['pageNum'] = currentPage;
                    getAllProductList(pageObj);
                }
            }
        })
    });
}