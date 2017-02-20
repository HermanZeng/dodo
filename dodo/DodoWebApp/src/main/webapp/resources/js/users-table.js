var page = 0;
function pageUp() {
    page += 1;
    loadTable();
}
function pageDown() {
    page -=1;
    loadTable();
}
function loadTable() {
    $.post('listUserAction.action', {
        'start': (page * 5),
        'count': 5
    }, function (data) {
        var users = eval(data);
        if (users.length > 0) {
            writeUsersToTable(users);
            if (page == 0) {
                $("#page-older").addClass("disabled").find("a").attr("onclick", "");
            } else {
                $("#page-older").removeClass("disabled").find("a").attr("onclick", "pageDown()");
            }
        }
        else {
            if (page > 0) {
                page -= 1;
                loadTable();
            } else {
                clearTable();
                $("#page-older").addClass("disabled");
            }
        }
    });
}

function clearTable() {
    $(".dodo-datagrid-td").html("");
}

function buttonStr(user) {
    var uid = user.userId;
    var eml = user.email;
    var fn = user.firstName;
    var ln = user.lastName;

    var str = '<div class="btn-group">\
                            <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle"\
                                    type="button">Edit <span class="caret"></span></button>\
                            <ul role="menu" class="dropdown-menu">\
                                <li><a onclick="prepareShow(\'' + uid + '\',\
                                        \'' + fn + '\',\
                                        \'' + ln + '\',\
                                        \'' + eml + '\')" data-toggle="modal"\
                                       data-target="#user-dialog">View\
                                    detail</a></li>\
                                <li>\
                                    <a onclick="prepareUpdate(\'' + uid + '\',\
                                            \''
        + fn + '\',\
                                            \''
        + ln + '\',\
                                            \''
        + eml + '\')" data-toggle="modal"\
                                       data-target="#user-dialog">Edit</a>\
                                </li>\
                                <li class="divider"></li>\
                                <li>\
                                    <a onclick="prepareDelete(\''
        + uid + '\')" data-toggle="modal"\
                                       data-target="#delete-dialog">Delete</a>\
                                </li>\
                            </ul>\
                        </div>';
    return str;
}

function writeUsersToTable(users) {
    for (var i = 0; i < users.length; i++) {
        var uid = users[i].userId;
        var eml = users[i].email;
        var fn = users[i].firstName;
        var ln = users[i].lastName;
        $("#" + i + "-userId").text(uid);
        $("#" + i + "-email").text(eml);
        $("#" + i + "-firstName").text(fn);
        $("#" + i + "-lastName").text(ln);
        var rolestr = '';
        try {
            for (var j = 0; j < users[i].roles.length; j++) {
                if (j>0) rolestr += ', ';
                rolestr += users[i].roles[j].description;
            }
        } catch (error) {
        }

        $("#" + i + "-roles").text(rolestr);
        $("#" + i + "-action").html(buttonStr(users[i]));

    }
}