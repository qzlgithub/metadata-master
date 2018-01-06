function resetPrivilege() {
    var roleId = $("#roleId").val();
    $.get(
        "/role/privilege",
        {"roleId": roleId},
        function(data) {
            $(".privilege").removeAttr("checked");
            for(var i in data) {
                $("#" + data[i]).attr("checked", "checked");
            }
        }
    );
}

function saveManager() {
    var managerId = $("#managerId").val();
    var roleId = $("#roleId").val();
    var username = $("#username").val();
    var name = $("#name").val();
    var phone = $("#phone").val();
    var enabled = $("input[name='enabled']:checked").val();
    var privilege = build_privilege();
    console.log("roleId: " + roleId + "\nusername: " + username + "\nname: " + name + "\nphone: " + phone
        + "\nenabled: " + enabled);
    $.ajax({
        type: "POST",
        url: "/manager/modification",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "managerId": managerId,
            "roleId": roleId,
            "username": username,
            "name": name,
            "phone": phone,
            "enabled": enabled,
            "privilege": privilege
        }),
        success: function(data) {
            if(data.errCode !== "000000") {
                alert("失败：" + data.errMsg);
            }
            else {
                window.location.href = "/manager/index.html";
            }
        }
    });
}

function build_privilege() {
    var privilege = [];
    if($("#11101").is(":checked")) {
        privilege.push("11101");
    }
    if($("#11102").is(":checked")) {
        privilege.push("11102");
    }
    if($("#11103").is(":checked")) {
        privilege.push("11103");
    }
    if($("#11201").is(":checked")) {
        privilege.push("11201");
    }
    if($("#11202").is(":checked")) {
        privilege.push("11202");
    }
    if($("#11203").is(":checked")) {
        privilege.push("11203");
    }
    if($("#12101").is(":checked")) {
        privilege.push("12101");
    }
    if($("#12102").is(":checked")) {
        privilege.push("12102");
    }
    if($("#12103").is(":checked")) {
        privilege.push("12103");
    }
    if($("#12104").is(":checked")) {
        privilege.push("12104");
    }
    if($("#12201").is(":checked")) {
        privilege.push("12201");
    }
    if($("#12202").is(":checked")) {
        privilege.push("12202");
    }
    if($("#12203").is(":checked")) {
        privilege.push("12203");
    }
    if($("#12204").is(":checked")) {
        privilege.push("12204");
    }
    if($("#12301").is(":checked")) {
        privilege.push("12301");
    }
    if($("#12302").is(":checked")) {
        privilege.push("12302");
    }
    if($("#12303").is(":checked")) {
        privilege.push("12303");
    }
    if($("#12304").is(":checked")) {
        privilege.push("12304");
    }
    if($("#12305").is(":checked")) {
        privilege.push("12305");
    }
    if($("#12401").is(":checked")) {
        privilege.push("12401");
    }
    if($("#12402").is(":checked")) {
        privilege.push("12402");
    }
    if($("#21101").is(":checked")) {
        privilege.push("21101");
    }
    if($("#21102").is(":checked")) {
        privilege.push("21102");
    }
    if($("#21103").is(":checked")) {
        privilege.push("21103");
    }
    if($("#21201").is(":checked")) {
        privilege.push("21201");
    }
    if($("#21202").is(":checked")) {
        privilege.push("21202");
    }
    if($("#21203").is(":checked")) {
        privilege.push("21203");
    }
    if($("#31101").is(":checked")) {
        privilege.push("31101");
    }
    if($("#31102").is(":checked")) {
        privilege.push("31102");
    }
    if($("#31103").is(":checked")) {
        privilege.push("31103");
    }
    if($("#31104").is(":checked")) {
        privilege.push("31104");
    }
    if($("#31105").is(":checked")) {
        privilege.push("31105");
    }
    if($("#31106").is(":checked")) {
        privilege.push("31106");
    }
    if($("#31107").is(":checked")) {
        privilege.push("31107");
    }
    if($("#31108").is(":checked")) {
        privilege.push("31108");
    }
    if($("#31109").is(":checked")) {
        privilege.push("31109");
    }
    if($("#31110").is(":checked")) {
        privilege.push("31110");
    }
    if($("#31111").is(":checked")) {
        privilege.push("31111");
    }
    if($("#31112").is(":checked")) {
        privilege.push("31112");
    }
    if($("#31113").is(":checked")) {
        privilege.push("31113");
    }
    if($("#31114").is(":checked")) {
        privilege.push("31114");
    }
    if($("#41101").is(":checked")) {
        privilege.push("41101");
    }
    if($("#41201").is(":checked")) {
        privilege.push("41201");
    }
    if($("#41202").is(":checked")) {
        privilege.push("41202");
    }
    if($("#41301").is(":checked")) {
        privilege.push("41301");
    }
    if($("#41302").is(":checked")) {
        privilege.push("41302");
    }
    if($("#41401").is(":checked")) {
        privilege.push("41401");
    }
    if($("#41402").is(":checked")) {
        privilege.push("41402");
    }
    if($("#41501").is(":checked")) {
        privilege.push("41501");
    }
    if($("#41502").is(":checked")) {
        privilege.push("41502");
    }
    return privilege;
}