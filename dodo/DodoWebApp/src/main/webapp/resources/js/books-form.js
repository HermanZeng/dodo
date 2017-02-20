var formType = 0; // create: 0, update: 1, show: 2;
var currentIndex;
var CATEGORY = [
    {
        "id": 0,
        "description": "文学",
        "reference": 0
    },
    {
        "id": 1,
        "description": "历史",
        "reference": 1
    },
    {
        "id": 2,
        "description": "科技",
        "reference": 2
    },
    {
        "id": 3,
        "description": "工具书",
        "reference": 3
    }
];

var modifyingAuthors = true;
var canAuthors = {};
var canTranslators = {};

function focusOnSearch() {
    $("#search").focus();
}

function openSearchModal(index) {
    modifyingAuthors = index == 0;
    focusOnSearch();
}

function removeACandidate(cid) {
    delete canAuthors[cid];
    $("#acan" + cid).remove();
}

function removeTCandidate(cid) {
    delete canTranslators[cid];
    $("#tcan" + cid).remove();
}

function prepareShow(index) {
    $("#dodo-modal-title").text('Book Detail');

    $(".dodo-form-item-tip").css({"display": "none"});
    $(".dodo-form-item").css({"border-color": "#66afe9"});

    $("#form-id-block").show();
    $("#form-rt-block").show();
    $("#form-img-block").show();
    $("#form-pb-block").hide();
    $("#form-ctg-block").hide();
    $("#form-authors-block").hide();
    $("#form-translators-block").hide();
    $("#dodo-form-savebtn").hide();

    var book = tmpbooks[index];

    $("#form-title").attr('disabled', true).val(book.title);
    $("#form-bookId").attr('disabled', true).val(book.id);
    $("#form-wid").attr('disabled', true).val(book.wid);
    $("#form-intro").attr('disabled', true).val(book.introduction);
    $("#form-image").attr('disabled', true).val(book.image);
    $("#form-page").attr('disabled', true).val(book.pages);
    $("#form-isbn10").attr('disabled', true).val(book.isbn10);
    $("#form-isbn13").attr('disabled', true).val(book.isbn13);
    $("#form-rate").attr('disabled', true).val(book.rate);

    formType = 2;
}

function initSelect(ctgstr) {
    $("#form-ctg-block").html('').html('<label class="control-label">Category</label>\
        <div class="controls">\
        <select id="form-category" multiple="multiple" >\
        </select>\
        <br />\
        <span id="form-category-tip"\
        class="form-item-tip"\
        style="display:none;color:red;"></span>\
        <p class="help-block"></p>\
        </div>');

    var ctgs = ctgstr.split(',');
    for (var i = 0; i < CATEGORY.length; i++) {
        var str = '';
        if (ctgs.indexOf(String(i)) >= 0) {
            str = ' selected="selected" ';
        }
        $("#form-category").append('<option ' + str + 'value="' + CATEGORY[i].reference + '">' + CATEGORY[i].description + '</option>')

    }
    $('#form-category').multiselect({
        nonSelectedText: 'choose a category',
        onChange: function (option, checked, select) {
            $("#form-category").css({"border-color": "#66afe9"});
            $("#form-category-tip").css({"display": "none"});
        }
    });
}

function prepareUpdate(index) {
    $("#dodo-modal-title").text('Edit Book');

    $(".dodo-form-item-tip").css({"display": "none"});
    $(".dodo-form-item").css({"border-color": "#66afe9"});

    $("#dodo-form-savebtn").show();

    $("#form-id-block").show();
    $("#form-pb-block").show();
    $("#form-img-block").show();
    $("#form-ctg-block").show();
    $("#form-authors-block").show();
    $("#form-translators-block").show();
    $("#form-rt-block").hide();

    $(".dodo-removable-label").remove();
    canAuthors = {};
    canTranslators = {};

    var book = tmpbooks[index];
    currentIndex = index;

    $("#form-bookId").attr('disabled', true).val(book.id);
    $("#form-title").val(book.title).removeAttr('disabled');
    $("#form-wid").val(book.wid).removeAttr('disabled');
    $("#form-intro").val(book.introduction).removeAttr('disabled');
    $("#form-page").val(book.pages).removeAttr('disabled');
    $("#form-isbn10").val(book.isbn10).removeAttr('disabled');
    $("#form-isbn13").val(book.isbn13).removeAttr('disabled');
    $("#form-image").val(book.image).removeAttr('disabled');
    $("#form-publisher").val(book.publisher).removeAttr('disabled');
    loadAuthorTranslator(book.authors, 0);
    loadAuthorTranslator(book.translators, 1);

    var catstr = '';
    for (var i = 0; i < book.category.length; i++) {
        if (i > 0) {
            catstr += ',';
        }
        catstr += book.category[i].reference;
    }
    initSelect(catstr);

    formType = 1;
}

