var form;
layui.config({
    base: '../../static/build/js/'
}).use(['form', 'app'], function() {
    var app = layui.app,
        $ = layui.jquery;
    form = layui.form;
    //详情鼠标经过
    getWarningList();
    $("td div").mouseover(function() {
        $(this).find(".detai-ceng").show();
    }).mouseout(function() {
        $(this).find(".detai-ceng").hide();
    });
});

function getWarningList() {
    $.get(
        "/warning/setting",
        {},
        function(data) {
            console.log(data);
            var warningProduct = data.warningProduct;
            var warningClient = data.warningClient;
        /*<tr>
            <td>请求失败过多</td>
            <td class="lh25">
                <div class=" position-r">
                手机短信 <i class="col3 fz-14 ml10 detail cp">详情</i>
                &lt;!&ndash;提示层&ndash;&gt;
        <div class="detai-ceng">
                第三方接口验证请求失败次数过多
                </div>
                &lt;!&ndash;提示层 end&ndash;&gt;
        </div>
            <div class=" position-r">
                现场警报 <i class="col3 fz-14 ml10 detail cp">详情</i>
                &lt;!&ndash;提示层&ndash;&gt;
        <div class="detai-ceng">
                第三方接口验证请求失败次数过多2321312321第三方接口验证请求失败次数过多2321312321第三方接口验证请求失败次数过多2321312321
                </div>
                &lt;!&ndash;提示层 end&ndash;&gt;
        </div>
            </td>
            <td class="lh25">
                严重 100&lt;= 连续出错 &lt;800<br/>
            一般 50&lt;= 连续出错 &lt;100
            </td>
            <td><a href="/warning/setting/edit.html" class="col4 important">编辑</a> | <span
        class="col3 important">停用</span></td>
            </tr>*/
            var htmlStr = "";
            for(var i in warningProduct){

            }
            for(var i in warningClient){

            }
        }
    );
}