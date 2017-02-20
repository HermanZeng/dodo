var formType = 0; // create: 0, update: 1, show: 2;
function prepareShow(currentId, currentFName, currentLName, currentEmail) {
    $("#dodo-modal-title").text('User Detail');

    $(".dodo-form-item-tip").css({"display": "none"});
    $(".dodo-form-item").css({"border-color": "#66afe9"});

    $("#form-id-block").show();
    $("#form-password-block").hide();
    $("#form-rpassword-block").hide();
    $("#dodo-form-savebtn").hide();

    $("#form-userId").attr('disabled', true).val(currentId);
    $("#form-email").attr('disabled', true).val(currentEmail);
    $("#form-firstName").attr('disabled', true).val(currentFName);
    $("#form-lastName").attr('disabled', true).val(currentLName);

    formType = 2;
}

function prepareUpdate(currentId, currentFName, currentLName, currentEmail) {
    $("#dodo-modal-title").text('Edit User');

    $(".dodo-form-item-tip").css({"display": "none"});
    $(".dodo-form-item").css({"border-color": "#66afe9"});

    $("#form-id-block").show();
    $("#form-password-block").hide();
    $("#form-rpassword-block").hide();
    $("#dodo-form-savebtn").show();

    $("#form-userId").attr('disabled', true).val(currentId);
    $("#form-email").val(currentEmail).removeAttr('disabled');
    $("#form-firstName").val(currentFName).removeAttr('disabled');
    $("#form-lastName").val(currentLName).removeAttr('disabled');

    formType = 1;
}

function prepareCreate() {
    $("#dodo-modal-title").text('Add User');

    $(".dodo-form-item-tip").css({"display": "none"});
    $(".dodo-form-item").css({"border-color": "#66afe9"});

    $("#form-id-block").hide();
    $("#form-password-block").show();
    $("#form-rpassword-block").show();
    $("#dodo-form-savebtn").show();

    $(".dodo-form-item").val('').removeAttr('disabled');

    formType = 0;
}

function prepareDelete(deleteId) {
    $("#deleteId").text(deleteId);
}

$(document).ready(function () {
    loadTable();

    $("#form-firstName").keypress(function () {
        $("#form-firstName").css({"border-color": "#66afe9"});
        $("#form-firstName-tip").css({"display": "none"});

    });

    $("#form-lastName").keypress(function () {
        $("#form-lastName").css({"border-color": "#66afe9"});
        $("#form-lastName-tip").css({"display": "none"});

    });

    $("#form-email").keypress(function () {
        $("#form-email").css({"border-color": "#66afe9"});
        $("#form-email-tip").css({"display": "none"});

    });

    $("#form-password").keypress(function () {
        $("#form-password").css({"border-color": "#66afe9"});
        $("#form-password-tip").css({"display": "none"});

    });

    $("#form-rpassword").keypress(function () {
        $("#form-rpassword").css({"border-color": "#66afe9"});
        $("#form-rpassword-tip").css({"display": "none"});

    });

    $("#form-email").blur(function () {
        var email = $("#form-email").val();
        if (!validEmail(email)) {
            showError('email', 'invalid email');
        }
    });

    $("#form-password").blur(function () {
        var pwd = $("#form-password").val();
        if (!validPwd(pwd)) {
            showError('password', 'invalid password');
        }
    });

    $("#form-rpassword").blur(function () {
        var pwd = $("#form-password").val();
        var rpwd = $("#form-rpassword").val();
        if (pwd != '' && pwd != rpwd) {
            showError('rpassword', 'not equals');
        }
    });

});

function validEmail(email) {
    return email.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) != -1;
}

function validName(name) {
    return name.search(/^([a-zA-Z0-9]|[\u4e00-\u9fa5]|_){3,30}$/) != -1;
}

function validPwd(pwd) {
    return pwd.search(/^\w{6,18}$/) != -1;
}

function validUpdate() {
    var fnval = $("#form-firstName").val();
    var lnval = $("#form-lastName").val();
    var emval = $("#form-email").val();

    var valid = true;
    if (!validEmail(emval)) {
        valid = false;
        showError('email', 'invalid email address');
    }
    if (!validName(fnval)) {
        valid = false;
        showError('firstName', 'invalid first name');
    }
    if (!validName(lnval)) {
        valid = false;
        showError('lastName', 'invalid last name');
    }

    return valid;
}

