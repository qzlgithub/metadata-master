//头部滚动变色
$(window).scroll(function() {
    var myScrollTop = parseInt(get_scrollTop_of_body());//获取ScrollTop的值
    if(myScrollTop >= 85) {//subnav不固定区块定位，注意此值要减掉fixed定位后的top值
        $('.navbar-fixed-top').addClass("header2");
        $('.navbar-fixed-top').removeClass("header1");
    }
    else {
        $('.navbar-fixed-top').addClass("header1");
        $('.navbar-fixed-top').removeClass("header2");
    }
});

function get_scrollTop_of_body() { //scrollTop能力检测函数
    var scrollTop;
    if(typeof window.pageYOffset != 'undefined') {
        scrollTop = window.pageYOffset;
    }
    else if(typeof document.compatMode != 'undefined' &&
        document.compatMode != 'BackCompat') {
        scrollTop = document.documentElement.scrollTop;
    }
    else if(typeof document.body != 'undefined') {
        scrollTop = document.body.scrollTop;
    }
    return scrollTop;
}

/***********公司资质************/
//$(window).load(function() {
//    //set and get some variables
//    var thumbnail = {
//        imgIncrease: 100, /* the image increase in pixels (for zoom) */
//        effectDuration: 400, /* the duration of the effect (zoom and caption) */
//        /*
//        get the width and height of the images. Going to use those
//        for 2 things:
//            make the list items same size
//            get the images back to normal after the zoom
//        *//*
//		imgWidth : $('.thumbnailWrapper ul li').find('img').width(),
//		imgHeight : $('.thumbnailWrapper ul li').find('img').height() */
//    };
//    //make the list items same size as the images
//    $('.thumbnailWrapper ul li').css({
//        'width': thumbnail.imgWidth,
//        'height': thumbnail.imgHeight
//    });
//    //when mouse over the list item...
//    $('.thumbnailWrapper ul li').hover(function() {
//        $(this).find('img').stop().animate({
//            /* increase the image width for the zoom effect*/
//            width: parseInt(thumbnail.imgWidth) + thumbnail.imgIncrease,
//            /* we need to change the left and top position in order to
//            have the zoom effect, so we are moving them to a negative
//            position of the half of the imgIncrease */
//            /*	left: thumbnail.imgIncrease/2*(-1),
//                top: thumbnail.imgIncrease/2*(-1)*/
//        }, {
//            "duration": thumbnail.effectDuration,
//            "queue": false
//        });
//        //show the caption using slideDown event
//        $(this).find('.caption:not(:animated)').slideDown(thumbnail.effectDuration);
//        //when mouse leave...
//    }, function() {
//
//        //find the image and animate it...
//        $(this).find('img').animate({
//            /* get it back to original size (zoom out) */
//            width: thumbnail.imgWidth,
//            /* get left and top positions back to normal */
//            left: 0,
//            top: 0
//        }, thumbnail.effectDuration);
//        //hide the caption using slideUp event
//        $(this).find('.caption').slideUp(thumbnail.effectDuration);
//    });
//});
