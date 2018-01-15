$(function() {
    $("#hide-pass").click(function() {
        $("#dec-passcode").val($("#enc-passcode").val());
        $("#pwd-show").hide();
        $("#pwd-hide").show();
    });
    $("#show-pass").click(function() {
        $("#enc-passcode").val($("#dec-passcode").val());
        $("#pwd-hide").hide();
        $("#pwd-show").show();
    });
});

function changeImage() {
    $.get(
        "/captcha",
        function(res) {
            if(res.errCode === '000000') {
                var data = res.dataMap;
                $("#captcha").attr("src", data.imageCaptcha);
            }
        }
    );
}