function validCreate() {
    var fnval = $("#form-firstName").val();
    var lnval = $("#form-lastName").val();
    var emval = $("#form-email").val();
    var pwval = $("#form-password").val();
    var rpwval = $("#form-rpassword").val();

    var valid = true;
    if (!validEmail(emval)) {
        valid = false;
        showError('email', 'invalid email address');
    }
    if (!validName(fnval)) {
        valid = false;
        showError('firstName', 'invalid first name');
    }
    if (!validName(lnval)) {
        valid = false;
        showError('lastName', 'invalid last name');
    }
    if (!validPwd(pwval)) {
        valid = false;
        showError('password', 'invalid password');
    }
    if (pwval != rpwval) {
        valid = false;
        showError('rpassword', 'not equals');
    }

    return valid;
}

function doUpdate() {
    if (validUpdate()) {
        var id = $("#form-userId").val();
        var email = $("#form-email").val();
        var firstname = $("#form-firstName").val();
        var lastname = $("#form-lastName").val();
        $.post('updateUserAction.action', {
            'user.userId': id,
            'user.email': email,
            'user.firstName': firstname,
            'user.lastName': lastname
        }, function (data) {
            if (data['success']) {
                ShowSuccess('user updated!');
                setTimeout("$('#user-dialog').modal('hide');", 900);
                loadTable();
            } else {
                ShowFailure(data['msg']);
            }
        });
    }
    event.preventDefault();
}

function doCreate() {
    if (validCreate()) {
        var email = $("#form-email").val();
        var firstname = $("#form-firstName").val();
        var lastname = $("#form-lastName").val();
        var password = $("#form-password").val();
        $.post('addUserAction.action', {
            'user.email': email,
            'user.firstName': firstname,
            'user.lastName': lastname,
            'user.password': password
        }, function (data) {
            if (data['success']) {
                ShowSuccess('user created!');
                setTimeout("$('#user-dialog').modal('hide');", 900);
                loadTable();
            } else {
                ShowFailure(data['msg']);
            }
        });
    }
    event.preventDefault();
}

function commitForm() {
    switch (formType) {
        case 0:
            doCreate();
            break;
        case 1:
            doUpdate();
            break;
    }
}

function commitDelete() {
    var id = $("#deleteId").text();
    $.post('deleteUserAction.action', {
        'user.userId': id
    }, function (data) {
        if (data['success']) {
            ShowSuccess('user deleted!');
            setTimeout("$('#delete-dialog').modal('hide');", 900);
        } else {
            ShowFailure(data['msg']);
        }
    });
}

function showError(formSpan, errorText) {
    $("#" + "form-" + formSpan).css({"border-color": "red"});
    $("#" + "form-" + formSpan + "-tip").empty().append(errorText).css({"display": "inline"});
}

//tip是提示信息，type:'success'是成功信息，'danger'是失败信息,'info'是普通信息,'warning'是警告信息
function ShowTip(msg, type) {
    // if ($tip.length == 0) {
    //     $tip = $('<strong id="tip" style="position:absolute;top:50px;left: 50%;z-index:9999"></strong>');
    //     $('body').append($tip);
    // }
    $(".my-msg-box").css({'display':'inline'}).text(msg).attr("class","my-msg-box alert alert-dismissable alert-" + type).fadeIn(100).delay(300).fadeOut(600);
}

function ShowMsg(msg) {
    // $('#msg-box').attr("class","alert alert-info alert-dismissable");
    ShowTip(msg, 'info');
}

function ShowSuccess(msg) {
    // $('#msg-box').attr("class","alert alert-success alert-dismissable");
    ShowTip(msg, 'success');
}

function ShowFailure(msg) {
    // $('#msg-box').attr("class","alert alert-danger alert-dismissable");
    ShowTip(msg, 'danger');
}

function ShowWarn(msg, $focus, clear) {
    // $('#msg-box').attr("class","alert alert-warning alert-dismissable");
    ShowTip(msg, 'warning');
}