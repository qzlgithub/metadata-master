var form;
layui.config({
    base: '../../static/build/js/'
}).use(['form', 'app'], function() {
    var app = layui.app,
        $ = layui.jquery;
    form = layui.form;
    //详情鼠标经过
    getWarningList();
});

function getWarningList() {
    $.get(
        "/warning/setting",
        {},
        function(data) {
            var warningProduct = data.data.warningProduct;
            var warningClient = data.data.warningClient;
            var htmlStr = '';
            for(var i in warningProduct) {
                var item = warningProduct[i];
                htmlStr += '<tr>';
                htmlStr += '<td>' + item.content + '</td>';
                htmlStr += '<td class="lh25">';
                if(item.send == 1) {
                    htmlStr += '<div class="position-r">手机短信</div>';
                }
                if(item.play == 1) {
                    htmlStr += '<div class="position-r">现场警报';
                    htmlStr += '<i class="col3 fz-14 ml10 detail cp">详情</i>';
                    htmlStr += '<div class="detai-ceng">' + item.fileName + '</div>';
                    htmlStr += '</div>';
                }
                htmlStr += '</td>';
                htmlStr += '<td class="lh25">';
                htmlStr += '严重 ' + item.severityLimit + '<= 连续出错 ' + '<' + item.warningLimit + "<br/>";
                htmlStr += '一般 ' + item.generalLimit + '<= 连续出错 ' + '<' + item.severityLimit;
                htmlStr += '</td>';
                htmlStr += '<td>';
                htmlStr += '<a href="/warning/setting/edit.html?id=' + item.id + '" class="col4 important">编辑</a> | <a href="javascript:;" class="col3 important" attData="' + item.enabled + '" onclick="doChangeStatus(this,' + item.id + ')">' + (item.enabled == 1 ? "停用" : "启用") + '</a>';
                htmlStr += '</td>';
                htmlStr += '</tr>';
            }
            $("#product-warning").append(htmlStr);
            htmlStr = '';
            for(var i in warningClient) {
                var item = warningClient[i];
                htmlStr += '<tr>';
                htmlStr += '<td>' + item.content + '</td>';
                htmlStr += '<td class="lh25">';
                if(item.send == 1) {
                    htmlStr += '<div class="position-r">手机短信</div>';
                }
                if(item.play == 1) {
                    htmlStr += '<div class="position-r">现场警报';
                    htmlStr += '<i class="col3 fz-14 ml10 detail cp">详情</i>';
                    htmlStr += '<div class="detai-ceng">' + item.fileName + '</div>';
                    htmlStr += '</div>';
                }
                htmlStr += '</td>';
                htmlStr += '<td class="lh25">';
                htmlStr += '严重 ' + item.severityLimit + '<= 连续出错 ' + '<' + item.warningLimit + '<br/>';
                htmlStr += '一般 ' + item.generalLimit + '<= 连续出错 ' + '<' + item.severityLimit;
                htmlStr += '</td>';
                htmlStr += '<td>';
                htmlStr += '<a href="/warning/setting/edit.html?id=' + item.id + '" class="col4 important">编辑</a> | <a href="javascript:;" class="col3 important" attData="' + item.enabled + '" onclick="doChangeStatus(this,' + item.id + ')">' + (item.enabled == 1 ? "停用" : "启用") + '</a>';
                htmlStr += '</td>';
                htmlStr += '</tr>';
            }
            $("#client-warning").append(htmlStr);
            $("td div").mouseover(function() {
                $(this).find(".detai-ceng").show();
            }).mouseout(function() {
                $(this).find(".detai-ceng").hide();
            });
        }
    );
}

var isSubmit = false;

function doChangeStatus(obj, id) {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    var attData = $(obj).attr("attData");
    var status;
    if(attData == '0') {
        status = 1;
    }
    else {
        status = 0;
    }
    $.ajax({
        type: "POST",
        url: "/warning/setting/enabled",
        data: {"id": id, "status": status},
        success: function(res) {
            if(res.code !== "000000") {
                layer.msg("修改失败:" + res.message, {
                    time: 2000
                });
            }
            else {
                $(obj).attr("attData", status);
                if(status == '0') {
                    $(obj).text("启用");
                }
                else {
                    $(obj).text("停用");
                }
                layer.msg("修改成功", {
                    time: 2000
                });
            }
            isSubmit = false;
        }
    });
}