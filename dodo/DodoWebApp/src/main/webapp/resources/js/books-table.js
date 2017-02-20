var page = 0;
function pageUp() {
    page += 1;
    loadTable();
}
function pageDown() {
    page -= 1;
    loadTable();
}
function loadTable() {
    $.post('listBookAction.action', {
        'start': (page * 5),
        'count': 5
    }, function (data) {
        var books = JSON.parse(data['books']);
        if (books.length > 0) {
            tmpbooks = books;
            writeBooksToTable(books);
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

var tmpbooks;
function buttonStr(index) {

   return '<div class="btn-group">\
                            <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle"\
                                    type="button">Edit <span class="caret"></span></button>\
                            <ul role="menu" class="dropdown-menu">\
                                <li><a onclick="prepareShow('+index+')" data-toggle="modal"\
                                       data-target="#dodo-modal">View\
                                    detail</a></li>\
                                <li>\
                                    <a onclick="prepareUpdate('+index+')" data-toggle="modal"\
                                       data-target="#dodo-modal">Edit</a>\
                                </li>\
                                <li class="divider"></li>\
                                <li>\
                                    <a onclick="prepareDelete(\''
        + tmpbooks[index].id + '\')" data-toggle="modal"\
                                       data-target="#dodo-delete-modal">Delete</a>\
                                </li>\
                            </ul>\
                        </div>';
}

function writeBooksToTable(books) {
    for (var i = 0; i < books.length; i++) {
        var bid = books[i].id;
        var tt = books[i].title;
        var pb = books[i].publisher;
        $("#" + i + "-bookId").text(bid);
        $("#" + i + "-title").text(tt);
        $("#" + i + "-publisher").text(pb);
        var categorystr = '';
        try {
            for (var j = 0; j < books[i].category.length; j++) {
                if (j > 0) categorystr += ', ';
                categorystr += books[i].category[j].description;
            }
        } catch (error) {
        }

        var authorsstr = '';
        try {
            for (var j = 0; j < books[i].authors.length; j++) {
                if (j > 0) authorsstr += ', ';
                authorsstr += books[i].authors[j].name;
            }
        } catch (error) {
        }

        $("#" + i + "-category").text(categorystr);
        $("#" + i + "-authors").text(authorsstr);
        $("#" + i + "-action").html(buttonStr(i));

    }
}