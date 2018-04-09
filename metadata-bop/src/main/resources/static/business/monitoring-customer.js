layui.config({
    base: '../../static/build/js/'
}).use(['app'], function() {
    var app = layui.app,
        $ = layui.jquery
    //主入口
    app.set({
        type: 'iframe'
    }).init();
});
$(function() {
    $(".choose-hide").click(function() {
        $(".third-select").hide();
    });
    $(".choose-show").click(function() {
        $(".third-select").show();
    });
});

function findCustomerAll() {
    /*<li><input type="checkbox" name="like1[write]" lay-skin="primary" title="写作"
    checked=""/></li>*/
    $.get(
        "/monitoring/customer/allCustomer",
        {},
        function(data) {
            var total = data.total;
            var list = data.list;
            var htmlStr = '';
            $("#customer-all-id").empty();
            for(var i in list){
                htmlStr+='<li>';
                htmlStr+='<input type="checkbox" name="" lay-skin="primary" title="写作" checked=""/>';
                htmlStr+='</li>';
            }
        }
    );
}