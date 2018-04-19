layui.config({
    base: '../../static/build/js/'
}).use(['app', 'form', 'laydate'], function() {
    var app = layui.app, form = layui.form, laydate = layui.laydate,
        $ = layui.jquery;
    //主入口
    app.set({
        type: 'iframe'
    }).init();
    laydate.render({
        elem: '#dates',
        range: true,
        done: function(value, date) {
            if(value != "") {
                var dates = value.split(" - ");
                $("#fromDate").val(dates[0]);
                $("#toDate").val(dates[1]);
            }
            else {
                $("#fromDate").val("");
                $("#toDate").val("");
            }
            refreshChart();
        }
    });
    laydate.render({
        elem: '#contrastDate',
        done: function(value, date) {
            refreshChart();
        }
    });
    //对比时间
    $(".contrast-time2").change(function() {
        $(".time-input2").toggle();
    });
    form.on('checkbox(allChoose)', function(data) {
        var child = $('#pro_type').find('li input[type="checkbox"]:not([name="show"])');
        child.each(function(index, item) {
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });
    var childCount = $("#pro_type li").length;
    form.on("checkbox(choose)", function(data) {
        var childChecked = $(data.elem).parents('#pro_type').find('li input[type="checkbox"]:checked');
        if(childChecked.length == childCount) {
            $(data.elem).parents('.product-nav1').find('span input#allChoose').get(0).checked = true;
        }
        else {
            $(data.elem).parents('.product-nav1').find('span input#allChoose').get(0).checked = false;
        }
        form.render('checkbox');
    })
});
//统计产品 点击下拉
$(document).ready(function() {
    $(".product-title1").click(function() {
        $(".product-tjlist1").toggle();
    });
    $(".nav-close1").click(function() {
        $(".product-tjlist1").hide();
    })
});
$(document).ready(function() {
    $(".product-title2").click(function() {
        $(".product-tjlist2").toggle();
    });
    $(".nav-close2").click(function() {
        $(".product-tjlist2").hide();
    })
});

function refreshChart(){

}