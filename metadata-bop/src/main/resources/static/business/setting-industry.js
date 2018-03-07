var message;
layui.config({
    base: '../../static/build/js/'
}).use(['form', 'app', 'message'], function() {
    var form = layui.form,
        $ = layui.jquery,
        layer = layui.layer;
    message = layui.message;
    $('#addindustry').on('click', function() {
        layer.open({
            title: false,
            type: 1,
            content: $('#add-industry'),
            area: ['500px'],
            shadeClose: true
        });
    });
    form.on('switch(switchTest)', function() {
        var id = $(this).parent().attr("obj-id");
        var checked = this.checked;
        var parent = $(this).data('parent');
        if(parent) {
            $(".switch-" + parent).prop("checked", checked);
        }
        enable_recharge_type(id, checked);
        form.render('checkbox');
    });
});

function enable_recharge_type(id, enabled) {
    $.ajax({
        type: "post",
        url: "/setting/industry/status",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "id": id,
            "enabled": enabled ? 1 : 0
        })
    });
}

//展开合起
(function() {
    var d = document,
        accordionToggles = d.querySelectorAll('.js-accordionTrigger'),
        setAria,
        setAccordionAria,
        switchAccordion,
        touchSupported = ('ontouchstart' in window),
        pointerSupported = ('pointerdown' in window);
    skipClickDelay = function(e) {
        e.preventDefault();
        e.target.click();
    };
    setAriaAttr = function(el, ariaType, newProperty) {
        el.setAttribute(ariaType, newProperty);
    };
    setAccordionAria = function(el1, el2, expanded) {
        switch(expanded) {
            case "true":
                setAriaAttr(el1, 'aria-expanded', 'true');
                setAriaAttr(el2, 'aria-hidden', 'false');
                break;
            case "false":
                setAriaAttr(el1, 'aria-expanded', 'false');
                setAriaAttr(el2, 'aria-hidden', 'true');
                break;
            default:
                break;
        }
    };
    //function
    switchAccordion = function(e) {
        e.preventDefault();
        var thisAnswer = e.target.parentNode.nextElementSibling;
        var thisQuestion = e.target;
        if(thisAnswer.classList.contains('is-collapsed')) {
            setAccordionAria(thisQuestion, thisAnswer, 'true');
        }
        else {
            setAccordionAria(thisQuestion, thisAnswer, 'false');
        }
        thisQuestion.classList.toggle('is-collapsed');
        thisQuestion.classList.toggle('is-expanded');
        thisAnswer.classList.toggle('is-collapsed');
        thisAnswer.classList.toggle('is-expanded');
        thisAnswer.classList.toggle('animateIn');
    };
    for(var i = 0, len = accordionToggles.length; i < len; i++) {
        if(touchSupported) {
            accordionToggles[i].addEventListener('touchstart', skipClickDelay, false);
        }
        if(pointerSupported) {
            accordionToggles[i].addEventListener('pointerdown', skipClickDelay, false);
        }
        accordionToggles[i].addEventListener('click', switchAccordion, false);
    }
})();
$(".add-child").on('click', function() {
    var parentId = $(this).attr("data-id");
    $("#l2-parent-id").val(parentId);
    $("#l2-code").val("");
    $("#l2-name").val("");
    layer.open({
        title: false,
        type: 1,
        content: $('#child-industry'),
        area: ['500px'],
        shadeClose: true
    });
});
$(".edit-industry").on('click', function() {
    var id = $(this).attr("data-id");
    var obj = $("#industry-id-" + id);
    var name = obj.find(".industry-name-class").text();
    var code = obj.find(".industry-code-class").text();
    $("#edit-id").val(id);
    $("#edit-name").val(name);
    $("#edit-code").val(code);
    layer.open({
        title: false,
        type: 1,
        content: $('#edit-industry'),
        area: ['500px'],
        shadeClose: true
    });
});

function checkNewCode() {
    var code = $("#new-code").val();
    if(code !== "") {
        $.get(
            "/setting/industry/verification",
            {"code": code},
            function(res) {
                if(res.code === '000000' && res.data.exist === 1) {
                    $("#tip-new-code").text("该行业编号已被占用");
                }
                else {
                    $("#tip-new-code").text("");
                }
            }
        );
    }
    else {
        $("#tip-new-code").text("");
    }
}

function addIndustry(type) {
    if(type !== 1 && type !== 2) {
        return;
    }
    var id, code, name;
    if(type === 1) {
        code = $("#new-code").val();
        name = $("#new-name").val();
        if(code === "") {
            $("#tip-new-code").text("行业编号不能为空");
            return;
        }
        if(name === "") {
            $("#tip-new-name").text("行业名称不能为空");
            return;
        }
    }
    else {
        id = $("#l2-parent-id").val();
        code = $("#l2-code").val();
        name = $("#l2-name").val();
        if(code === "") {
            $("#tip-l2-code").text("行业编号不能为空");
            return;
        }
        if(name === "") {
            $("#tip-l2-name").text("行业名称不能为空");
            return;
        }
    }
    saveIndustry(id, code, name);
}

function saveIndustry(id, code, name) {
    $.ajax({
        type: "PUT",
        url: "/setting/industry/addition",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"id": id, "code": code, "name": name}),
        success: function(res) {
            if(res.code !== "000000") {
                layer.msg("添加失败" + res.message, {
                    time: 2000
                });
            }
            else {
                layer.msg("添加成功", {
                    time: 2000
                }, function() {
                    window.location.href = "/setting/industry.html";
                });
            }
        }
    });
}

//添加主行业和子行业
$(document).on("click", ".js_addIndustry", function() {
    var $frm = $("#industryForm");
    $.ajax({
        data: $frm.serialize(),
        dataType: "json",
        type: "post",
        url: "/industry/add",
        success: function(data) {
            layer.msg("添加成功", {
                time: 2000
            }, function() {
                window.location.href = "/industry/manage";
            });
        },
        error: function(data) {
            layer.msg("添加失败" + data.errMsg, {
                time: 2000
            });
        }
    })
});
$(document).on("click", ".js_addChildIndustry", function() {
    var parId = $(this).data("name");
    var $frm = $("#childIndustry");
    $.ajax({
        data: $frm.serialize(),
        dataType: "json",
        type: "post",
        url: "/industry/add",
        success: function(data) {
            layer.msg("添加成功", {
                time: 2000
            }, function() {
                window.location.href = "/industry/manage";
            });
        },
        error: function(data) {
            layer.msg("添加失败" + data.errMsg, {
                time: 2000
            });
        }
    })
});

function editIndustry() {
    var id = $("#edit-id").val();
    var code = $("#edit-code").val();
    var name = $("#edit-name").val();
    $.ajax({
        type: "POST",
        url: "/setting/industry",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"id": id, "code": code, "name": name}),
        success: function(res) {
            if(res.code !== "000000") {
                layer.msg("修改失败：" + res.message, {time: 2000});
            }
            else {
                window.location.href = "/setting/industry.html";
            }
        }
    });
}