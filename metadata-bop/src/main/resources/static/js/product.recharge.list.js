$(".date").datepicker({dateFormat: "yy-mm-dd"});
var pageLink = '<a href="javascript:goPage(#{pageNum});">#{pageNum}</a>';
$(function() {
    getProdRechargeList($("#client-id").val(), null, null, null, 1, $("#pageSize").val());
});

var
    rowTr = "<tr>&lt;!&ndash;<td>#{createTime}</td><td>#{tradeNo}</td><td>#{clientName}</td><td>#{shortName}</td><td>#{username}</td><td>#{productName}</td><td>#{rechargeType}</td><td>#{amount}</td><td>#{balance}</td><td>#{manager}</td><td>#{contractNo}</td><td>#{remark}</td>&ndash;&gt;</tr>";

function getProdRechargeList(clientId, productId, startTime, endTime, pageNumVal, pageSizeVal) {
    $("#pageNum").val(pageNumVal);
    $.get(
        "/client/rechargeList",
        {
            "clientId": clientId,
            "productId": productId,
            "startTime": startTime,
            "endTime": endTime,
            "pageNum": pageNumVal,
            "pageSize": pageSizeVal
        },
        function(data) {
            var list = data.list;
            $("#dataBody").empty();
            for(var d in list) {
                //alert(list[d].username);
                var tr = rowTr.replace("#{createTime}", list[d].tradeAt)
                .replace("#{tradeNo}", list[d].tradeNo)
                .replace("#{clientName}", list[d].corpName)
                .replace("#{shortName}", list[d].shortName)
                .replace("#{username}", list[d].username)
                .replace("#{productName}", list[d].productName)
                .replace("#{rechargeType}", list[d].rechargeType)
                .replace("#{amount}", list[d].amount)
                .replace("#{balance}", list[d].balance)
                .replace("#{manager}", list[d].managerName)
                .replace("#{contractNo}", list[d].contractNo)
                .replace("#{remark}", list[d].remark);
                $("#dataBody").append(tr);
            }
            $("#total").text("共 " + data.total + " 条");
            $("#pages").text("共 " + data.pages + " 页");
            $("#totalPage").val(data.pages);
            $("#currentPage").text(data.pageNum);
            if(data.pageNum === 1) {
                $("#prevPage").addClass("layui-disabled");
            }
            else {
                $("#prevPage").removeClass("layui-disabled");
            }
            if(data.pageNum === data.pages) {
                $("#nextPage").addClass("layui-disabled");
            }
            else {
                $("#nextPage").removeClass("layui-disabled");
            }
            refreshPageInfo($("#frontPages"), $("#nextPages"), data.pageNum, data.pages);
        });
}

//<![CDATA[
function refreshPageInfo(front, next, pageNum, totalPage) {
    /*var pageLink = '<a href="javascript:goPage(#{pageNum});">#{pageNum}</a>';*/
    front.empty();
    next.empty();
    if(pageNum > 1) {
        front.append(pageLink.replace(/#{pageNum}/g, 1));
    }
    if(pageNum === 3) {
        front.append(pageLink.replace(/#{pageNum}/g, 2));
    }
    else if(pageNum > 3) {
        front.append("...");
        front.append(pageLink.replace(/#{pageNum}/g, pageNum - 1));
    }
    if(totalPage === pageNum + 2) {
        next.append(pageLink.replace(/#{pageNum}/g, totalPage - 1));
    }
    else if(totalPage > pageNum + 2) {
        next.append(pageLink.replace(/#{pageNum}/g, pageNum + 1));
        next.append("...");
    }
    if(pageNum < totalPage) {
        next.append(pageLink.replace(/#{pageNum}/g, totalPage));
    }
}

function gotoPrev() {
    var pageNum = $("#pageNum").val();
    if(pageNum > 1) {
        var clientId = $("#client-id").val();
        var productId = $("#product").val();
        var startTime = $("#start-time").val();
        var endTime = $("#end-time").val();
        var pageNumVal = pageNum - 1;
        var pageSizeVal = $("#pageSize").val();
        getProdRechargeList(clientId, productId, startTime, endTime, pageNumVal, pageSizeVal);
    }
}

function gotoNext() {
    var pageNum = $("#pageNum").val();
    var totalPage = $("#totalPage").val();
    if(pageNum !== totalPage) {
        var clientId = $("#client-id").val();
        var productId = $("#product").val();
        var startTime = $("#start-time").val();
        var endTime = $("#end-time").val();
        var pageNumVal = parseInt(pageNum) + 1;
        var pageSizeVal = $("#pageSize").val();
        getProdRechargeList(clientId, productId, startTime, endTime, pageNumVal, pageSizeVal);
    }
}

function searchProdRechargeList() {
    var clientId = $("#client-id").val();
    var productId = $("#product").val();
    var startTime = $("#start-time").val();
    var endTime = $("#end-time").val();
    var pageNumVal = 1;
    var pageSizeVal = $("#pageSize").val();
    getProdRechargeList(clientId, productId, startTime, endTime, pageNumVal, pageSizeVal);
}

function goPage(pageNum) {
    var clientId = $("#client-id").val();
    var productId = $("#product").val();
    var startTime = $("#start-time").val();
    var endTime = $("#end-time").val();
    var pageNumVal = pageNum;
    var pageSizeVal = $("#pageSize").val();
    getProdRechargeList(clientId, productId, startTime, endTime, pageNumVal, pageSizeVal);
}

function gotoPage() {
    var clientId = $("#client-id").val();
    var productId = $("#product").val();
    var startTime = $("#start-time").val();
    var endTime = $("#end-time").val();
    var pageNumVal = $("#userPageNum").val();
    var pageSizeVal = $("#pageSize").val();
    getProdRechargeList(clientId, productId, startTime, endTime, pageNumVal, pageSizeVal);
}

//]]>