function loadAuthorTranslator(data, op) {
    if (op == 0) {
        if (data != null) {
            // load author
            for (var i = 0; i < data.length; i++) {
                canAuthors[data[i].id] = data[i];

                $("#authors-plus").before('<span class="btn btn-default dodo-removable-label" id=acan'
                    + data[i].id
                    + '>'
                    + data[i].name
                    + '<span>&nbsp;&nbsp;|&nbsp;&nbsp;</span><a onclick="removeACandidate(\''
                    + data[i].id
                    + '\')"><span class="glyphicon glyphicon-remove"></span></a></span>');
            }
        }
    } else {
        if (data != null) {
            // load translator
            for (var i = 0; i < data.length; i++) {
                canTranslators[data[i].id] = data[i];

                $("#translators-plus").before('<span class="btn btn-default dodo-removable-label" id=tcan'
                    + data[i].id
                    + '>'
                    + data[i].name
                    + '<span>&nbsp;&nbsp;|&nbsp;&nbsp;</span><a onclick="removeTCandidate(\''
                    + data[i].id
                    + '\')"><span class="glyphicon glyphicon-remove"></span></a></span>');
            }
        }
    }
}

function prepareCreate() {
    $("#dodo-modal-title").text('Add Book');

    $(".dodo-form-item-tip").css({"display": "none"});
    $(".dodo-form-item").css({"border-color": "#66afe9"});

    $("#form-id-block").hide();
    $("#form-rt-block").hide();
    $("#form-img-block").show();
    $("#form-ctg-block").show();
    $("#form-pb-block").show();
    $("#form-authors-block").show();
    $("#form-translators-block").show();

    $("#dodo-form-savebtn").show();

    $(".dodo-form-item").val('').removeAttr('disabled');
    initSelect('');
    $(".dodo-removable-label").remove();
    canAuthors = {};
    canTranslators = {};

    formType = 0;
}

function prepareDelete(deleteId) {
    $("#deleteId").text(deleteId);
}

$(document).ready(function () {
    loadTable();

    $('#dodo-search-modal').on('shown.bs.modal', function () {
        $('#search').focus();
    }).on('hidden.bs.modal', function () {
        $("#search").typeahead('val', '');

    });

    $('#form-category').multiselect({
        nonSelectedText: 'choose a category',
        onChange: function (option, checked, select) {
            $("#form-category").css({"border-color": "#66afe9"});
            $("#form-category-tip").css({"display": "none"});
        }
    });

    $("#form-isbn10").keypress(function () {
        $("#form-isbn10").css({"border-color": "#66afe9"});
        $("#form-isbn10-tip").css({"display": "none"});

    }).blur(function () {
        var is10 = $("#form-isbn10").val();
        if (!validIs10(is10)) {
            showError('isbn10', 'invalid ISBN-10');
        }
    });

    $("#form-isbn13").keypress(function () {
        $("#form-isbn13").css({"border-color": "#66afe9"});
        $("#form-isbn13-tip").css({"display": "none"});

    }).blur(function () {
        var is13 = $("#form-isbn13").val();
        if (!validIs13(is13)) {
            showError('isbn13', 'invalid ISBN-13');
        }
    });


});

function validIs10(arg) {
    return arg.search(/^\d{10}$/) != -1;
}

function validIs13(arg) {
    return arg.search(/^\d{13}$/) != -1;
}

