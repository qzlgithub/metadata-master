//菜单
$(function() {
    $(".treebox .level1>a").click(function() {
        $(this).addClass('current')   //给当前元素添加"current"样式
        .find('i').addClass('down')   //小箭头向下样式
        .parent().next().slideDown('slow', 'easeOutQuad')  //下一个元素显示
        .parent().siblings().children('a').removeClass('current')//父元素的兄弟元素的子元素去除"current"样式
        .find('i').removeClass('down').parent().next().slideUp('slow', 'easeOutQuad');//隐藏
        //return false; //阻止默认时间
    });
    $(".treebox .menu li").each(function() {
        $(this).find('dl').each(function() {
            if($(this).find('dd').hasClass('layui-this')) {
                $(this).prev().find('i').addClass('down');
                $(this).prev().addClass('current');
                $(this).css("display", "block");
            }
        })
    });
});