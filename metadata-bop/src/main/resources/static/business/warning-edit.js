layui.config({
    base: '../../static/build/js/'
}).use(['app'], function() {
    $ = layui.jquery
    app = layui.app;
    app.set({
        type: 'iframe'
    }).init();
});
var isSubmit = false;

function saveData() {
    if(isSubmit) {
        return;
    }
    isSubmit = true;
    var generalLimit = $.trim($("#generalLimit").val());
    var severityLimit = $.trim($("#severityLimit").val());
    var warningLimit = $.trim($("#warningLimit").val());
    if(generalLimit == '' || severityLimit == '' || warningLimit == '') {
        layer.msg("保存失败，预警阀值存在空值！", {time: 3000});
        isSubmit = false;
        return;
    }
    var generalLimit = parseInt(generalLimit);
    var severityLimit = parseInt(severityLimit);
    var warningLimit = parseInt(warningLimit);
    if(generalLimit == severityLimit || generalLimit == warningLimit || severityLimit == warningLimit) {
        layer.msg("保存失败，预警阀值不能存在相同的数值！", {time: 3000});
        isSubmit = false;
        return;
    }
    if(generalLimit >= severityLimit || generalLimit >= warningLimit) {
        layer.msg("保存失败，预警阀值大小关系不匹配！", {time: 3000});
        isSubmit = false;
        return;
    }
    var options = {
        url: '/warning/setting/modification',
        success: showReply,
        clearForm: false,
        resetForm: false,
        type: 'post'
    };
    $('#formData').ajaxSubmit(options);
}

function showReply(res) {
    if(res.code !== '000000') {
        layer.msg(res.message, {time: 2000});
        isSubmit = false;
    }
    else {
        layer.msg("保存成功", {
            time: 2000
        }, function() {
            window.location.href = "/warning/setting/list.html";
        });
    }
}

function fileChange(target) {
    var fileSize = 0;
    if(isIE() && !target.files) {
        var filePath = target.value;
        var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
        var file = fileSystem.GetFile(filePath);
        fileSize = file.Size;
    }
    else {
        fileSize = target.files[0].size;
    }
    var size = fileSize / 1024;
    if(size > 1024 * 3) {
        layer.msg("附件不能大于3M", {time: 2000});
        target.value = "";
        return
    }
    var name = target.value;
    var fileName = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
    if(fileName != "mp3") {
        layer.msg("请选择mp3格式文件上传！", {time: 2000});
        target.value = "";
        return
    }
    $("#playButton").hide();
    $("#showFileName").text(name);
    if($("#music").length > 0) {
        var audio = document.getElementById('music');
        if(!audio.paused) {
            audio.pause();
        }
    }
}

function isIE() {
    return ("ActiveXObject" in window);
}

function bf() {
    var audio = document.getElementById('music');
    if(audio !== null) {
        //检测播放是否已暂停.audio.paused 在播放器播放时返回false.
        //alert(audio.paused);
        if(audio.paused) {
            audio.play();//audio.play();// 这个就是播放
        }
        else {
            audio.pause();// 这个就是暂停
        }
    }
}