function validUpdate() {
    var is10 = $("#form-isbn10").val();
    var is13 = $("#form-isbn13").val();
    var ctglist = $("#form-category").val();

    var valid = true;
    if (!validIs10(is10)) {
        valid = false;
        showError('isbn10', 'invalid ISBN-10');
    }
    if (!validIs13(is13)) {
        valid = false;
        showError('isbn13', 'invalid ISBN-13');
    }
    if (ctglist == null) {
        valid = false;
        showError('category', 'category cannot be empty.');
    }
    if (countKey(canAuthors) < 1) {
        valid = false;
        showError('authors', 'authors cannot be empty.');
    }

    return valid;
}

function validCreate() {
    var is10 = $("#form-isbn10").val();
    var is13 = $("#form-isbn13").val();
    var ctglist = $("#form-category").val();

    var valid = true;
    if (!validIs10(is10)) {
        valid = false;
        showError('isbn10', 'invalid ISBN-10');
    }
    if (!validIs13(is13)) {
        valid = false;
        showError('isbn13', 'invalid ISBN-13');
    }
    if (ctglist == null) {
        valid = false;
        showError('category', 'category cannot be empty.');
    }

    if (countKey(canAuthors) < 1) {
        valid = false;
        showError('authors', 'authors cannot be empty.');
    }

    // if (countKey(canTranslators)){
    //     valid = false;
    //     showError('translators', 'translators cannot be empty.');
    // }

    return valid;
}

function countKey(ob) {
    var cnt = 0;
    for (var key in ob) {
        cnt += 1;
    }
    return cnt;
}

function doUpdate() {
    if (validUpdate()) {
        var book = tmpbooks[currentIndex];
        book.id = $("#form-bookId").val();
        book.title = $("#form-title").val();
        book.wid = $("#form-wid").val();
        book.introduction = $("#form-intro").val();
        book.pages = $("#form-page").val();
        book.isbn10 = $("#form-isbn10").val();
        book.isbn13 = $("#form-isbn13").val();
        book.image = $("#form-image").val();
        book.publisher = $("#form-publisher").val();
        book.category = getCategory();
        book.authors = getAuthorsTranslators(0);
        book.translators = getAuthorsTranslators(1);

        $.post('updateBookAction.action', {
            'bookstr': JSON.stringify(book)
        }, function (data) {
            if (data['success']) {
                ShowSuccess('book updated!');
                setTimeout("$('#dodo-modal').modal('hide');", 900);
                loadTable();
            } else {
                ShowFailure(data['msg']);
            }
        });
    }
    event.preventDefault();
}

function getCategory() {
    var ret = [];
    var ctglist = $("#form-category").val();

    for (var i = 0; i < ctglist.length; i++) {
        for (var j = 0; j < CATEGORY.length; j++) {
            if (CATEGORY[j].reference == ctglist[i]) {
                ret = ret.concat(CATEGORY[j]);
            }
        }
    }

    return ret;
}

function getAuthorsTranslators(op) {
    var ret = [];
    if (op == 0) {
        for (var mykey in canAuthors) {
            ret = ret.concat(canAuthors[mykey]);
        }
    } else {
        for (var mykey in canTranslators) {
            ret = ret.concat(canTranslators[mykey]);
        }
    }
    return ret;
}

function doCreate() {
    if (validCreate()) {
        var book = {};
        book.title = $("#form-title").val();
        book.wid = $("#form-wid").val();
        book.introduction = $("#form-intro").val();
        book.publisher = $("#form-publisher").val();
        book.pages = $("#form-page").val();
        book.isbn10 = $("#form-isbn10").val();
        book.isbn13 = $("#form-isbn13").val();
        book.image = $("#form-image").val();
        book.category = getCategory();
        book.authors = getAuthorsTranslators(0);
        book.translators = getAuthorsTranslators(1);

        $.post('addBookAction.action', {
            'bookstr': JSON.stringify(book)
        }, function (data) {
            if (data['success']) {
                ShowSuccess('book created!');
                setTimeout("$('#dodo-modal').modal('hide');", 900);
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
    $.post('deleteBookAction.action', {
        'ID': id
    }, function (data) {
        if (data['success']) {
            ShowSuccess('book deleted!');
            setTimeout("$('#dodo-delete-modal').modal('hide');", 900);
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
    $(".dodo-msg-box").css({'display': 'inline'}).text(msg).attr("class", "dodo-msg-box alert alert-dismissable alert-" + type).fadeIn(100).delay(300).fadeOut(600);
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